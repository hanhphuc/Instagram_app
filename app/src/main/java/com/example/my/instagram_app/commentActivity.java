package com.example.my.instagram_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class commentActivity extends AppCompatActivity {
    private ArrayList<InstagramPhoto> comments;
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    public String mediaId;
    private InstagramCommentAddapter commentAddapter;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // lấy ActionBar
       // ActionBar actionBar = getActionBar();
        // hiển thị nút Up ở Home icon
        //actionBar.setDisplayHomeAsUpEnabled(true);
        // set màu cho actionBar
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
        setContentView(R.layout.activity_comment);

        ListView lvcomment = (ListView)findViewById(R.id.lvcomment);
        //lấy intent gọi Activity này
        Intent callerIntent=getIntent();
        //có intent rồi thì lấy Bundle dựa vào MyPackage
        Bundle packageFromCaller=
                callerIntent.getBundleExtra("myBundle");
        //Có Bundle rồi thì lấy các thông số dựa vào soa, sob
        mediaId = packageFromCaller.getString("mediaId");
        // xem chuỗi trả về
        Log.i("MEDIA_ID",mediaId+"");

        comments =new ArrayList<>();
        commentAddapter = new InstagramCommentAddapter(this,comments);
        lvcomment.setAdapter(commentAddapter);

        //Loading data
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

        // viết hàm cho commentAddapter
        fetchComment();

    }

    public void fetchComment(){
        String url = "https://api.instagram.com/v1/media/"+ mediaId +"/comments?client_id="+ CLIENT_ID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,null,new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray commentsJSON = null;
                try {
                    commentsJSON = response.getJSONArray("data");
                    for(int i =0;i<commentsJSON.length();i++)
                    {
                        JSONObject commentJSON = commentsJSON.getJSONObject(i);
                        InstagramPhoto comment = new InstagramPhoto();
                        comment.username = commentJSON.getJSONObject("from").getString("username");
                        comment.userImage = commentJSON.getJSONObject("from").getString("profile_picture");
                        comment.comment = commentJSON.getString("text");
                       // Toast.makeText(commentActivity.this, comment.comment, Toast.LENGTH_LONG).show();
                        //add từng comment vào array comment
                        comments.add(comment);
                        }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //load dữ liệu liên tục cho Addapter hiển thị
                commentAddapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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
}
