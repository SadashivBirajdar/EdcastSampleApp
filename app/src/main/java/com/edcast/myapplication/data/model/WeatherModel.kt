package com.edcast.myapplication.data.model

data class WeatherModel(
    val forecasts: List<Forecast>
) {
    data class Forecast(
        val day: String,
        val desc_day: String,
        val fcst_datetime: String,
        val imperial: Imperial,
        val long_daypart_name: String,
        val metric: Metric,
        val sunrise: String,
        val sunset: String
    ) {
        data class Imperial(
            val max_temp: Int,
            val min_temp: Int
        )

        data class Metric(
            val max_temp: Int,
            val min_temp: Int
        )
    }
}