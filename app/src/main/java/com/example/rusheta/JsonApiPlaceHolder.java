package com.example.rusheta;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface JsonApiPlaceHolder {

    @POST("users")
    Call<User> createUser(@Body User user);

    @Multipart
    @POST ("/images/upload")
    Call<Response>
    uploadImage(@Header("Authorization") String authToken, @Part MultipartBody.Part image);

    @POST("/image/download")
    Call<ResponseBody> getImage(@Header("Authorization") String authToken,@Body ImagePath path);

    @POST("/contacts")
    Call<Contacts> getContacts(@Header("Authorization") String authToken, @Body Contacts contacts);
}
