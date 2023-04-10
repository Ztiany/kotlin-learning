package me.ztiany.kapt.main

import me.ztiany.kapt.annotations.Code

class User {

    @Code(author = "Ztiany", date = "2023-04-07")
    fun name(): String {
        return "Ztiany"
    }

}