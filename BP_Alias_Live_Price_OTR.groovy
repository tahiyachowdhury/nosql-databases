
package glance

import static glance.MonitoringJob.DaysOfWeek.*

class BPAliasLiveMidPriceOTRCheck extends MonitoringJob {

    def suite = "BP_Alias_Live_Price_OTR"

    def BPAliasLiveMidPriceOTRCheck () {
        times = [
                (SUNDAY): ["18:15", "23:59"],
                (WEEKDAY): ["00:00", "17:30", "18:15", "23:59"],
                (FRIDAY): ["00:00", "17:30"]
        ]
    }

    def aliasChainPrefix = "USD.ALIASLOOKUP.ANALYTICS."

    def entryNames = [
            "P2YR",
            "PP2YR",
            "PPP2YR",
            "P3YR",
            "PP3YR",
            "PPP3YR",
            "P5YR",
            "PP5YR",
            "PPP5YR",
            "P7YR",
            "PP7YR",
            "PPP7YR",
            "P10YR",
            "PP10YR",
            "PPP10YR",
            "P30YR",
            "PP30YR",
            "PPP30YR",
            "FV_NC",
            "US_NC",
            "AUL_NC",
            "18MO",
            "2YR",
            "3YR",
            "5YR",
            "7YR",
            "10YR",
            "30YR",
            "2S3S_1",
            "2S3S_2",
            "2S3S_3",
            "2S3S_4"
    ]

    def monitor(def statusEntries) {

        def liveMidPricePrefix = "USD.ANALYTICS_PRICE.ANALYTICS."
        logInfo("Performing Checks for suite: ${suite}")
        statusEntries.each { record, entry ->

            //Get Cusip for entry
            def cusip = "${aliasChainPrefix}${record}".record().Cusip
            def liveMidPriceRecord = "${liveMidPricePrefix}${cusip}".record()
            def liveMidPrice = liveMidPriceRecord.MidPrice

            if (liveMidPriceRecord) {
                if (liveMidPrice && !liveMidPrice.equals("NaN")) {
                    entry.setStatusAndDescription(StatusType.PASS.value, "Alias Live Mid Price Available");
                } else {
                    entry.setStatusAndDescription(StatusType.FAIL.value, "Record exists but MidPrice is NaN. Underlying Cusip: ${cusip}");
                }
            } else {
                entry.setStatusAndDescription(StatusType.FAIL.value, "Alias Live Mid Price Record Not Available. Underlying Cusip: ${cusip}");
            }
        }
    }
}


"SELECT * FROM (SELECT RecordId FROM ${aliasChainPrefix}${record}) AS records WHERE fieldname = "cusip""

            "SELECT RecordId FROM {liveMidPricePrefix}${cusip}"

            "SELECT * FROM WHERE fieldname = "Midprice"
