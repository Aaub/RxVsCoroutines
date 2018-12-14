package com.alexetnico.rxvscoroutines.model

import java.io.Serializable

data class Category(
    val id: Int,
    val name: String,
    val createDate: String
) : Serializable
