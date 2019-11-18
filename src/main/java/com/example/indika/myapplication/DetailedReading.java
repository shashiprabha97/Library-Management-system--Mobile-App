package com.example.indika.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indika.myapplication.http_handlers.ReadingElementRequest;
import com.example.indika.myapplication.http_handlers.TokenStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailedReading extends AppCompatActivity {
    //visibility field list
    private LinearLayout publisherVisible,priceVisible,isbnVisible,authorVisible,issuedDateVisible, ruledKingVisible,yearRangeVisible;

    //data list views
    private TextView id,title,category,year,language,state,publisher,price,isbn,issuedDate,ruledKing,yearRange;
    private ListView author;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_reading);

//        String id = getIntent().getStringExtra("ID");
//        String type = getIntent().getStringExtra("TYPE");
//
//        Log.d("Caried data: ",id+" "+type);
        visibilityFieldRegister();
        fieldRagistration();


        if (isConnected()){
            new HttpAsyncTask().execute(AccessData.SERVER_PATH);
        }else {
            Toast.makeText(getApplicationContext(),"No internet connectivity",Toast.LENGTH_LONG);
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
    private void visibilityFieldRegister(){
        publisherVisible = (LinearLayout) findViewById(R.id.publisher_visible);
        priceVisible = (LinearLayout) findViewById(R.id.price_visible);
        isbnVisible = (LinearLayout) findViewById(R.id.isbn_visible);
        authorVisible = (LinearLayout) findViewById(R.id.author_visible);
        issuedDateVisible = (LinearLayout) findViewById(R.id.issued_date_visible);
        ruledKingVisible = (LinearLayout) findViewById(R.id.ruled_king_visible);
        yearRangeVisible = (LinearLayout) findViewById(R.id.year_range_visible);
    }
    private void fieldRagistration(){
        id = (TextView) findViewById(R.id.readings_id);
        title = (TextView) findViewById(R.id.title);
        category = (TextView) findViewById(R.id.category);
        year = (TextView) findViewById(R.id.year);
        language = (TextView) findViewById(R.id.language);
        state = (TextView) findViewById(R.id.state);
        publisher = (TextView) findViewById(R.id.publisher);
        price = (TextView) findViewById(R.id.price);
        isbn = (TextView) findViewById(R.id.isbn);
        issuedDate = (TextView) findViewById(R.id.issued_date);
        ruledKing = (TextView) findViewById(R.id.ruled_king);
        year = (TextView) findViewById(R.id.year);
        yearRange = (TextView) findViewById(R.id.year_range);
        author = (ListView) findViewById(R.id.author);
    }
    public void commonDataFieldVisibility(String id, String title,String category,int year,String language,String state){
        this.id.setText(id);
        this.title.setText(title);
        this.category.setText(category);
        this.year.setText(String.valueOf(year));
        this.language.setText(language);
        this.state.setText(state);

    }
    private void bookFieldVisible(String publisher,double price,String isbn,List<String> authors){
        this.publisherVisible.setVisibility(View.VISIBLE);
        this.priceVisible.setVisibility(View.VISIBLE);
        this.isbnVisible.setVisibility(View.VISIBLE);
        this.authorVisible.setVisibility(View.VISIBLE);

        this.publisher.setText(publisher);
        this.price.setText(String.valueOf(price));
        this.isbn.setText(isbn);

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.author_element, authors);
        this.author.setAdapter(adapter);

    }
    private void magazineFieldVisible(String issuedDate,double price){
        this.issuedDateVisible.setVisibility(View.VISIBLE);
        this.priceVisible.setVisibility(View.VISIBLE);

        this.price.setText(String.valueOf(price));
        this.issuedDate.setText(issuedDate);
    }
    private void newspaperFieldVisibiliy(double price, String issuedDate,String publisher){
        this.priceVisible.setVisibility(View.VISIBLE);
        this.issuedDateVisible.setVisibility(View.VISIBLE);
        this.publisherVisible.setVisibility(View.VISIBLE);

        this.price.setText(String.valueOf(price));
        this.issuedDate.setText(issuedDate);
        this.publisher.setText(publisher);
    }
    private void olaleafFieldVisibility(String ruledKing,String yearRange){
        this.ruledKingVisible.setVisibility(View.VISIBLE);
        this.yearRangeVisible.setVisibility(View.VISIBLE);

        this.ruledKing.setText(ruledKing);
        this.yearRange.setText(yearRange);
    }

    public class HttpAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... url) {
            SharedPreferences makeSharedPref = getSharedPreferences(TokenStore.SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            String token = TokenStore.getToken(makeSharedPref);
            String id = getIntent().getStringExtra("ID");
            String readingType = getIntent().getStringExtra("TYPE");

            String readingReasult = ReadingElementRequest.GET(url[0],token,readingType,id);
            return readingReasult;
        }

        @Override
        protected void onPostExecute(String s) {
            String readingType = getIntent().getStringExtra("TYPE");
            try{
                JSONObject jo = new JSONObject(s);

                String id = jo.getString("id");
                String title = jo.getString("title");
                String category = jo.getString("category");
                int year = jo.getInt("year");
                String language = jo.getString("language");
                int state =  jo.getInt("state");
                String stateD = (state==0)?AccessData.PUBLIC:AccessData.RARE;

                System.out.println(id+" "+title);
                commonDataFieldVisibility(id,title,category,year,language,stateD);

                switch (readingType){
                    case AccessData.BOOK:{
                        String publisher = jo.getString("publisher");
                        double price = jo.getDouble("price");
                        String isbn = jo.getString("isbn");
                        JSONArray ja = jo.getJSONArray("authors");
                        List<String> authors = new ArrayList<>();

                        for(int i =0;i<ja.length();i++){
                            JSONObject author = ja.getJSONObject(i);
                            authors.add(author.getString("name"));
                        }

                        bookFieldVisible(publisher,price,isbn,authors);
                        break;
                    }
                    case AccessData.MAGAZINE:{
                        double price = jo.getDouble("price");
                        String issuedDate = jo.getString("issuedDate");

                        magazineFieldVisible(issuedDate,price);
                        break;
                    }
                    case AccessData.NAWSPAPER:{
                        double price = jo.getDouble("price");
                        String issuedDate = jo.getString("issuedDate");
                        String publisher = jo.getString("publisher");

                        newspaperFieldVisibiliy(price,issuedDate,publisher);
                        break;
                    }
                    case AccessData.OLALEAF:{
                        String ruledKing = jo.getString("ruledKing");
                        String yearRange = jo.getString("yearRange");

                        olaleafFieldVisibility(ruledKing,yearRange);
                        break;
                    }
                }
            }catch (JSONException ex){
                Log.d("ERROR: ",ex.getLocalizedMessage());
            }

        }
    }
}
