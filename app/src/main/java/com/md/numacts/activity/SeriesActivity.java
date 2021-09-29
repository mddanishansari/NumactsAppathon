package com.md.numacts.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md.numacts.R;
import com.md.numacts.adapter.SeriesFactsAdapter;
import com.md.numacts.data.FactResponse;
import com.md.numacts.databinding.ActivitySeriesBinding;
import com.md.numacts.retrofit.ApiClient;
import com.md.numacts.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeriesActivity extends BaseActivity {
    Toolbar toolbar;
    ActivitySeriesBinding binding;
    ApiInterface apiService;
    Call<HashMap<String, String>> apiCall;
    int firstNumber, secondNumber;
    RecyclerView recyclerView;
    List<FactResponse> list = new ArrayList<>();
    SeriesFactsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_series);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new SeriesFactsAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        Intent i = getIntent();
        firstNumber = i.getIntExtra("firstNum", 1);
        secondNumber = i.getIntExtra("secondNum", 1);
        if ((secondNumber - firstNumber) > 99) {
            Toast.makeText(this, "Due to server limitations only first 100 facts will be shown", Toast.LENGTH_LONG).show();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.series);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(4);
        } else {
            binding.toolbarShadow.setVisibility(View.VISIBLE);
        }

        apiCall = apiService.getSeriesFact(firstNumber, secondNumber);
        apiCall.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                TreeMap<String,String> sortedResponseBody = new TreeMap<>();
                sortedResponseBody.putAll(response.body());

                for (Map.Entry<String, String> factEntry : sortedResponseBody.entrySet()) {
                    String number = factEntry.getKey();
                    String fact = factEntry.getValue();
                    FactResponse data = new FactResponse(fact, Double.parseDouble(number));
                    list.add(data);
                }
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                binding.loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Toast.makeText(SeriesActivity.this, t + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
