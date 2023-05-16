package com.baitaplon.model

import java.io.Serializable

class Book(
    internal var id: String? = null,
    internal var name: String? = null,
    internal var author: String? = null,
    internal var price: Int? = null,
    internal var description: String? = null,
    internal var categories: ArrayList<Category>? = null
) : Serializable {
    override fun toString(): String {
        return "Book(id=$id, name=$name, author=$author, price=$price, description=$description, categories=$categories)"
    }
}
