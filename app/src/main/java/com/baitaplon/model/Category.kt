package com.baitaplon.model

import java.io.Serializable

class Category(
    var id: String?,
    var name: String
) : Serializable {
    override fun toString(): String {
        return "Category(id=$id, name='$name')"
    }
}