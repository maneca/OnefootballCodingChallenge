package com.onefootball.data.remote

interface NewsApi {
    fun readFile(name: String): String
}