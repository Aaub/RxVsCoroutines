package com.exmple.alexetnico.model

import java.io.Serializable

data class Beer(
        val id : String,
        val name : String,
        val displayName : String,
        val abv : String,
        val glasswareId : Int,
        val styleId : Int,
        val isOrganic : String,
        val isRetired : String,
        val labels : List<Label>,
        val status : String,
        val statusDisplay : String,
        val createdDate : String,
        val updatedDate : String,
        val glass : Glass,
        val style : Style
): Serializable