package lab

import java.io.BufferedReader
import java.io.File
import java.lang.RuntimeException

object IDEA {
    fun code(textReader: BufferedReader) {
        val keyWriter = File("key").writer()
        val codeWriter = File("code").writer()

        val key = KeyGenerator.generateNewKey()
        keyWriter.write(String(key[0].map { short -> Char(short) }.toCharArray()))
        keyWriter.write(Char(key[1][0]).toString())
        keyWriter.write(Char(key[1][1]).toString())
        keyWriter.flush()
        var text = CharArray(4)
        var readSymbols = textReader.read(text)
        do {
            var data = List(4) { position ->
                if(position + 1 > readSymbols) {
                    UShort.MIN_VALUE
                } else {
                    text[position].code.toUShort()
                }
            }
            for (round in 0..7) {
                data = codeRound(key[round], data)
                println(data)
            }
            data = finalOperations(key[8], data)
            codeWriter.write(String(data.map { Char(it) }.toCharArray()))
            codeWriter.flush()

            text = CharArray(4)
            readSymbols = textReader.read(text)
        } while (readSymbols > 0)
    }

    private fun codeRound(k: List<UShort>, d: List<UShort>): List<UShort> {
        val m0 = multiply(k[0].toLong(), d[0].toLong())
        val s0 = sum(k[1].toLong(), d[1].toLong())
        val s1 = sum(k[2].toLong(), d[2].toLong())
        val m1 = multiply(k[3].toLong(), d[3].toLong())
        val x0 = xor(m0, s1)
        val x1 = xor(s0, m1)
        val m2 = multiply(k[4].toLong(), x0)
        val s2 = sum(m2, x1)
        val m3 = multiply(s2, k[5].toLong())
        val s3 = sum(m2, m3)
        val r0 = xor(m0, m3)
        val r1 = xor(s1, m3)
        val r2 = xor(s0, s3)
        val r3 = xor(m1, s3)
        return listOf(r0.toUShort(), r1.toUShort(), r2.toUShort(), r3.toUShort())
    }

    private fun finalOperations(k: List<UShort>, d: List<UShort>): List<UShort> {
        val r0 = multiply(k[0].toLong(), d[0].toLong())
        val r1 = sum(k[1].toLong(), d[2].toLong())
        val r2 = sum(k[2].toLong(), d[1].toLong())
        val r3 = multiply(k[3].toLong(), d[3].toLong())
        return listOf(r0.toUShort(), r1.toUShort(), r2.toUShort(), r3.toUShort())
    }

    private fun xor(a: Long, b: Long): Long {
        return a xor b
    }

    private fun sum(a: Long, b: Long): Long {
        return (a + b) % (65536L)
    }

    private fun multiply(a: Long, b: Long): Long {
        val a1 = if (a == 0L) 65536L else a
        val b1 = if (b == 0L) 65536L else b
        val result =  (a1 * b1) % (65537L)
        return if (result == 65536L) 0 else result
    }

    fun decode(codeReader: BufferedReader, keyReader: BufferedReader) {
        val keyString = CharArray(8)
        var readSymbols = keyReader.read(keyString)
        if (readSymbols != 8) {
            throw RuntimeException("bad key file")
        }
        val inputKey = List(8) { position ->
            keyString[position].code.toShort().toUShort()
        }
        val decodingKey = KeyGenerator.generateDecodingKey(inputKey)

        val decodedText = mutableListOf<UShort>()
        var text = CharArray(4)
        readSymbols = codeReader.read(text)
        do {
            var data = List(4) { position ->
                if(position + 1 > readSymbols) {
                    UShort.MIN_VALUE
                } else {
                    text[position].code.toUShort()
                }
            }
            for (round in 0..7) {
                data = codeRound(decodingKey[round], data)
                println(data)
            }
            data = finalOperations(decodingKey[8], data)
            decodedText.addAll(data)
            text = CharArray(4)
            readSymbols = codeReader.read(text)
        } while (readSymbols > 0)

        val textWriter = File("decodedText").writer()
        textWriter.write(String(decodedText.map { short -> Char(short) }.toCharArray()))
        textWriter.flush()
    }
}