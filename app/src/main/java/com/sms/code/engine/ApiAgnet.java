package com.sms.code.engine;

import com.sms.code.interfaces.RequestService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/9
 */

public class ApiAgnet {
    private final static String BASE_URL = "http://www.66yzm.com/api/admin/";
    private static RequestService mRequestService;


    public static RequestService getApiService() {
        if (mRequestService == null) {
            Retrofit mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mRequestService = mRetrofit.create(RequestService.class);
        }
        return mRequestService;
    }


}
