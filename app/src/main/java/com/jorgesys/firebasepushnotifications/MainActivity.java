package com.jorgesys.firebasepushnotifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SERVER_AUTHORIZATION_KEY= "AAAALfL3dws:APA91bHTX5zJ596CWkqXhMFyTxOro3ngTKRlszFM6UBdjmTjbbBPYzAef52s0sbvnvdP_cjanAXBf1T5KqElWVT99XNWNtRbVqinAi7rw9q_gEDJJnleZZv97-hfOWYFTfY-xrl0bQpY";
    private static final String TOPIC_NAME = "BreakingNews";
    private Button btnSendNotification;
    private EditText editText;
    private Bitmap bitmap;

    private void getUserToken() {

        //Subscrite to topic "BreakingNews"!
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NAME);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }else{
                String token = task.getResult();
                String msg = "The user token is : " + token;
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUserToken();

        btnSendNotification = findViewById(R.id.btnSendNotification);
        editText = findViewById(R.id.myEditText);
        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification(getApplicationContext());
            }
        });

    }

    private void convertImagetoBase64(){
        /*http://jaredwinick.github.io/base64-image-viewer/
        * */
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taqueria);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteFormat = stream.toByteArray();
                // Get the Base64 string
                String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                return imgString;
            }

            @Override
            protected void onPostExecute(String s) {
                editText.setText(s);
                Log.i(TAG, ":: " +s);
            }
        }.execute();
    }

    private void sendNotification(Context context){
        Log.i(TAG, "sendNotification() :-)");
        RequestQueue myrequest= Volley.newRequestQueue(context);
        JSONObject json = new JSONObject();
        try {
            String urlImage="https://scontent-gua1-1.xx.fbcdn.net/v/t1.6435-9/252413731_447363656810717_3874714756525223403_n.jpg?_nc_cat=110&ccb=1-5&_nc_sid=8bfeb9&_nc_ohc=xnaYzjbprH8AX-b2HXl&_nc_ht=scontent-gua1-1.xx&oh=39529147a191bc9c14d3272548b1a9ee&oe=61ACC277";
            json.put("to","/topics/"+ TOPIC_NAME);
            JSONObject notificacion=new JSONObject();
            notificacion.put("title", "Your taco order is ready");
            notificacion.put("detail","¡You can now pick up your order at the restaurant!");
            notificacion.put("foto",urlImage);

            json.put("data",notificacion);
            String URL="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String>header=new HashMap<>();
                    header.put("Content-Type","application/json");
                    header.put("Authorization","key=" +SERVER_AUTHORIZATION_KEY);
                    return header;
                }
            };
            myrequest.add(request);


        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}