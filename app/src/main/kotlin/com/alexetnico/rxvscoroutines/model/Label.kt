package com.alexetnico.rxvscoroutines.model

import java.io.Serializable

data class Label(
    val icon: String,
    val medium: String,
    val large: String,
    val contentAwareIcon: String,
    val contentAwareMedium: String,
    val contentAwareLarge: String
) : Serializable