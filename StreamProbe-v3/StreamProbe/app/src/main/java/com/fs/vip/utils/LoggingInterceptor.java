package com.fs.vip.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fs.vip.domain.ETHWalletDao;
import com.streamr.client.StreamrClient;
import com.streamr.client.authentication.EthereumAuthenticationMethod;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "TokenInterceptor";
    private final int RETRY_MAX_COUNT = 3;

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        int retryCount = 0;
        Request request = chain.request();
        Response response = chain.proceed(request);
        //根据和服务端的约定判断token过期
        if (isTokenExpired(response)) {
            //同步请求方式，获取最新的Token
            String type;
            synchronized ("abc") {
                type = getNewToken();
            }
            //使用新的Token，创建新的请求
            if (!TextUtils.isEmpty(type)) {
                response.close();
                Request newRequest = chain.request()
                        .newBuilder()
                        .header("Authorization", "Bearer " + type)
                        .build();
                return chain.proceed(newRequest);
            }
        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        if (401 == response.code()) {
            return true;
        }
        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    private String getNewToken() throws IOException {
        try {
            String privateKey = ETHWalletUtils.derivePrivateKey(WalletDaoUtils.getCurrent(), WalletDaoUtils.getCurrent().getPassword());
            StreamrClient client = new StreamrClient(new EthereumAuthenticationMethod(privateKey));
            String token = client.getSessionToken();
            SharedPreferencesUtil.getInstance().putString("token", token);
            return token;

        } catch (Exception e) {
            return "";
        }
    }


}
