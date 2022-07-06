package com.edcast.myapplication.data.model

data class PeopleListResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Result>? = null
) {
    data class Result(
        val birth_year: String,
        val created: String,
        val edited: String,
        val eye_color: String,
        val films: List<String>,
        val gender: String,
        val hair_color: String,
        val height: String,
        val homeworld: String,
        val mass: String,
        val name: String,
        val skin_color: String,
        val species: List<String>,
        val starships: List<String>,
        val url: String,
        val vehicles: List<String>
    )
}