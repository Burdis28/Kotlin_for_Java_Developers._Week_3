package nicestring

fun String.isNice(): Boolean {
    val uglyStrings = listOf("bu", "ba", "be")
    val isVowel: (Char) -> Boolean = {it.isVowel()}

    return atleastTwo(listOf(
        this.containsDoubleLetter(),
        this.count(isVowel) >= 3,
        uglyStrings.none { uglyString -> this.contains(uglyString) }))
}

fun Char.isVowel(): Boolean {
    return listOf('a', 'e', 'i', 'o', 'u').contains(this)
}

fun atleastTwo(conditions: List<Boolean>): Boolean {
    return conditions.count { it == true} >= 2
}

fun String.containsDoubleLetter(): Boolean {
    for (i in 0 until this.length - 1) {
        if (this[i] == this[i + 1]) {
            return true
        }
    }
    return false
}