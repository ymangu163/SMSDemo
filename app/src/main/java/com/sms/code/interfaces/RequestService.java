package com.sms.code.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/9
 */

public interface RequestService {


    @GET("Login")
    Call<String> login(@Query("uName") String name, @Query("pWord") String pwd);


}
