package com.example.indika.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indika.myapplication.http_handlers.SignupRequest;
import com.example.indika.myapplication.http_handlers.UserRegester;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private RadioGroup radioGroup;
    private EditText nic,district,passport,countryId,firstName,lastName,userName,password,passwordRe;
    private TextView passwordError;
    private CardView signUp;
    private String userType = null;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = (RadioGroup)findViewById(R.id.radio_type);
        radioGroup.setOnCheckedChangeListener(this);

        signUp = (CardView) findViewById(R.id.signup);
        signUp.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);

        formFieldRegistration();

    }

    public void formFieldRegistration(){
        nic = (EditText) findViewById(R.id.nic);
        district = (EditText) findViewById(R.id.district);
        passport = (EditText) findViewById(R.id.passport);
        countryId = (EditText) findViewById(R.id.country);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordRe = (EditText) findViewById(R.id.password_re);
        passwordError = (TextView) findViewById(R.id.password_error);
    }

    public void formFieldVisibility(boolean status){
        if(status){
            nic.setVisibility(View.VISIBLE);
            district.setVisibility(View.VISIBLE);
            passport.setVisibility(View.GONE);
            countryId.setVisibility(View.GONE);

        }else{
            nic.setVisibility(View.GONE);
            district.setVisibility(View.GONE);
            passport.setVisibility(View.VISIBLE);
            countryId.setVisibility(View.VISIBLE);
        }
    }

    public boolean formValidation(){
        boolean status = true;
        if(firstName.getText().toString().length()==0){
            firstName.setBackgroundResource(R.color.error_text);
            System.out.println("firstname");
            status =false;
        }else {
            firstName.setBackgroundResource(R.color.text_edit);
        }

        if(lastName.getText().toString().length()==0){
            lastName.setBackgroundResource(R.color.error_text);
            System.out.println("lastname");
            status =false;
        }else {
            lastName.setBackgroundResource(R.color.text_edit);
        }

        if(userName.getText().toString().length()==0){
            userName.setBackgroundResource(R.color.error_text);
            System.out.println("username");
            status =false;
        }else {
            userName.setBackgroundResource(R.color.text_edit);
        }

        if(password.getText().toString().length()==0){
            password.setBackgroundResource(R.color.error_text);
            System.out.println("password");
            status =false;
        }else {
            password.setBackgroundResource(R.color.text_edit);
        }

        if(passwordRe.getText().toString().length()==0){
            passwordRe.setBackgroundResource(R.color.error_text);
            System.out.println("password Re");
            status =false;
        }else {
            passwordRe.setBackgroundResource(R.color.text_edit);
        }

        if(userType == null){
            Toast.makeText(this,"User type not filed",Toast.LENGTH_SHORT).show();
            System.out.println("usetType");
            status =false;
        }
        if(password.getText().toString().equals(passwordRe.getText().toString())){
            passwordError.setVisibility(View.GONE);
        }else {
            passwordError.setVisibility(View.VISIBLE);
            status =false;
        }
        return status;
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        View radioButton = radioGroup.findViewById(checkedId);
        int index = radioGroup.indexOfChild(radioButton);
        formFieldVisibility(index==0?true:false);
        userType = index==0?AccessData.SRILANKA:AccessData.FOREIGN;
    }

    @Override
    public void onClick(View v) {
        if(isConnected()){
            if (formValidation()){
                new HttpAsyncTask().execute(AccessData.SERVER_PATH);
            }else{
                Toast.makeText(getApplicationContext(),"No form completed",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    public class HttpAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String lNic = nic.getText().toString();
            String lDistrict = district.getText().toString();
            String lPassport = passport.getText().toString();
            String lCountryId = countryId.getText().toString();
            String lFirstName = countryId.getText().toString();
            String lLastName = lastName.getText().toString();
            String lUserName = userName.getText().toString();
            String lPassword = password.getText().toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            UserRegester ur = new UserRegester(lUserName,lPassword,lFirstName,lLastName,date,userType,lNic,lDistrict,lCountryId,lPassport);
            String result = SignupRequest.POST(urls[0],ur);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jo = new JSONObject(s);
                String massage = jo.getString("registrationAlert");
                boolean status = jo.getBoolean("status");

                if(status){ System.out.println(s);
                    builder.setMessage(massage)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                }});

                }else {
                    builder.setMessage(massage)
                            .setCancelable(false)
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                }
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("User Registration alert");
                alert.show();

            }catch (JSONException ex){
                Log.e("Error: ",ex.getLocalizedMessage());
            }
        }
    }
}
