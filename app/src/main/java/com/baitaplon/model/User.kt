package com.baitaplon.model

class User : java.io.Serializable{
    private var uid : String? = null
    private var role: String? = null

    fun getUID(): String? {
        return uid
    }

    fun setUID(name: String?) {
        this.uid = uid
    }

    fun getRole(): String? {
        return role
    }

    fun setRole(email: String?) {
        this.role = email
    }

    override fun toString(): String {
        return "User(uid=$uid, role=$role)"
    }

}