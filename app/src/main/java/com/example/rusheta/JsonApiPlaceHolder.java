package com.example.rusheta;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface JsonApiPlaceHolder {

    @POST("users")
    Call<User> createUser(@Body User user);

    @Multipart
    @POST ("/images/upload")
    Call<Response>
    uploadImage( @Part MultipartBody.Part image);

    @POST("/image/download")
    Call<ResponseBody> getImage(@Body ImagePath path);

    @POST("/contacts")
    Call<Contacts> getContacts(@Body Contacts contacts);
}
