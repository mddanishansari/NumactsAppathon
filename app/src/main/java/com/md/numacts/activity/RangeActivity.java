package com.md.numacts.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.md.numacts.R;
import com.md.numacts.data.FactResponse;
import com.md.numacts.databinding.ActivityRangeBinding;
import com.md.numacts.retrofit.ApiClient;
import com.md.numacts.retrofit.ApiInterface;
import com.md.numacts.util.Utils;
import com.robinhood.ticker.TickerUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RangeActivity extends BaseActivity
{
    ActivityRangeBinding binding;
    Toolbar toolbar;
    int min, max;
    ApiInterface apiService;
    Call<FactResponse> apiCall;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_range);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.range);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getSupportActionBar().setElevation(4);
        }
        else
        {
            binding.toolbarShadow.setVisibility(View.VISIBLE);
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);

        Intent i = getIntent();
        min = i.getIntExtra("min", 0);
        max = i.getIntExtra("max", 0);

        binding.fabNext.hide();
        binding.minNumber.setText("Min: " + min);
        binding.maxNumber.setText("Max: " + max);

        binding.resultNumber.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        binding.resultNumber.setAnimationDuration(550);
        binding.resultNumber.setGravity(Gravity.CENTER);
        binding.resultNumber.setText("Loading...");
        fetchFact(min, max);
    }

    private void fetchFact(int min, int max)
    {
        apiCall = apiService.getFactInRange(min, max);
        apiCall.enqueue(new Callback<FactResponse>()
        {
            @Override
            public void onResponse(Call<FactResponse> call, Response<FactResponse> response)
            {
                if (response != null && response.isSuccessful())
                {
                    binding.resultNumber.setText((long) response.body().getNumber() + "");
                    binding.resultText.setText(response.body().getText());
                    binding.fabNext.show();
                }
            }

            @Override
            public void onFailure(Call<FactResponse> call, Throwable t)
            {

            }
        });
    }

    public void fabNext(View v)
    {
        if (Utils.isNetworkAvailable(this))
        {
            fetchFact(min, max);
        }
        else
        {
            Toast.makeText(this, "No internet connection found", Toast.LENGTH_LONG).show();
        }
    }
}
