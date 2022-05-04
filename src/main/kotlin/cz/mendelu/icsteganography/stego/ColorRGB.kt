package cz.mendelu.icsteganography.stego

data class ColorRGB(
    var red: UByte,
    var green: UByte,
    var blue: UByte
) {
    fun toColorString(): String {
        return "#%02x%02x%02x".format(red.toByte(), green.toByte(), blue.toByte())
    }

    fun toHexString(): String {
        return "%02x %02x %02x".format(red.toByte(), green.toByte(), blue.toByte()).uppercase()
    }
}
