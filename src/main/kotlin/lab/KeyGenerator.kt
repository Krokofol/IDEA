package lab

import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random

class KeyGenerator {

    private val random = Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))

    fun generateNewKey(): List<List<UShort>> {
        val firstKeyLayer = List(8) { position ->
            (position + 1).toUShort()
//            random.nextInt().toUShort()
        }
        return generateKey(firstKeyLayer)
    }

    fun generateDecodingKey(input: List<UShort>): List<List<UShort>> {
        val codingKey = generateKey(input)
        return List(9) { layer ->
            List(codingKey[layer].size) { position ->
                when(position + 1) {
                    1, 4 -> {
                        findMultiplicativeInversion(codingKey[8 - layer][position])
                    }
                    2 -> {
                        when(layer) {
                            0, 8 -> findInversion(codingKey[8 - layer][position])
                            else -> findInversion(codingKey[8 - layer][position + 1])
                        }
                    }
                    3 -> {
                        when(layer) {
                            0, 8 -> findInversion(codingKey[8 - layer][position])
                            else -> findInversion(codingKey[8 - layer][position - 1])
                        }
                    }
                    5, 6 -> {
                        codingKey[7 - layer][position]
                    }
                    else -> {
                        throw RuntimeException("unexpected position")
                    }
                }
            }
        }
    }

    private fun generateKey(input: List<UShort>): List<List<UShort>> {
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

    private fun findMultiplicativeInversion(a: UShort): UShort {
        for (i in 0L..65537L) {
            if ((a.toLong() * i) % 65537 == 1L) return i.toUShort()
        }
        throw RuntimeException("was not found multiplicative inversion")
    }

    private fun findInversion(a: UShort): UShort {
        return (65536 - a.toInt()).toUShort()
    }
}