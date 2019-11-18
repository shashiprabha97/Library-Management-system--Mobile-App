package com.example.indika.myapplication.http_handlers;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadingElementRequest {
    public static String GET(String url,String token,String readingType,String id){

        InputStream inputStream = null;
        String result = null;

        try{
            String makeUrl = url+"/"+readingType+"/"+id;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(makeUrl);

            String json = "";

            httpGet.addHeader("Content-Type","application/json");
            httpGet.addHeader("Accept","application/json");
            httpGet.addHeader("token",token);

            HttpResponse httpResponse = httpClient.execute(httpGet);
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
