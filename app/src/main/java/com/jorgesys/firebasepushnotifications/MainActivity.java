package com.jorgesys.firebasepushnotifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    //*
    // https://developer.android.com/training/volley
    // Server Key:	AAAALfL3dws:APA91bHTX5zJ596CWkqXhMFyTxOro3ngTKRlszFM6UBdjmTjbbBPYzAef52s0sbvnvdP_cjanAXBf1T5KqElWVT99XNWNtRbVqinAi7rw9q_gEDJJnleZZv97-hfOWYFTfY-xrl0bQpY
    //https://androidredman.wordpress.com/2017/08/27/send-push-notifications-from-1-android-phone-to-another-with-out-server/
    //https://firebase.google.com/docs/cloud-messaging/android/client

    private static final String TAG = "MainActivity";
    private static final String SERVER_AUTHORIZATION_KEY= "AAAALfL3dws:APA91bHTX5zJ596CWkqXhMFyTxOro3ngTKRlszFM6UBdjmTjbbBPYzAef52s0sbvnvdP_cjanAXBf1T5KqElWVT99XNWNtRbVqinAi7rw9q_gEDJJnleZZv97-hfOWYFTfY-xrl0bQpY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSendNotification = findViewById(R.id.btnSendNotification);
        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification(getApplicationContext());
            }
        });

    }


    private void sendNotification(Context context){
        Log.i(TAG, "sendNotification() :-)");
        RequestQueue myrequest= Volley.newRequestQueue(context);
        JSONObject json = new JSONObject();
        try {
            String url_foto="https://scontent-gua1-1.xx.fbcdn.net/v/t1.6435-9/252413731_447363656810717_3874714756525223403_n.jpg?_nc_cat=110&ccb=1-5&_nc_sid=8bfeb9&_nc_ohc=xnaYzjbprH8AX-b2HXl&_nc_ht=scontent-gua1-1.xx&oh=39529147a191bc9c14d3272548b1a9ee&oe=61ACC277";
            String BodyPayload = "{   \"to\" : \"Receiver FireBase token\", // if you want to send to all use to : \"/topics/all\"\n" +
                    "    \"priority\" : \"normal\", //optional  \n" +
                    "    \"data\" : {    \n" +
                    "                 \"title\" : \"your notification title\"\n" +
                    "                 \"body\" : \"yournotification body/message\"\n" +
                    "                 \"key1\" : \"value1\",  \n" +
                    "                 \"key2\" : \"value2\" // send any thing else needed for your app here  \n" +
                    "             } \n" +
                    "}";

            json.put("to",BodyPayload);
            //json.put("to","/topics/"+"enviaratodos");
            JSONObject notificacion=new JSONObject();
            notificacion.put("titulo", "Tu pedido ya esta listo");
            notificacion.put("detalle","¡Ya puedes recoger tu pedido en el restaurante");
            notificacion.put("foto",url_foto);

            json.put("data",notificacion);

            Log.i(TAG, "sendNotification() JSON: " +json);
            String URL="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String>header=new HashMap<>();
                    //header.put("content-type","application/json");
                    header.put("Content-Type","application/json");
                    header.put("Authorization",SERVER_AUTHORIZATION_KEY);
                    return header;
                }
            };
            myrequest.add(request);

        }catch (JSONException e){
            Log.e(TAG, "sendNotification() " + e.getMessage());
        }
    }

}