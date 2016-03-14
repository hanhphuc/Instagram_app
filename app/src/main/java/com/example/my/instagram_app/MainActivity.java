package com.example.my.instagram_app;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAddapter addapterPhoto;
    public String mediaId;
    private Bundle bundle = new Bundle();
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // lấy ActionBar
        ActionBar actionBar = getActionBar();
        // hiển thị nút Up ở Home icon
        //actionBar.setDisplayHomeAsUpEnabled(true);
        // set màu cho actionBar
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));

        //SENT OUT API REQUEST TO POPULAR PHOTOS
        photos = new ArrayList<>();
        addapterPhoto =new InstagramPhotoAddapter(this,photos);
        ListView lvPhoto = (ListView)findViewById(R.id.listView);
        lvPhoto.setAdapter(addapterPhoto);

        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 20;
                        progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        fetchPopularPhotos();


    }
    public void fetchPopularPhotos(){

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        //Create the network client
        AsyncHttpClient client = new  AsyncHttpClient();
        // Trigger the Get client
       client.get(url, null, new JsonHttpResponseHandler() {

           @Override
           public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               //Do something
               //Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_LONG).show();
           }

           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               JSONArray photosJson = null;
               try {
                   photosJson = response.getJSONArray("data");
                   for (int i = 0; i < photosJson.length(); i++) {
                       JSONObject photoJSON = photosJson.getJSONObject(i);
                       InstagramPhoto photo = new InstagramPhoto();
                       photo.username = photoJSON.getJSONObject("user").getString("username");
                       photo.userImage = photoJSON.getJSONObject("user").getString("profile_picture");
                       photo.caption = photoJSON.getJSONObject("caption").getString("text");
                       photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                       photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                       photo.LikesCount = photoJSON.getJSONObject("likes").getInt("count");
                       photo.location = photoJSON.getString("location");
                       Log.i("location", photo.location + "");

                       mediaId = photo.mediaId = photoJSON.getString("id");
                       photo.dateUtils = photoJSON.getJSONObject("caption").getString("created_time");


                       JSONArray array = photoJSON.getJSONObject("comments").getJSONArray("data");
                       if(array.length()>0) {
                           photo.userComment = array.getJSONObject(array.length() - 1).getJSONObject("from").getString("username");
                           photo.comment = array.getJSONObject(array.length() - 1).getString("text");
                           photo.userComment2 = array.getJSONObject(array.length() - 2).getJSONObject("from").getString("username");
                           photo.comment2 = array.getJSONObject(array.length() - 2).getString("text");
                           photo.numberComment = array.length();
                       }
                       photos.add(photo);
                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }
               // notifyDataSetChanged() ->hàm này để listview load lại dữ liệu
               addapterPhoto.notifyDataSetChanged();
           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }


    public void btnClick(View view) {
        Intent myIntent = new Intent(this,commentActivity.class);
        bundle.putString("mediaId",mediaId);
        myIntent.putExtra("myBundle",bundle);
        startActivity(myIntent);
    }
}

