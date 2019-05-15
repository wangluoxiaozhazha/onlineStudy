package com.jazzi.onlinestudy;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;




public class VideoActivity extends AppCompatActivity {


    private VideoView videoView;
    private int id;
    private int CourseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        videoView=(VideoView)findViewById(R.id.video_view);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        CourseId=intent.getIntExtra("CourseId",0);
        initView();
    }

    private void initView() {
        String url = IpConfig.getIp()+":8080/getVideo/?resourceId="+CourseId+"&id="+id;
        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调


        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();
    }

}
