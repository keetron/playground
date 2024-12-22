import kotlinx.serialization.Serializable

@Serializable
data class Output(
    val stopLeeftijd: Int,
    val waardeNwl445: Int,
    val waardeB9: Int,
    var metrics: List<Metrics>
)

@Serializable
data class Metrics(
    val age: Int,
    val aandelen: Int,
    val vkw445: Int,
    val vkwB9: Int,
    val huur: Int,
    val aowPerJaar: Int,
    val inkomenPerJaar: Int,
)