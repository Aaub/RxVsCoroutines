package com.exmple.alexetnico.model

import java.io.Serializable

data class Category(
        val id : Int = 0,
        val name : String,
        val createDate : String
) : Serializable
