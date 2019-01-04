package com.example.notification;

import com.twitter.sdk.android.core.models.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {
//    @Headers({"authorization: Bearer <bearer>"})
    @GET("/1.1/users/show.json")
    Call<User> show(@Header("authorization") String bearer,
                    @Query("user_id") long uerID, @Query("screen_name")String screenName/*,@Query("include_entities") boolean include_entities*/);
 @GET("/1.1/lists/list.json")
    Call<User> list(@HeaderMap HashMap<String,String> headers,
                    @Query("user_id") long uerID, @Query("screen_name")String screenName,@Query("reverse") boolean reverse);

    /*@POST("/api/users")
    Call<User> createUser(@Body User user);

    @GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/
}
