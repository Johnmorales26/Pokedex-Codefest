package com.johndev.pokedex.common.utils

fun getImageById(id: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
}

fun formatId(number: Int): String {
    // Usar la función "String.format" para formatear el número con ceros a la izquierda
    return String.format("#%03d", number)
}

fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) {
        return this
    }
    return substring(0, 1).toUpperCase() + substring(1)
}