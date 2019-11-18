package com.example.indika.myapplication.http_handlers;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SignupRequest {
    public static String POST(String url, UserRegester userRegester){
        InputStream inputStream = null;
        String result = null;
        String makeUrl = url+"/user/register";

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(makeUrl);

            String json = "";

            JSONObject jo = new JSONObject();
            jo.accumulate("username",userRegester.getUsername());
            jo.accumulate("password",userRegester.getPassword());
            jo.accumulate("firstName",userRegester.getFirstName());
            jo.accumulate("lastName",userRegester.getLastName());
            jo.accumulate("regDate",userRegester.getRegDate());
            jo.accumulate("userType",userRegester.getUserType());
            jo.accumulate("idNumber",userRegester.getIdNumber());
            jo.accumulate("district",userRegester.getDistrict());
            jo.accumulate("countryCode",userRegester.getCountryCode());
            jo.accumulate("passportId",userRegester.getPassportId());

            json = jo.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.addHeader("Content-Type","application/json");
            httpPost.addHeader("Accept","application/json");

            HttpResponse httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();

            if(inputStream!=null){
                result = convertInputStreamToString(inputStream);
            }else {
                result = "Did not work!";
            }
        } catch (Exception ex){
            Log.d("InputStream", ex.getLocalizedMessage());
        }
        return result;

    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
