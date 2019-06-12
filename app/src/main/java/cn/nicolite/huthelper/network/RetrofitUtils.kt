package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.BuildConfig
import cn.nicolite.huthelper.model.ConstantsValue
import cn.nicolite.huthelper.utils.EncryptUtils
import cn.nicolite.huthelper.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

/**
 * Reftrofit工具类 单例
 * Created by nicolite on 17-10-17.
 */

private const val CONNECT_TIME_OUT = 10//连接超时时长x秒
private const val READ_TIME_OUT = 10//读数据超时时长x秒
private const val WRITE_TIME_OUT = 10//写数据接超时时长x秒

val okHttpClient: OkHttpClient
    get() {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> LogUtils.d("okHttpClient", "okHttp：$message") })
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val xHeader = Interceptor { chain ->
            val token = URLEncoder.encode(EncryptUtils.encryptString(), "UTF-8")
            LogUtils.d("okhttp", "token: $token")
            chain.proceed(
                    chain.request().newBuilder()
                            .addHeader("token", token)
                            .addHeader("package_name", BuildConfig.APPLICATION_ID)
                            .addHeader("channel", BuildConfig.FLAVOR)
                            .addHeader("version", "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})")
                            .build()
            )
        }

        return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addNetworkInterceptor(logging)
                .addInterceptor(xHeader)
                .build()
    }

private val BASE_URL = if (BuildConfig.DEBUG) ConstantsValue.BASE_API_TEST_URL else ConstantsValue.BASE_API_URL
val retrofit: Retrofit
    get() {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

fun getRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}

fun getRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}
