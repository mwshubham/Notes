package com.mwshubham.notes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform