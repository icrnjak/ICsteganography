package cz.mendelu.icsteganography.stego

data class ColorRGB(
    var red: Int,
    var green: Int,
    var blue: Int
) {

    constructor(red: Byte, green: Byte, blue: Byte) : this(
        red.toUByte().toInt(),
        green.toUByte().toInt(),
        blue.toUByte().toInt()
    )

    fun toColorString(): String {
        return "#%02x%02x%02x".format(red.toByte(), green.toByte(), blue.toByte())
    }

    fun toHexString(): String {
        return "%02x %02x %02x".format(red.toByte(), green.toByte(), blue.toByte()).uppercase()
    }

    fun getRedHex(): String {
        return formatHex(red)
    }

    fun getGreenHex(): String {
        return formatHex(green)
    }

    fun getBlueHex(): String {
        return formatHex(blue)
    }

    private fun formatHex(color: Int): String {
        return "%02x".format(color).uppercase()
    }
}
