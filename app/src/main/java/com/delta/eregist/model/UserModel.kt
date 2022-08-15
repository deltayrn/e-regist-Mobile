package com.delta.eregist.model

data class UserModel(
        var id: String? = "",
        var fullName: String? = "",
        var email: String? = "",
        var type: String? = "",
        var photo: String? = ""
)