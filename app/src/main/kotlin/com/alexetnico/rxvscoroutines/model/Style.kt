package com.alexetnico.rxvscoroutines.model

import java.io.Serializable

data class Style(
    val id: Int,
    val categoryId: Int,
    val category: Category,
    val name: String,
    val shortName: String,
    val description: String,
    val ibuMin: String,
    val ibuMax: String,
    val abvMin: String,
    val abvMax: String,
    val srmMin: String,
    val srmMax: String,
    val ogMin: String,
    val fgMin: String,
    val fgMax: String,
    val createDate: String,
    val updateDate: String
) : Serializable


