package com.example.virtuousvoice.Services

import java.io.IOException

import java.io.DataOutputStream

import java.io.FileInputStream

import java.io.File

import java.nio.ByteBuffer

import java.nio.ByteOrder

import java.io.FileOutputStream

import java.io.DataInputStream


@Throws(IOException::class)
fun audioConverter(rawFile: File, waveFile: File) {
    val rawData = ByteArray(rawFile.length().toInt())
    var input: DataInputStream? = null
    try {
        input = DataInputStream(FileInputStream(rawFile))
        input.read(rawData)
    } finally {
        input?.close()
    }
    var output: DataOutputStream? = null
    try {
        output = DataOutputStream(FileOutputStream(waveFile))
        // WAVE header
        // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
        writeString(output, "RIFF") // chunk id
        writeInt(output, 36 + rawData.size) // chunk size
        writeString(output, "WAVE") // format
        writeString(output, "fmt ") // subchunk 1 id
        writeInt(output, 16) // subchunk 1 size
        writeShort(output, 1.toShort()) // audio format (1 = PCM)
        writeShort(output, 1.toShort()) // number of channels
        writeInt(output, 44100) // sample rate
        writeInt(output, 44100 * 2) // byte rate
        writeShort(output, 2.toShort()) // block align
        writeShort(output, 16.toShort()) // bits per sample
        writeString(output, "data") // subchunk 2 id
        writeInt(output, rawData.size) // subchunk 2 size
        // Audio data (conversion big endian -> little endian)
        val shorts = ShortArray(rawData.size / 2)
        ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()[shorts]
        val bytes = ByteBuffer.allocate(shorts.size * 2)
        for (s in shorts) {
            bytes.putShort(s)
        }
        output.write(fullyReadFileToBytes(rawFile))
    } finally {
        output?.close()
    }
}

@Throws(IOException::class)
fun fullyReadFileToBytes(f: File): ByteArray? {
    val size = f.length().toInt()
    val bytes = ByteArray(size)
    val tmpBuff = ByteArray(size)
    val fis = FileInputStream(f)
    try {
        var read = fis.read(bytes, 0, size)
        if (read < size) {
            var remain = size - read
            while (remain > 0) {
                read = fis.read(tmpBuff, 0, remain)
                System.arraycopy(tmpBuff, 0, bytes, size - remain, read)
                remain -= read
            }
        }
    } catch (e: IOException) {
        throw e
    } finally {
        fis.close()
    }
    return bytes
}

@Throws(IOException::class)
private fun writeInt(output: DataOutputStream, value: Int) {
    output.write(value shr 0)
    output.write(value shr 8)
    output.write(value shr 16)
    output.write(value shr 24)
}

@Throws(IOException::class)
private fun writeShort(output: DataOutputStream, value: Short) {
//    output.write(value shl 0)
//    output.write(value shr 8)
}

@Throws(IOException::class)
private fun writeString(output: DataOutputStream, value: String) {
    for (i in 0 until value.length) {
        output.write(value[i].toInt())
    }
}