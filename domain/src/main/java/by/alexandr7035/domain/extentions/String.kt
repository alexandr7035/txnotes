package by.alexandr7035.domain.extentions

fun String.getSubString(symbolsCount: Int): String {
    return substring(0, kotlin.math.min(symbolsCount, length)).trim()
}