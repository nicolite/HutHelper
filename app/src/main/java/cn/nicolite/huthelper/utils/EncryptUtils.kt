package cn.nicolite.huthelper.utils

import cn.nicolite.huthelper.BuildConfig
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by nicolite on 2018/7/9.
 * email nicolite@nicolite.cn
 */
object EncryptUtils {

    fun encryptString(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val date = simpleDateFormat.format(Date())
        return sha1(BuildConfig.APPLICATION_ID) + md5(date) + System.currentTimeMillis()
    }


    fun sha1(env: String): String {
        var sha1String = ""
        try {
            val digest = MessageDigest.getInstance("SHA-1")
            digest.update(env.toByteArray())
            val messageDigest = digest.digest()
            sha1String = toHexString(messageDigest)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return sha1String
    }

    fun md5(env: String): String {
        var md5String = ""
        try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(env.toByteArray())
            val messageDigest = digest.digest()
            md5String = toHexString(messageDigest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return md5String
    }

    private fun toHexString(messageDigest: ByteArray): String {
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            val md5Hex = Integer.toHexString((aMessageDigest.toInt() and 0xFF))
            if (md5Hex.length < 2) {
                hexString.append(0)
            }
            hexString.append(md5Hex)
        }
        return hexString.toString().toLowerCase()
    }
}