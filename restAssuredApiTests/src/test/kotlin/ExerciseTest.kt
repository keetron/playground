import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.blankOrNullString
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


class ExerciseTest {

    @AfterEach
    fun cleanup() {
        Given {
            port(8080)
        } When {
            delete("/all")
        } Then {
            statusCode(204)
            body("message", equalTo("Database clean succeeded"))
        }
    }

    @Test
    fun postSingleActivity() {
        val activity =
            """{
                "type": "Run",
                "distance": 10000,
                "start": 1678279436,
                "finish": 1678275836
            }"""

        val message: String = Given {
            port(8080)
            body(activity)
        } When {
            post("/activity")
        } Then {
            statusCode(201)
            body("id", not(blankOrNullString()))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        } Extract {
            body().asPrettyString()
        }

        println(message)
    }

    @Test
    fun getSingleActivity() {
        val activity =
            """{
                "type": "Run",
                "distance": 10000,
                "start": 1678279436,
                "finish": 1678275836
            }"""

        val id: String = Given {
            port(8080)
            body(activity)
        } When {
            post("/activity")
        } Then {
            statusCode(201)
            body("id", not(blankOrNullString()))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        } Extract {
            body().jsonPath().getString("id")
        }

        Given {
            port(8080)
        } When {
            get("/activity/$id")
        } Then {
            statusCode(200)
            body("id", equalTo(id))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        }
    }

    @Test
    fun updateSingleActivity() {
        val activity =
            """{
                "type": "Run",
                "distance": 10000,
                "start": 1678279436,
                "finish": 1678275836
            }"""

        val id: String = Given {
            port(8080)
            body(activity)
        } When {
            post("/activity")
        } Then {
            statusCode(201)
            body("id", not(blankOrNullString()))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        } Extract {
            body().jsonPath().getString("id")
        }

        Given {
            port(8080)
        } When {
            get("/activity/$id")
        } Then {
            statusCode(200)
            body("id", equalTo(id))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        }

        val updatedActivity =
            """{
                "id": "$id",
                "type": "BicycleRide",
                "distance": 11345,
                "start": 1678279436,
                "finish": 1678275836
            }"""

        Given {
            port(8080)
            body(updatedActivity)
        } When {
            put("/activity/$id")
        } Then {
            statusCode(200)
            body("id", equalTo(id))
            body("type", equalTo("BicycleRide"))
            body("distance", equalTo(11345))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        }

        Given {
            port(8080)
        } When {
            get("/activity/$id")
        } Then {
            statusCode(200)
            body("id", equalTo(id))
            body("type", equalTo("BicycleRide"))
            body("distance", equalTo(11345))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        }
    }

    @Test
    fun deleteSingleActivity() {
        val activity =
            """{
                "type": "Run",
                "distance": 10000,
                "start": 1678279436,
                "finish": 1678275836
            }"""

        val id: String = Given {
            port(8080)
            body(activity)
        } When {
            post("/activity")
        } Then {
            statusCode(201)
            body("id", not(blankOrNullString()))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        } Extract {
            body().jsonPath().getString("id")
        }

        Given {
            port(8080)
        } When {
            get("/activity/$id")
        } Then {
            statusCode(200)
            body("id", equalTo(id))
            body("type", equalTo("Run"))
            body("distance", equalTo(10000))
            body("start", equalTo(1678279436))
            body("finish", equalTo(1678283036))
        }

        Given {
            port(8080)
        } When {
            delete("/activity/$id")
        } Then {
            statusCode(204)
            body(nullValue())
        }

        Given {
            port(8080)
        } When {
            get("/activity/$id")
        } Then {
            statusCode(404)
            body(nullValue())
        }
    }



}