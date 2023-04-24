package com.baitaplon.model

import java.io.Serializable

class Category : Serializable {
    var id = 0
    var name: String? = null

    constructor() {}
    constructor(id: Int, name: String?) {
        this.id = id
        this.name = name
    }

    constructor(name: String?) {
        this.name = name
    }
}