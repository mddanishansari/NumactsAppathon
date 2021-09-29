package com.md.numacts.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.md.numacts.R;
import com.md.numacts.databinding.ActivityQuestMenuFactsBinding;

public class QuestFactsMenuActivity extends BaseActivity
{
    ActivityQuestMenuFactsBinding binding;
    CardView btnTriviaCV, btnMathCV, btnDateCV, btnYearCV;
    TextView btnTriviaTV, btnMathTV, btnDateTV, btnYearTV;
    Toolbar toolbar;
    String toolbar_title;
    ActivityOptionsCompat options;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quest_menu_facts);
        assignIds();

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.quest_facts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getSupportActionBar().setElevation(4);
        } else
        {
            binding.toolbarShadow.setVisibility(View.VISIBLE);
        }
        binding.btnTrivia.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toolbar_title = getString(R.string.trivia);
                startNextActivity();
            }
        });
        binding.btnMath.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toolbar_title = getString(R.string.math);
                startNextActivity();
            }
        });
        binding.btnDate.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toolbar_title = getString(R.string.date);
                startNextActivity();
            }
        });
        binding.btnYear.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toolbar_title = getString(R.string.year);
                startNextActivity();
            }
        });
    }

    private void startNextActivity()
    {
        startActivity(new Intent(this, QuestFactsActivity.class).putExtra("toolbar_title", toolbar_title));
    }

    private void assignIds()
    {


        CardView btnTriviaCV = binding.btnTrivia.btnHolder;
        CardView btnMathCV = binding.btnMath.btnHolder;
        CardView btnDateCV = binding.btnDate.btnHolder;
        CardView btnYearCV = binding.btnYear.btnHolder;


        TextView btnTriviaTV = binding.btnTrivia.btnLabel;
        TextView btnMathTV = binding.btnMath.btnLabel;
        TextView btnDateTV = binding.btnDate.btnLabel;
        TextView btnYearTV = binding.btnYear.btnLabel;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            btnTriviaCV.setElevation(4);
            btnMathCV.setElevation(4);
            btnDateCV.setElevation(4);
            btnYearCV.setElevation(4);
        }


        btnTriviaTV.setText(R.string.trivia);
        btnMathTV.setText(R.string.math);
        btnDateTV.setText(R.string.date);
        btnYearTV.setText(R.string.year);

    }

}
