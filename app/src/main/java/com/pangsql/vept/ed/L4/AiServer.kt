package com.example.vept.ed.L4

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.serialization.json.Json

@Serializable
data class AiResult(
    val result: String
)

suspend fun AiServer(
    prompt: String,
    db: String
): String? = withContext(Dispatchers.IO) {
    val jsonObject = JSONObject()
    jsonObject.put("prompt", prompt)
    jsonObject.put("db", db)

    Log.e("catch", prompt)
    Log.e("catch", db)

    val jsonObjectString = jsonObject.toString()

    try {
        val url = URL("https://teaching-broadly-manatee.ngrok-free.app/AI")
        val connection = url.openConnection() as HttpURLConnection
        connection.doOutput = true
        connection.doInput = true
        connection.requestMethod = "POST"

        Log.e("catch", "1")

        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")

        val outputStreamWriter = OutputStreamWriter(connection.outputStream)
        outputStreamWriter.write(jsonObjectString)
        outputStreamWriter.flush()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
            Log.e("catch", "ai result: $inputStream")

            val sepToken = "<#s#>"
            val sepTokenIndex = inputStream.indexOf(sepToken) + sepToken.length
            val startIndex = inputStream.indexOf(sepToken, sepTokenIndex) + sepToken.length
            val endIndex = inputStream.indexOf(sepToken, startIndex)

            val result = inputStream.substring(startIndex, endIndex)
            Log.e("catch", result)
            return@withContext result
        } else {
            Log.e("xxx", "else")
            return@withContext null
        }
    } catch (e: Exception) {
        Log.e("xxx", "catch")
        e.printStackTrace()
        return@withContext "쿼리문에 관련된 정보만을 입력해주세요."
    }
}
