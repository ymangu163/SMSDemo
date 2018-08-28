package com.sms.code.interfaces;

import com.sms.code.bean.TokenBean;

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


    @GET("admin/dengl")
    Call<TokenBean> login(@Query("zhanghao") String name, @Query("mima") String pwd);


}
