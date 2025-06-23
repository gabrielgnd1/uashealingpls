package com.mnkdev.uashealing23

data class Category (
    var id: Int,
    var name: String,
) {
    override fun toString(): String {
        return name
    }
}