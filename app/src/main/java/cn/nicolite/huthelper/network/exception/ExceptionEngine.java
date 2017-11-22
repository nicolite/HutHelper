package cn.nicolite.huthelper.network.exception;


import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * 错误/异常处理工具
 * Created by nicolite on 17-10-13.
 */

public class ExceptionEngine {
    public static final int UN_KNOWN_ERROR = 1000;//未知错误
    public static final int PARSE_SERVER_DATA_ERROR = 1001;//解析(服务器)数据错误
    public static final int PARSE_CLIENT_DATA_ERROR = 1002;//解析(客户端)数据错误
    public static final int CONNECT_ERROR = 1003;//网络连接错误
    public static final int CONNECT_TIME_OUT_ERROR = 1004;//网络连接超时

    public static APIException handleException(Throwable throwable){
        throwable.printStackTrace();

        APIException apiException;
        if (throwable instanceof HttpException){
            HttpException httpException = (HttpException) throwable;
            apiException = new APIException(throwable, httpException.code());
            apiException.setMsg("网络错误");
            return apiException;
        }else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException
                || throwable instanceof MalformedJsonException){
            apiException = new APIException(throwable, PARSE_SERVER_DATA_ERROR);
            apiException.setMsg("解析数据错误");
            return apiException;
        }else if (throwable instanceof ConnectException){
            apiException = new APIException(throwable, CONNECT_ERROR);
            apiException.setMsg("网络连接失败");
            return apiException;
        }else if (throwable instanceof SocketTimeoutException){
            apiException = new APIException(throwable, CONNECT_TIME_OUT_ERROR);
            apiException.setMsg("网络连接超时");
            return apiException;
        }else if (throwable instanceof ServerException){
            ServerException serverException = (ServerException) throwable;
            apiException = new APIException(serverException, serverException.getCode());
            apiException.setMsg(serverException.getMsg());
            return apiException;
        } else {
            apiException = new APIException(throwable, UN_KNOWN_ERROR);
            apiException.setMsg("未知错误");
            return apiException;
        }
    }
}
