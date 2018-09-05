package com.sms.code.interfaces;

import com.sms.code.bean.ProjectBean;
import com.sms.code.bean.TokenBean;
import com.sms.code.bean.UserInfo;

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


    @GET("api/admin/dengl")
    Call<TokenBean> login(@Query("zhanghao") String name, @Query("mima") String pwd);

    @POST("api/admin/shousxiangmu")
    @FormUrlEncoded
    Call<List<ProjectBean>> queryProject(@Field("linpai") String token, @Field("xiangmu") String name);

    @POST("api/admin/getmobile")
    @FormUrlEncoded
    Call<String> getPhoneNumber(@Field("linpai") String token, @Field("itemid") int id,
                                @Field("xunihaoduan") int haoduan);


    @GET("index/login/index.html")
    Call<String> index();

    @POST("index/login/index.html")
    @FormUrlEncoded
    Call<String> loginIndex(@Field("username") String username, @Field("password") String pwd,
                            @Field("code") String code, @Field("__token__") String token);

    @GET("index/usertable/chongz.html")
    Call<String> chongz();


    @POST("api/admin/shortmessage")
    @FormUrlEncoded
    Call<String> queryMsg(@Field("linpai") String token, @Field("itemid") int id,
                                @Field("mobile") String mobile, @Field("zuoz") int zuoz);


    @POST("api/admin/releaseadd")
    @FormUrlEncoded
    Call<String> releasePhone(@Field("linpai") String token);

    @POST("api/admin/blacklist")
    @FormUrlEncoded
    Call<String> blacklist(@Field("linpai") String token, @Field("itemid") int id,
                          @Field("mobile") String mobile);


    @POST("api/admin/usercenter")
    @FormUrlEncoded
    Call<UserInfo> userInfo(@Field("linpai") String token);


}
