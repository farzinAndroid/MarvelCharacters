package com.farzin.marvelcharacters.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.contracts.Returns

fun getHash(apiKey: String, apiSecret: String, ts: String): String {
    val hashStr = ts + apiSecret + apiKey

    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(hashStr.toByteArray()))
        .toString(16)
        .padStart(32, '0')

}


fun List<String>.convertListToString() = this.joinToString(separator = ", ")