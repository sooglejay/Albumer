package sooglejay.youtu.utils;

import android.content.Context;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import sooglejay.youtu.constant.NetWorkConstant;

public class RetrofitUtil {



    /**
     * 创建RestAdapter
     * @param context
     * @return restAdapter
     */
    protected static RestAdapter getRestAdapter(Context context, final String sign) {
        final RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Host", "api.youtu.qq.com");
                request.addHeader("user-agent", "youtu-java-sdk");
                request.addHeader("accept", "*/*");
                request.addHeader("Content-Type", "text/json");
                request.addHeader("Authorization",sign);
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(NetWorkConstant.API_YOUTU_END_POINT).setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).build();
        return restAdapter;
    }

}
