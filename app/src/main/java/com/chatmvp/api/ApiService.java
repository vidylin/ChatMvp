package com.chatmvp.api;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by clevo on 2015/6/10.
 */
public interface ApiService {

    @Multipart
    @POST("UploadServlet")
    Call<ResponseBody> upload(@PartMap Map<String, RequestBody> params);
//    http://192.168.4.120:8080/UploadServlet/downloadfile?filename=
    @GET("downloadfile")
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Query("filename") String fileUrl);
}
