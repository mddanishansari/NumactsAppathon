package com.md.numacts.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.md.numacts.database.DbHelper;
import com.md.numacts.retrofit.ApiClient;
import com.md.numacts.retrofit.ApiInterface;
import com.md.numacts.data.FactResponse;
import com.md.numacts.R;
import com.md.numacts.util.Utils;
import com.md.numacts.databinding.ActivityQuestFactsBinding;
import com.robinhood.ticker.TickerUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestFactsActivity extends BaseActivity
{
    ActivityQuestFactsBinding binding;
    Toolbar toolbar;
    ApiInterface apiService;
    Call<FactResponse> apiCall;
    String type;
    boolean triviaCalled = true, mathCalled = true, yearCalled = true, dateCalled = true;
    SQLiteDatabase db;
    ContentValues values;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quest_facts);
        binding.fabShare.hide();
        values = new ContentValues();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        db = new DbHelper(this).getWritableDatabase();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getSupportActionBar().setElevation(4);
        } else
        {
            binding.toolbarShadow.setVisibility(View.VISIBLE);
        }
        Intent i = getIntent();
        type = i.getStringExtra("toolbar_title");
        getSupportActionBar().setTitle(type);

        binding.resultNumber.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        binding.resultNumber.setAnimationDuration(550);
        binding.resultNumber.setGravity(Gravity.CENTER);

        if (type.equals(getString(R.string.trivia)))
        {
            triviaSetup();
        } else if (type.equals(getString(R.string.math)))
        {
            mathSetup();
        } else if (type.equals(getString(R.string.year)))
        {
            yearSetup();
        } else if (type.equals(getString(R.string.date)))
        {
            dateSetup();
        }


        binding.fabShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String shareFact = "Do you know ?\n\n" + binding.resultText.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, shareFact);
                startActivity(Intent.createChooser(shareIntent, "Share with"));
            }
        });

        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    private void dateSetup()
    {

        Calendar cal = Calendar.getInstance();
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int mm = cal.get(Calendar.MONTH) + 1;

        binding.dateLayout.setVisibility(View.VISIBLE);
        binding.npDD.setMinValue(1);
        binding.npDD.setMaxValue(31);
        binding.npDD.setValue(dd);

        binding.npMM.setMinValue(1);
        binding.npMM.setMaxValue(12);
        binding.npMM.setValue(mm);
    }

    public void getDateFact(View v)
    {
        final int dd = binding.npDD.getValue();
        final int mm = binding.npMM.getValue();
        if (isDateValid(dd, mm))
        {
            if (Utils.isNetworkAvailable(QuestFactsActivity.this))
            {
                if (dateCalled)
                {
                    binding.resultNumber.setText("Loading...");
                    dateCalled = false;
                }
                apiCall = apiService.getDateFact(dd, mm);
                apiCall.enqueue(new Callback<FactResponse>()
                {
                    @Override
                    public void onResponse(Call<FactResponse> call, Response<FactResponse> response)
                    {
                        if (response != null && response.isSuccessful())
                        {
                            if (response.body().isFound() == true)
                            {
                                showFab();
                                String date = response.body().getText().split(" ")[0] + " " + response.body().getText().split(" ")[1];
                                binding.resultNumber.setText(date);
                                binding.resultText.setText(response.body().getText());

                                values.put(DbHelper.COLUMN_NAME_TYPE, "date");
                                values.put(DbHelper.COLUMN_NAME_NUMBER, date);
                                values.put(DbHelper.COLUMN_NAME_FACT, response.body().getText());
                                db.insert(DbHelper.TABLE_NAME_QUEST, null, values);
                            } else
                            {
                                binding.resultNumber.setText("Sorry :(");
                                String[] month_array = getResources().getStringArray(R.array.month_array);
                                binding.resultText.setText("Oops no fact was found for " + dd + " " + month_array[mm - 1]);
                                hideFab();
                                Toast.makeText(QuestFactsActivity.this, "Please try different date", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FactResponse> call, Throwable t)
                    {

                    }
                });
            } else
            {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
    }

    private boolean isDateValid(int dd, int mm)
    {
        if (dd == 31 && (mm == 2 || mm == 4 || mm == 6 || mm == 9 || mm == 11))
            return false;
        else if (dd == 30 && mm == 2)
            return false;
        else return true;
    }

    private void yearSetup()
    {
        binding.yearLayout.setVisibility(View.VISIBLE);
        binding.yearSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(final String query)
            {
                if (Utils.isNetworkAvailable(QuestFactsActivity.this))
                {
                    if (yearCalled)
                    {
                        binding.resultNumber.setText("Loading...");
                        yearCalled = false;
                    }
                    binding.yearSearchView.clearFocus();
                    if (binding.ad.isChecked())
                        apiCall = apiService.getYearFact(Integer.valueOf(query));
                    else
                        apiCall = apiService.getYearFact(Integer.valueOf("-" + query));
                    apiCall.enqueue(new Callback<FactResponse>()
                    {
                        @Override
                        public void onResponse(Call<FactResponse> call, Response<FactResponse> response)
                        {
                            if (response != null && response.isSuccessful())
                            {
                                if (response.body().isFound() == true)
                                {
                                    showFab();
                                    String year;
                                    if (binding.ad.isChecked())
                                        year = (int) response.body().getNumber() + " AD";
                                    else
                                        year = Math.abs((int) response.body().getNumber()) + " BC";
                                    binding.resultNumber.setText(year);
                                    binding.resultText.setText(response.body().getText());


                                    values.put(DbHelper.COLUMN_NAME_TYPE, "year");
                                    values.put(DbHelper.COLUMN_NAME_NUMBER, year);
                                    values.put(DbHelper.COLUMN_NAME_FACT, response.body().getText());
                                    db.insert(DbHelper.TABLE_NAME_QUEST, null, values);
                                } else
                                {
                                    binding.resultNumber.setText("Sorry :(");
                                    if (binding.ad.isChecked())
                                        binding.resultText.setText("Oops no fact was found for " + query + " AD");
                                    else
                                        binding.resultText.setText("Oops no fact was found for " + query + " BC");
                                    hideFab();
                                    Toast.makeText(QuestFactsActivity.this, R.string.please_try_different_number, Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FactResponse> call, Throwable t)
                        {
                            binding.resultNumber.setText("Something went wrong :(");
                            Toast.makeText(QuestFactsActivity.this, t.toString() + "\t" + t.getLocalizedMessage() + "\t" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else
                {
                    Toast.makeText(QuestFactsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

    }

    private void mathSetup()
    {
        binding.mathLayout.setVisibility(View.VISIBLE);

        binding.mathSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(final String query)
            {
                if (Utils.isNetworkAvailable(QuestFactsActivity.this))
                {
                    if (mathCalled)
                    {
                        binding.resultNumber.setText("Loading...");
                        mathCalled = false;
                    }
                    binding.mathSearchView.clearFocus();
                    apiCall = apiService.getMathFact(Integer.valueOf(query));
                    apiCall.enqueue(new Callback<FactResponse>()
                    {
                        @Override
                        public void onResponse(Call<FactResponse> call, Response<FactResponse> response)
                        {
                            if (response != null && response.isSuccessful())
                            {
                                if (response.body().isFound() == true)
                                {
                                    showFab();
                                    binding.resultNumber.setText((int) response.body().getNumber() + "");
                                    binding.resultText.setText(response.body().getText());

                                    values.put(DbHelper.COLUMN_NAME_TYPE, "math");
                                    values.put(DbHelper.COLUMN_NAME_NUMBER, (int) response.body().getNumber() + "");
                                    values.put(DbHelper.COLUMN_NAME_FACT, response.body().getText());
                                    db.insert(DbHelper.TABLE_NAME_QUEST, null, values);
                                } else
                                {
                                    binding.resultNumber.setText("Sorry :(");
                                    binding.resultText.setText("Oops no fact was found for " + query);
                                    hideFab();
                                    Toast.makeText(QuestFactsActivity.this, R.string.please_try_different_number, Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FactResponse> call, Throwable t)
                        {

                        }
                    });

                } else
                {
                    Toast.makeText(QuestFactsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }


    private void triviaSetup()
    {
        binding.triviaLayout.setVisibility(View.VISIBLE);

        binding.triviaSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(final String query)
            {
                if (Utils.isNetworkAvailable(QuestFactsActivity.this))
                {
                    if (triviaCalled)
                    {
                        binding.resultNumber.setText("Loading...");
                        triviaCalled = false;
                    }
                    binding.triviaSearchView.clearFocus();
                    apiCall = apiService.getTriviaFact(Integer.valueOf(query));
                    apiCall.enqueue(new Callback<FactResponse>()
                    {
                        @Override
                        public void onResponse(Call<FactResponse> call, Response<FactResponse> response)
                        {
                            if (response != null && response.isSuccessful())
                            {
                                if (response.body().isFound() == true)
                                {
                                    binding.fabShare.show();
                                    binding.resultNumber.setText((int) response.body().getNumber() + "");
                                    binding.resultText.setText(response.body().getText());

                                    values.put(DbHelper.COLUMN_NAME_TYPE, "trivia");
                                    values.put(DbHelper.COLUMN_NAME_NUMBER, (int) response.body().getNumber() + "");
                                    values.put(DbHelper.COLUMN_NAME_FACT, response.body().getText());
                                    db.insert(DbHelper.TABLE_NAME_QUEST, null, values);
                                } else
                                {
                                    binding.resultNumber.setText("Sorry :(");
                                    binding.resultText.setText("Oops no fact was found for " + query);
                                    hideFab();
                                    Toast.makeText(QuestFactsActivity.this, R.string.please_try_different_number, Toast.LENGTH_LONG).show();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FactResponse> call, Throwable t)
                        {

                        }
                    });

                } else
                {
                    Toast.makeText(QuestFactsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    private void hideFab()
    {
        if (binding.fabShare.isShown())
            binding.fabShare.hide();
    }

    private void showFab()
    {
        if (!binding.fabShare.isShown())
            binding.fabShare.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
