package com.example.rusheta.service.remote;

import com.example.rusheta.service.model.Contacts;
import com.example.rusheta.service.model.Contacts2;
import com.example.rusheta.service.model.ImagePath;
import com.example.rusheta.service.model.Response;
import com.example.rusheta.service.model.User;

import java.util.List;

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
    @POST("/images/upload")
    Call<Response> uploadImage(@Header("Authorization") String authToken, @Part MultipartBody.Part image);

    @POST("/image/download")
    Call<ResponseBody> getImage(@Header("Authorization") String authToken, @Body ImagePath path);

    @POST("/image/delete")
    Call<ResponseBody> deleteImage(@Header("Authorization") String authToken, @Body ImagePath path);

    @POST("/contacts")
    Call<Contacts> getContacts(@Header("Authorization") String authToken, @Body Contacts contacts);

    @POST("/allcontacts")
    Call<List<Contacts2>> getAllContacts(@Header("Authorization") String authToken);
}
