package com.example.homepage;


import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAAKbUSGhk:APA91bFk_z_zctE3aR8xTIkLlJjsnMODb6IeaUS59wTDH4bbyIN2TCjZnZKiahnXsDJ7qGXGKVW-ugjFbSzj6JWhg_Y4Y6kDGVjgVLo71K5e_XaIbfaZwd0X1FnAmNyZa9IRk3Fc492B";

    public static void pushNotification(Context context, String token, String title, String message){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue= Volley.newRequestQueue(context);
        try{
            JSONObject json=new JSONObject();
            json.put("to",token);
            JSONObject notification=new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            json.put("notification",notification);


            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM"+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String > params=new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization",SERVER_KEY);
                    return params;
                }
            };

            queue.add(jsonObjectRequest);

        }   catch (JSONException e){
            e.printStackTrace();
        }
    }
}


//SEND NOTIF
//    String title=mTitle.getText().toString().trim();
//    String message=mMessage.getText().toString().trim();
//                if(!title.equals("") && !message.equals("")){
//                        FCMSend.pushNotification(
//                        MainActivity.this,
//                        "fdICascoQ2OZoTH4v8S012:APA91bFTQoi3_w1gy-6X4cMSy0IlVV_Q4_l_W8uCdoGzpBaKv7S7xugQDmkoAeK4cB5dbIn-kOKityJPUnEJgb3XN9uENwlTBs48H4FcDouUzDIk1VN9wASgjL-33yIXbCjLhQDWCrjX",
//                        title,
//                        message
//                        );
//                        }
