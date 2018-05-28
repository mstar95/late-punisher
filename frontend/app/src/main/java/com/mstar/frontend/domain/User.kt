package com.mstar.frontend.domain

data class User(val name: String,
                val id: String) {
    override fun toString(): String {
        return name
    }
}