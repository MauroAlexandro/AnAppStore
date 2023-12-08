package com.mauroalexandro.anappstore.models

data class AppList(
    val responses: Responses?,
    val status: String?,
    val errors: List<Error>?,
    val info: Info?
)