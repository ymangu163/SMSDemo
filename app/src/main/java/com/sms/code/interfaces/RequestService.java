package com.sms.code.interfaces;

import com.sms.code.bean.ProjectBean;
import com.sms.code.bean.TokenBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/9
 */

public interface RequestService {


    @GET("dengl")
    Call<TokenBean> login(@Query("zhanghao") String name, @Query("mima") String pwd);

    @POST("shousxiangmu")
    @FormUrlEncoded
    Call<List<ProjectBean>> queryProject(@Field("linpai") String token, @Field("xiangmu") String name);

    @POST("getmobile")
    @FormUrlEncoded
    Call<String> getPhoneNumber(@Field("linpai") String token, @Field("itemid") int id,
                                @Field("xunihaoduan") int haoduan);




}
