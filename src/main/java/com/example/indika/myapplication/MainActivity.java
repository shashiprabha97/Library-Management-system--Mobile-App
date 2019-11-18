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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indika.myapplication.http_handlers.ReadingsSeachList;
import com.example.indika.myapplication.http_handlers.TokenStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    private RecyclerView mRecycleView;
    private ReaderListAdapter mAdapter;
    private RecyclerView.LayoutManager mLoyoutManager;
    private Spinner readingType;
    private Spinner searchType;
    private EditText searchBar;
    private CardView searchButton;
    private TextView errorMassege;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(TokenStore.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        //add recycler view
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);
        mLoyoutManager = new LinearLayoutManager(this);


        mRecycleView.setLayoutManager(mLoyoutManager);
        mRecycleView.setAdapter(mAdapter);

        this.searchButton = (CardView) findViewById(R.id.search_button);
        this.searchButton.setOnClickListener(this);
        //add spinners
        onReadType();
        this.readingType.setOnItemSelectedListener(this);

        onSearchType();
        this.searchType.setOnItemSelectedListener(this);

        //seaech edit text bar
        this.searchBar = (EditText) findViewById(R.id.search_bar);
        this.searchBar.setSelected(false);
        //error
        this.errorMassege = (TextView) findViewById(R.id.error_msg);
        this.errorMassege.setVisibility(View.INVISIBLE);

        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(isConnected()){
            new HttpAsyncTask().execute(AccessData.SERVER_PATH);
        }else {
            Toast.makeText(this,"Alert: No internet connection",Toast.LENGTH_LONG).show();
        }
    }

    public void onReadType(){
        this.readingType = (Spinner) findViewById(R.id.sReading_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.readings_type, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        this.readingType.setAdapter(adapter);
    }

    public void onSearchType(){
        this.searchType = (Spinner) findViewById(R.id.sSearch_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.seatch_type, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        this.searchType.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.munu_resouce,menu);

        if(TokenStore.getToken(sharedPreferences)!=null){
            menu.findItem(R.id.signup).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(true);
        }else {
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.signup).setVisible(true);
            menu.findItem(R.id.login).setVisible(true);
        }
         return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        String token = TokenStore.getToken(sharedPreferences);
        MenuItem singup = menu.findItem(R.id.signup);
        MenuItem login = menu.findItem(R.id.login);

        MenuItem logout = menu.findItem(R.id.logout);
        if(token !=null)
        {
            singup.setVisible(false);
            login.setVisible(false);

            logout.setVisible(true);
        }
        else
        {
            singup.setVisible(true);
            login.setVisible(true);

            logout.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.login):{
                Intent intent = new Intent(this,LoginActivity.class);
                this.startActivity(intent);
                return true;
            }
            case (R.id.signup):{
                Intent intent = new Intent(this,SignupActivity.class);
                this.startActivity(intent);
                return true;
            }
            case (R.id.logout):{
                TokenStore.removeToken(sharedPreferences);
                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.search_button):{
                if(isConnected()){
                    new HttpAsyncTask().execute(AccessData.SERVER_PATH);
                }else {
                    Toast.makeText(this,"Alert: No internet connection",Toast.LENGTH_LONG).show();
                }

//
            }
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


    public class HttpAsyncTask extends AsyncTask<String ,Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... url) {

            SharedPreferences makeSharedPref = getSharedPreferences(TokenStore.SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            String token = TokenStore.getToken(makeSharedPref);
            String returnList = ReadingsSeachList.GET(url[0],
                    token,
                    String.valueOf(readingType.getSelectedItem()).replaceAll("\\s","").toLowerCase(),
                    String.valueOf(searchType.getSelectedItem()).toLowerCase(),
                    searchBar.getText().toString().trim());

            return returnList;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
            if(s != null){
                final List<ReadingItem> items = new ArrayList<>();

                try{

                    JSONArray ja = new JSONArray(s);
                    errorMassege.setVisibility(View.INVISIBLE);

                    for(int i = 0;i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        ReadingItem rI = new ReadingItem(
                                jo.getString("id"),
                                jo.getString("title"),
                                jo.getString("category"),
                                jo.getString("type")
                        );
                        items.add(rI);
                    }

                        mAdapter = new ReaderListAdapter(items);
                        mRecycleView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListner(new ReaderListAdapter.OnItemClickListner() {
                            @Override
                            public void OnItemClick(int position) {
                                ReadingItem item = items.get(position);
                                Intent intent = new Intent(getApplicationContext(),DetailedReading.class);
                                intent.putExtra("TYPE",readingType.getSelectedItem().toString().replaceAll("\\s","").toLowerCase());
                                intent.putExtra("ID",item.getId());
                                startActivity(intent);

                            }
                        });

//                    }else {
//                        Toast.makeText(getApplicationContext(),"No item found",Toast.LENGTH_LONG);
//                    }

                }catch (JSONException ex){
                    Log.d("ERROR: ",ex.getLocalizedMessage());
                    mAdapter = new ReaderListAdapter(items);
                    mRecycleView.setAdapter(mAdapter);
                    errorMassege.setVisibility(View.VISIBLE);
                }


            }else {
                Toast.makeText(getApplicationContext(),"Alert: No any item found",Toast.LENGTH_LONG).show();
            }

        }
    }
}
