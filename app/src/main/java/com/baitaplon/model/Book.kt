package com.baitaplon.model

import java.io.Serializable

class Book : Serializable {
    private var id:Int? = null
    internal var name: String? = null
    private var author: String? = null
    private var price: Double? = null
    private var favorited: Boolean? = null

    constructor() {}
    constructor(name: String?, author: String?, price: Double?, isFavorited: Boolean?) {
        this.name = name
        this.author = author
        this.price = price
        favorited = isFavorited
    }

    constructor(id: Int, name: String?, author: String?, price: Double?, isFavorited: Boolean?) {
        this.id = id
        this.name = name
        this.author = author
        this.price = price
        favorited = isFavorited
    }

}
