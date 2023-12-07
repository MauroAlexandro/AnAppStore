package com.mauroalexandro.anappstore.models

data class Data(
    val hidden: Int,
    val limit: Int,
    val list: List<App>,
    val next: Int,
    val offset: Int,
    val total: Int
)