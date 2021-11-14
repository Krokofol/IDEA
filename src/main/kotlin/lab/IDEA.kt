package lab

import java.io.File

class IDEA {

    private val keyGenerator = KeyGenerator()

    fun code(text: String) {
        val code = mutableListOf<UShort>()
        var key = keyGenerator.generateStandardKey()
        var result: List<UShort>
        var position = 0
        do {
            var data = List(4) {
                position++
                text.getOrNull(position)?.code?.toUShort() ?: UShort.MIN_VALUE
            }
            for (round in 0..7) {
                data = codeRound(key[round], data)
                println(data)
            }
            result = finalOperations(key[8], data)
            code.addAll(data)
            key = keyGenerator.generateKey(result)
        } while (position <= text.length)

        val codeWriter = File("code").writer()
        codeWriter.write(String(code.map { Char(it) }.toCharArray()))
        codeWriter.flush()

        val keyWriter = File("key").writer()
        keyWriter.write(String(key[0].map { short -> Char(short) }.toCharArray()))
        keyWriter.write(Char(key[1][0]).toString())
        keyWriter.write(Char(key[1][1]).toString())
        keyWriter.flush()
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
}