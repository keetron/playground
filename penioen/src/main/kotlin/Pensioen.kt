import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import java.io.File
import java.time.LocalDate

fun main() {
    var stopLeeftijd = 47
    val output: MutableMap<Int, Output> = mutableMapOf()

    repeat(20) { _ ->
        var aow = aow2024Maand * 12
        var metrics: List<Metrics> = emptyList()
        stopLeeftijd += 1

        var aandelenvermogen = aandelenRekening
        var waarde445 = restWaarde445
        var depot = beleggingsDepot445
        var b9 = restWaardeB9
        var vermogen: Double
        var leeftijd: Int
        var jaarinkomen = gewenstJaarinkomen
        var nwl445 = 999
        var b9vk = 999
        var huur = huurWinst445voor2024
        var nwl445Verkocht = false

        for (idx in 0 until 43) {
            huur += huur * (inflatie + 0.01)
            jaarinkomen += jaarinkomen * inflatie
            leeftijd = leeftijd(geboorteDatum, idx + 1)
            val geenInleg = deeltijdLeeftijd < leeftijd
            vermogen = aandelenvermogen + waarde445 + depot + b9


            if (leeftijd <= stopLeeftijd) {
                val inleg = if (geenInleg) 0.0 else inlegPerJaar
                aandelenvermogen += (aandelenvermogen * aandelenWaardeStijging) + inleg
            } else {
                aandelenvermogen += (aandelenvermogen * aandelenWaardeStijging) - jaarinkomen
                if (aandelenvermogen < 0 && waarde445 > 0.0) {
                    aandelenvermogen += waarde445 + depot
                    depot = 0.0
                    waarde445 = 0.0
                    nwl445 = leeftijd
                    huur = 0.0
                    if (!nwl445Verkocht) {
                        nwl445Verkocht = true
                        println("Stopleeftijd $stopLeeftijd, 445 verkocht op leeftijd $leeftijd")
                    }
                } else if (aandelenvermogen < 0.0 && nwl445Verkocht && b9 != 0.0) {
                    aandelenvermogen += b9
                    b9 = 0.0
                    b9vk = leeftijd
                    println("Stopleeftijd $stopLeeftijd, b9 verkocht op leeftijd $leeftijd ")
                } else if (aandelenvermogen < 0.0 && nwl445Verkocht) {
                    println("Stopleeftijd $stopLeeftijd, blut bij $leeftijd ")
                    return@repeat
                }
            }

            waarde445 += waarde445 * woningWaardeStijging
            b9 += b9 * woningWaardeStijging
            depot += depot * woningWaardeStijging
            aow += aow * inflatie

            if (leeftijd > aowLeeftijd) vermogen += aow
            metrics = metrics.plus(Metrics(
                age = leeftijd,
                aandelen = aandelenvermogen.toInt(),
                vkw445 = waarde445.toInt(),
                vkwB9 = b9.toInt(),
                huur = huur.toInt(),
                aowPerJaar = aow.toInt(),
                inkomenPerJaar = jaarinkomen.toInt()
            ))
        }


        output[stopLeeftijd] = Output(
            stopLeeftijd,
            nwl445,
            b9vk,
            metrics
        )
//        output[stopLeeftijd] = Output(
//            stopLeeftijd,
//            nwl445,
//            b9vk,
//            metrics
//        )
    }
    val pretty = Json { prettyPrint = true }
    val json = pretty.encodeToString(output)
    File("${inlegPerJaar}.json").writeText(json)
}

fun leeftijd(geboorteDatum: LocalDate, verjaring: Int): Int {
    return LocalDate.now().year - geboorteDatum.year + verjaring
}

