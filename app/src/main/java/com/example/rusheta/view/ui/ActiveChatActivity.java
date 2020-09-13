package com.example.rusheta.view.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rusheta.service.model.ImagePath;
import com.example.rusheta.service.remote.JsonApiPlaceHolder;
import com.example.rusheta.R;
import com.example.rusheta.service.model.Response;
import com.example.rusheta.service.model.UserImage;
import com.example.rusheta.service.model.UserMessage;
import com.example.rusheta.service.model.UserText;
import com.example.rusheta.service.model.Chat;
import com.example.rusheta.utils.ObjectSerializationClass;
import com.example.rusheta.utils.CryptoClass;
import com.example.rusheta.utils.signal.SignalProtocolKeyGen;
import com.example.rusheta.view.adapter.MyListAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActiveChatActivity extends AppCompatActivity {

    private static final int VIEW_TYPE_TEXT_SENT = 1;
    private static final int VIEW_TYPE_TEXT_RECEIVED = 2;
    private static final int VIEW_TYPE_IMAGE_SENT = 3;
    private static final int VIEW_TYPE_IMAGE_RECEIVED = 4;
        private static final String BASE_URL = "http://10.0.2.2:3000";
//    private static final String BASE_URL = "http://localhost:3000";
//    private static final String BASE_URL = "https://rusheta.herokuapp.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    JsonApiPlaceHolder jsonApiPlaceHolder = retrofit.create(JsonApiPlaceHolder.class);

    RecyclerView recyclerView;
    MyListAdapter myListAdapter;
    ArrayList<UserMessage> messageList = new ArrayList<>();
    private Socket socket;
    private String Username;
    private Chat chat;
    private String myPhone;
    private String AESKey;
    SignalProtocolKeyGen signalProtocolKeyGen;
    SharedPreferences sharedPreferences;
    CryptoClass cryptoClass;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.share_image) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getPhoto();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void displayMessage(String message, String username) {
        Date now = new Date();
        UserMessage newMsg = new UserText(VIEW_TYPE_TEXT_RECEIVED, message, now, username);
        messageList.add(newMsg);
        myListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(myListAdapter);
    }

    public void displayImage(String path, String username) {

        ImagePath imagePath = new ImagePath();

        imagePath.setPath(path);
        SharedPreferences sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);

        String token = sharedPreferences.getString("token", "");

        Call<ResponseBody> call = jsonApiPlaceHolder.getImage(token, imagePath);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        Log.d("displayImage", "server contacted and has file");

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                        Date now = new Date();
                        String fileName = formatter.format(now) + ".jpeg";
                        byte[] resEncrypted = Base64.decode(response.body().string(), Base64.DEFAULT);
                        byte[] res = cryptoClass.decrypt(resEncrypted);
                        boolean writtenToDisk = writeResponseBodyToDisk(res, fileName);

                        if (writtenToDisk) {
                            File imgFile = new File(getExternalFilesDir(null) + File.separator + fileName);
                            if (imgFile.exists()) {
                                Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                UserMessage newmsg = new UserImage(VIEW_TYPE_IMAGE_RECEIVED, image, now, Username);
                                messageList.add(newmsg);
                                myListAdapter.notifyDataSetChanged();

                                Call<ResponseBody> callDelete = jsonApiPlaceHolder.deleteImage(token, imagePath);
                                callDelete.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                        Log.d("deleteImage", "file delete was a success? " + response.isSuccessful());
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        t.printStackTrace();

                                    }
                                });
                            }

                        }
                        Log.d("displayImage", "file download was a success? " + writtenToDisk);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("displayImage", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private boolean writeResponseBodyToDisk(byte[] body, String fileName) {
        try {

            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + fileName);

            ByteArrayInputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.length;
                long fileSizeDownloaded = 0;

                inputStream = new ByteArrayInputStream(body);
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("writeResposnebody", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public void sendText(View view) {

        EditText editText = findViewById(R.id.edittext_username);

        if (!editText.getText().toString().isEmpty()) {
            String msg = editText.getText().toString();
            String encryptedMsg = Base64.encodeToString(cryptoClass.encrypt(msg.getBytes()), Base64.DEFAULT);
            mSocket.emit("messageDetection", Username, chat.getPhone(), encryptedMsg);

            Date now = new Date();
            UserMessage newMsg = new UserText(VIEW_TYPE_TEXT_SENT, msg, now, Username);
            messageList.add(newMsg);
            myListAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(myListAdapter);
            editText.setText("");
        }
    }

    public void sendImage(Bitmap image, byte[] selectedImage) {

        String encryptedImage = Base64.encodeToString(cryptoClass.encrypt(selectedImage), Base64.DEFAULT);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), encryptedImage);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        SharedPreferences sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);

        String token = sharedPreferences.getString("token", "");


        Call<Response> call = jsonApiPlaceHolder.uploadImage(token, body);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if (response.isSuccessful()) {

                    Date now = new Date();
                    UserMessage newmsg = new UserImage(VIEW_TYPE_IMAGE_SENT, image, now, Username);
                    messageList.add(newmsg);
                    myListAdapter.notifyDataSetChanged();
                    mSocket.emit("imageDetection", Username, chat.getPhone(), response.body().getPath());

                } else {

                    ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                    try {

                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                        Log.i("SendImage", "Error Response");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.i("Fail", "onFailure: " + t.getLocalizedMessage());
            }
        });


    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String encryptedMessage = data.getString("message");
                        String message = new String(cryptoClass.decrypt(Base64.decode(encryptedMessage.getBytes(), Base64.DEFAULT)));
                        String username = data.getString("senderNickname");
                        if (!username.equals(Username))
                            displayMessage(message, username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };


    private Emitter.Listener onNewImage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                String username;
                String path;
                try {
                    path = data.getString("imagePath");
                    username = data.getString("senderNickname");
                } catch (JSONException e) {
                    return;
                }

                if (!username.equals(Username))
                    displayImage(path, username);
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat = (Chat) getIntent().getExtras().getSerializable("Chat");
        Username = chat.getName();
        String sendPhone = chat.getPhone();
        AESKey = chat.getAESKey();
        setTitle(chat.getName());
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        String key = new String(AESKey.getBytes(), 0, 32, Charset.defaultCharset());
        String IV = new String(AESKey.getBytes(), 16, 16, Charset.defaultCharset());
        cryptoClass = new CryptoClass(key.getBytes(), IV.getBytes());
        sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);
        myPhone = sharedPreferences.getString("phone", "");
        signalProtocolKeyGen = new SignalProtocolKeyGen(sharedPreferences);
        recyclerView = findViewById(R.id.chatRecyclerView);
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        myLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(myLinearLayoutManager);

        myListAdapter = new MyListAdapter(this, messageList, Username);
        recyclerView.setAdapter(myListAdapter);
        mSocket.connect();
        mSocket.emit("join", myPhone);
        mSocket.on("receiveSecret", onReceiveSecret);
        try {
            String identityKeyString = ObjectSerializationClass.getStringFromObject(
                    signalProtocolKeyGen.getIdentityKeyPair().getKp().getPublic()
            );
            String ephemeralKeyString = ObjectSerializationClass.getStringFromObject(
                    signalProtocolKeyGen.getEphemeralKeyPair().getKp().getPublic()
            );
            mSocket.emit("sendSecret", sendPhone, identityKeyString, ephemeralKeyString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocket.on("message", onNewMessage);
        mSocket.on("image", onNewImage);


    }

    private Emitter.Listener onReceiveSecret = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.i("OnReceiveSecret", data.toString());
                    String identityKey;
                    String ephemeralKey;
                    try {
                        identityKey = data.getString("identityKey");
                        ephemeralKey = data.getString("ephemeralKey");
                        PublicKey identityKeyPublicKey = (PublicKey) ObjectSerializationClass.getObjectFromString(identityKey);
                        PublicKey ephemeralKeyPublicKey = (PublicKey) ObjectSerializationClass.getObjectFromString(ephemeralKey);
                        byte[] AESKeyReceived = signalProtocolKeyGen.genAESKey(identityKeyPublicKey, ephemeralKeyPublicKey);
                        AESKey = new String(Base64.encode(AESKeyReceived, Base64.DEFAULT));
                        String key = new String(AESKey.getBytes(), 0, 32, Charset.defaultCharset());
                        String IV = new String(AESKey.getBytes(), 16, 16, Charset.defaultCharset());
                        cryptoClass = new CryptoClass(key.getBytes(), IV.getBytes());
                    } catch (JSONException | IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                InputStream is = getContentResolver().openInputStream(selectedImage);
                sendImage(bitmap, getBytes(is));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }
}