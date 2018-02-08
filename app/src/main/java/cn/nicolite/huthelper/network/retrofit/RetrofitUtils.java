package cn.nicolite.huthelper.network.retrofit;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.utils.LogUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Reftrofit工具类 单例
 * Created by nicolite on 17-10-17.
 */

public class RetrofitUtils {

    private static final String TAG = "RetrofitUtils";

    public static String BASE_URL = Constants.BASE_URL;
    public static final int CONNECT_TIME_OUT = 10;//连接超时时长x秒
    public static final int READ_TIME_OUT = 10;//读数据超时时长x秒
    public static final int WRITE_TIME_OUT = 10;//写数据接超时时长x秒
    private volatile static RetrofitUtils instance;

    private RetrofitUtils() {
    }

    public static RetrofitUtils getInstance() {
        if (instance == null){
            synchronized (RetrofitUtils.class){
                if (instance == null){
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 配置OkHttp
     * @return OkHttpClient
     */
    private static OkHttpClient okHttpClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                LogUtils.d(TAG ,"okHttp：" + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }

    /**
     * 获取Retrofit
     * @return Retrofit
     */
    public Retrofit retrofit() {
      // if (BuildConfig.LOG_DEBUG){
      //     BASE_URL = Constants.TEST_BASE_URL;
      // }

        return new Retrofit.Builder()
                .client(okHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
