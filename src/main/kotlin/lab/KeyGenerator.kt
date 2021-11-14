package lab

class KeyGenerator {

    fun generateStandardKey(): List<List<UShort>> {
        val firstKeyLayer = List(8) { position ->
            (position + 1).toUShort()
        }
        return generateKey(firstKeyLayer)
    }

    fun generateKey(input: List<UShort>): List<List<UShort>> {
        val result = mutableListOf(input)
        for (i in 2..7) {
            result.add(generateNextKeyLayer(result.last()))
        }
        return List(9) { y ->
            val size = if (y == 8) 4 else 6
            List(size) { x ->
                result[(x + y * 6) / 8][(x + y * 6) % 8]
            }
        }
    }

    private fun generateNextKeyLayer(previousLayer: List<UShort>): List<UShort> {
        return List(previousLayer.size) { position ->
            generateUByte(position, previousLayer)
        }
    }

    private fun generateUByte(position: Int, previousLayer: List<UShort>): UShort =
        ((previousLayer[(position + 1) % previousLayer.size].toInt() shl 9) + (previousLayer[(position + 2) % previousLayer.size].toInt() shr 7)).toUShort()
}