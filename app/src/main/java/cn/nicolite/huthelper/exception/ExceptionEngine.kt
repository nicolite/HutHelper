package cn.nicolite.huthelper.exception


import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException

/**
 * 错误/异常处理工具
 * Created by nicolite on 17-10-13.
 */

enum class ExceptionError(val code: Int, val msg: String) {
    UN_KNOWN_ERROR(100, "未知错误"),
    PARSE_SERVER_DATA_ERROR(101, "解析(服务器)数据错误"),
    PARSE_CLIENT_DATA_ERROR(102, "解析(客户端)数据错误"),
    CONNECT_ERROR(103, "网络连接错误"),
    CONNECT_TIME_OUT_ERROR(104, "网络连接超时"),
    SERVER_ERROR(105, "服务器错误"),
    HTTP_ERROR(106, "网络错误")
    ;
}

fun handleAPIException(throwable: Throwable): APIException {
    throwable.printStackTrace()
    val apiException: APIException
    when (throwable) {
        is HttpException -> {
            apiException = APIException(ExceptionError.HTTP_ERROR)
        }
        is retrofit2.adapter.rxjava2.HttpException -> {
            apiException = APIException(ExceptionError.HTTP_ERROR)
        }
        is JsonParseException -> {
            apiException = APIException(ExceptionError.PARSE_SERVER_DATA_ERROR)
        }
        is JSONException -> {
            apiException = APIException(ExceptionError.PARSE_SERVER_DATA_ERROR)
        }
        is ParseException -> {
            apiException = APIException(ExceptionError.PARSE_SERVER_DATA_ERROR)
        }
        is MalformedJsonException -> {
            apiException = APIException(ExceptionError.PARSE_SERVER_DATA_ERROR)
        }
        is ConnectException -> {
            apiException = APIException(ExceptionError.CONNECT_ERROR)
        }
        is SocketTimeoutException -> {
            apiException = APIException(ExceptionError.CONNECT_TIME_OUT_ERROR)
        }
        else -> {
            apiException = APIException(ExceptionError.UN_KNOWN_ERROR)
        }
    }
    return apiException
}

