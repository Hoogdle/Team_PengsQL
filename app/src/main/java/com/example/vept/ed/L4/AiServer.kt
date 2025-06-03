package com.example.vept.ed.L4

import android.util.Log
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
): String?{

    val jsonObject = JSONObject()
    jsonObject.put("prompt", prompt)
    jsonObject.put("db", db)

    Log.e("catch",prompt)
    Log.e("catch",db)
    val jsonObjectString = jsonObject.toString()

    try {
        val url = URL("https://fluent-marmoset-immensely.ngrok-free.app/AI") // edit1
        val connection = url.openConnection() as java.net.HttpURLConnection
        connection.doOutput = true // 서버로 보내기 위해 doOutPut 옵션 활성화
        connection.doInput = true
        connection.requestMethod = "POST" // edit2 // or POST

        Log.e("catch","1")

        // 서버와 통신을 위하 코드는 아래으 url 참조
        // https://johncodeos.com/post-get-put-delete-requests-with-httpurlconnection/
        connection.setRequestProperty(
            "Content-Type",
            "application/json"
        ) // The format of the content we're sending to the server
        connection.setRequestProperty(
            "Accept",
            "application/json"
        ) // The format of response we want to get from the server

        val outputStreamWriter = OutputStreamWriter(connection.outputStream)
        outputStreamWriter.write(jsonObjectString)
        outputStreamWriter.flush()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {

            val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
            Log.e("catch",  "ai result :" + inputStream)

            // 일반적으로는 아래의 코드를 사용(parsing code)
            // 근데 이상하게 오류가 생겨 수동 파싱 코드 사용
            //val json = Json{ ignoreUnknownKeys = true }.decodeFromString<AiResult?>(inputStream) // edit3

            // 수동 파싱
            val sepToken = "<#s#>"

            // 1차 토큰 검증
            val sepTokenIndex = inputStream.indexOf(sepToken) + sepToken.length
            val startIndex = inputStream.indexOf(sepToken, sepTokenIndex) + sepToken.length
            val endIndex = inputStream.indexOf(sepToken, startIndex)

            val result = inputStream.substring(startIndex,endIndex)

            Log.e("catch", result)
            return result
        } else {
            Log.e("xxx","else")
            return  null
        }
    } catch (e: Exception) {
        Log.e("xxx","catch")
        e.printStackTrace()
        return  "쿼리문에 관련된 정보만을 입력해주세요."
    }
}