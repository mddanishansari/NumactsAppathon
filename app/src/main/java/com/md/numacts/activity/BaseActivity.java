package com.md.numacts.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.WindowManager;

import com.md.numacts.R;
import com.md.numacts.util.Utils;

/**
 * Created by Danish Ansari on 27-Oct-17.
 */

public class BaseActivity extends AppCompatActivity
{
    private final static int THEME_DARK = 1;
    private final static int THEME_LIGHT = 2;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        updateTheme();
    }

    public void updateTheme()
    {
        if (Utils.getTheme(getApplicationContext()) <= THEME_DARK)
        {
            setTheme(R.style.AppThemeDark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark_blue));
            }
        } else if (Utils.getTheme(getApplicationContext()) == THEME_LIGHT)
        {
            setTheme(R.style.AppThemeLight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark_red));
            }
        }
    }
}
