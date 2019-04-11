package com.arshilgenius.kisan.agriculture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private List<News_List> news_list;
    private NewsAdapter1 myAdapter;
    private String Url="https://newsapi.org/v2/everything?sources=the-times-of-india&q=Farmer&apiKey=2012066be1c944409c701878d544b5fc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        news_list = new ArrayList<>();
        myAdapter = new NewsAdapter1(this,news_list,null,0);
        mRecyclerView.setAdapter(myAdapter);

        getNewsData(Url);
        myAdapter.notifyDataSetChanged();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)(findViewById(R.id.swipe_refresh_layout));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            news_list.clear();
            getNewsData(Url);
            myAdapter.notifyDataSetChanged();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { swipeRefreshLayout.setRefreshing(false);
                }
            },4000);
            }
        });
    }

    private void getNewsData(String url) {
        //Initialising dialog box
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.e("inside onResponse","reached");

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    Log.e("inside try block","reached");

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        News_List item = new News_List(object.getString("title"),
                                object.getString("description"),object.getString("urlToImage"),object.getString("url"));
                        news_list.add(item);
                        myAdapter.notifyDataSetChanged();
                    }

                    if(news_list.size()==0){
                        Toast.makeText(getApplicationContext(),"That was Last Page",Toast.LENGTH_SHORT).show();
                    }

                }catch(JSONException e){
                    //progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

        //Requesting using volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
