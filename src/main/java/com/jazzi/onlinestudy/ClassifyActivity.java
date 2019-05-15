package com.jazzi.onlinestudy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClassifyActivity extends AppCompatActivity {

    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter fruitAdapter;
    private RecyclerView fruitRecyclerView;
    private TextView classifyBar;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classify_activity);
        fruitRecyclerView=(RecyclerView)findViewById(R.id.class_view);
        classifyBar=(TextView) findViewById(R.id.classify_bar);
        Intent intent=getIntent();
        String ClassName=intent.getStringExtra("ClassName");
        String userId=intent.getStringExtra("userId");
        classifyBar.setText(ClassName);

        if (intent.getStringExtra("no").equals("1")){

            url=IpConfig.getIp()+":8080/resource/select/?ClassName="+ClassName;
        }else
            url=IpConfig.getIp()+":8080/resource/userlove/?id="+userId;

        System.out.println(url);
        //开辟线程发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendOkHttpRequest(url, new okhttp3.Callback() {


                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到返回的具体内容
                        String responseData=response.body().string();
                        parseClassJson(responseData);

                        Message message=new Message();
                        handler.sendMessage(message);
                    }
                });

            }
        }).start();


    }
    //异步处理
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            GridLayoutManager layoutManager=new GridLayoutManager(ClassifyActivity.this,2);
            fruitRecyclerView.setLayoutManager(layoutManager);
            fruitAdapter =new FruitAdapter(fruitList);
            fruitRecyclerView.setAdapter(fruitAdapter);
        }
    };


    //发送请求
    public void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }


            //解析json数据
    private void parseClassJson(String jsonData){
        try{
            JSONArray jsonArray =new JSONArray(jsonData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int resourceID=jsonObject.getInt("resourceID");
                String courseName=jsonObject.getString("courseName");
                String coverPath=jsonObject.getString("coverPath");
                String courseIntroduction=jsonObject.getString("courseIntroduction");
                String teacher=jsonObject.getString("lecturer");
                String coursePath=jsonObject.getString("coursePath");
                int numberEpisodes=Integer.parseInt(jsonObject.getString("numberEpisodes"));

                Fruit tmp=new Fruit();
                tmp.setResourceId(resourceID);
                tmp.setCourseName(courseName);
                tmp.setCoverPath(coverPath);
                tmp.setCourseIntroduction(courseIntroduction);
                tmp.setLecturer(teacher);
                tmp.setNumberEpisodes(numberEpisodes);
                tmp.setCoursePath(coursePath);
                fruitList.add(tmp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
