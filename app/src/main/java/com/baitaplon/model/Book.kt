package com.baitaplon.model

import java.io.Serializable

class Book(
    private var id: Int? = null,
    internal var name: String? = null,
    private var author: String? = null,
    private var price: Double? = null,
    private var categories: MutableList<Category>? = null
) : Serializable {}
