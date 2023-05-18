package com.baitaplon.model

import android.graphics.Bitmap
import java.io.Serializable

class Book(
    internal var id: String? = null,
    internal var name: String? = null,
    internal var author: String? = null,
    internal var price: Int? = null,
    internal var description: String? = null,
    internal var categories: ArrayList<Category>? = null,
    internal var bitmap : ByteArray? = null
) : Serializable {
    fun setBitmap(bitmap: ByteArray){
        this.bitmap = bitmap
    }
    override fun toString(): String {
        return "Book(id=$id, name=$name, author=$author, price=$price, description=$description, categories=$categories, bitmap = $bitmap)"
    }
}
