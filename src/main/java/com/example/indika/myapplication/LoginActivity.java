package com.example.indika.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indika.myapplication.http_handlers.LoginRequest;
import com.example.indika.myapplication.http_handlers.TokenStore;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText eUsetname;
    EditText ePassword;
    CardView cLogin;
    TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eUsetname = (EditText) findViewById(R.id.username);
        ePassword = (EditText) findViewById(R.id.password);
        cLogin = (CardView) findViewById(R.id.login_button);
        errorMsg = (TextView) findViewById(R.id.error_msg);

        errorMsg.setVisibility(View.INVISIBLE);

        cLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
               /* if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();*/
                // call AsynTask [to perform network operation on separate thread
                if(isConnected()){
                    new HttpAsyncTask().execute(AccessData.SERVER_PATH+"//user//login");
                }else{
                    Toast.makeText(getApplicationContext(),"No Internet connection", Toast.LENGTH_LONG).show();
                }


                //new HttpAsyncTask().execute("http://requestb.in/11yd96i1");

                break;
        }
    }

    public boolean isConnected(){
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            return false;
        }else{
            return true;
        }
    }

    public class HttpAsyncTask extends AsyncTask<String ,Void, String>{

        @Override
        protected String doInBackground(String... url) {
            return LoginRequest.POST(url[0],eUsetname.getText().toString(),ePassword.getText().toString());

        }


        @Override
        protected void onPostExecute(String s) {
            SharedPreferences makeSharedPref = getSharedPreferences(TokenStore.SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            Log.i("Result: ",s);

            try{
                JSONObject jo = new JSONObject(s);
                if(jo.getBoolean("status")){
                    errorMsg.setVisibility(View.INVISIBLE);
                    TokenStore.setToken(jo.getString("token"),makeSharedPref);
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    getBaseContext().startActivity(intent);
                }else{
                    String errorAlert = jo.getString("alert");
                    errorMsg.setVisibility(View.VISIBLE);
                    errorMsg.setText(errorAlert);
                }

            }catch (JSONException ex){
                Log.d("ERROR: ",ex.getLocalizedMessage());
            }


        }
    }

}
