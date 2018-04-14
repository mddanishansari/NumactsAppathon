package com.md.numacts.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.md.numacts.R;
import com.race604.drawable.wave.WaveDrawable;

public class SplashActivity extends BaseActivity
{
    ImageView ivIcon;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        WaveDrawable splashicon=new WaveDrawable(this,R.mipmap.ic_launcher);
        splashicon.setIndeterminate(true);
        splashicon.setWaveLength(750);
        ivIcon.setImageDrawable(splashicon);



        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3800);

    }
}
