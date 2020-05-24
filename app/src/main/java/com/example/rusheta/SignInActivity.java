package com.example.rusheta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    public static final String NICKNAME = "usernickname";
    EditText name;
    EditText phone;
    EditText password;
    private static final String BASE_URL = "http://10.0.2.2:3000";
//    private static final String BASE_URL = "https://rusheta.herokuapp.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    JsonApiPlaceHolder jsonApiPlaceHolder = retrofit.create(JsonApiPlaceHolder.class);

    public void SignIn(){
        Intent i  = new Intent(SignInActivity.this, MainActivity.class);
        i.putExtra(NICKNAME,name.getText().toString());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        SignInActivity.this.finish();
    }

    public void onSignIn(View view){

        name = findViewById(R.id.edittext_name);
        phone = findViewById(R.id.edittext_phone);
        password = findViewById(R.id.edittext_password);

        if(!name.getText().toString().isEmpty()&&!phone.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()) {
                createUser(phone.getText().toString(),name.getText().toString(),password.getText().toString());
        }else
            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);
//        sharedPreferences.edit().clear().commit();

        if(!sharedPreferences.getString("token","").isEmpty()){
            Intent i  = new Intent(SignInActivity.this, MainActivity.class);
            i.putExtra(NICKNAME,sharedPreferences.getString("name",""));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            SignInActivity.this.finish();
        }

        setContentView(R.layout.signin_screen);

    }

    private void createUser(String phone,String name,String password){
        User user = new User(name,phone,password);

        Call<User> call = jsonApiPlaceHolder.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.i("createUserLognosucess",""+response.code());
                    return;
                }

                SharedPreferences sharedPreferences
                        = getSharedPreferences("RushetaData",
                        MODE_PRIVATE);

                SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();

                myEdit.putString(
                        "name",
                        response.body().getName());
                myEdit.putString(
                        "phone",
                        response.body().getPhone());

                Log.i("user",response.body().getName());
                myEdit.putString("token",response.body().getTokens().get(0).getToken());
                Log.i("user",response.body().getTokens().get(0).getToken());

                myEdit.commit();

                SignIn();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("createUserLogfail",t.toString());
            }
        });
    }
}
