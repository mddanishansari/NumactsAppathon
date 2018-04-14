package com.md.numacts.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.md.numacts.R;
import com.md.numacts.data.FactResponse;
import com.md.numacts.databinding.ActivityMainBinding;
import com.md.numacts.retrofit.ApiClient;
import com.md.numacts.retrofit.ApiInterface;
import com.md.numacts.util.Utils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
{
    ActivityMainBinding binding;
    Toolbar toolbar;
    boolean firstCall = true;
    ApiInterface apiService;
    Call<FactResponse> apiCall;
    ProgressDialog pd;
    int dd, mm;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getSupportActionBar().setElevation(4);
        }
        else
        {
            binding.toolbarShadow.setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setTitle("Home");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            binding.btnRandomFacts.btnHolder.setElevation(4.0f);
            binding.btnQuestFacts.btnHolder.setElevation(4.0f);
            binding.btnRange.btnHolder.setElevation(4.0f);
            binding.btnSeries.btnHolder.setElevation(4.0f);
            binding.btnThisDayThatYear.btnHolder.setElevation(4.0f);
            binding.btnHistory.btnHolder.setElevation(4.0f);
        }
        binding.btnQuestFacts.btnLabel.setText(getString(R.string.quest_facts));
        binding.btnRandomFacts.btnLabel.setText(getString(R.string.random_facts));
        binding.btnRange.btnLabel.setText(getString(R.string.range));
        binding.btnSeries.btnLabel.setText(getString(R.string.series));
        binding.btnThisDayThatYear.btnLabel.setText("#TDTY");
        binding.btnHistory.btnLabel.setText(getString(R.string.history));

        binding.btnQuestFacts.btnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
        binding.btnRandomFacts.btnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
        binding.btnRange.btnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
        binding.btnSeries.btnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
        binding.btnThisDayThatYear.btnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
        binding.btnHistory.btnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);

        apiService = ApiClient.getClient().create(ApiInterface.class);


        binding.btnQuestFacts.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, QuestFactsMenuActivity.class));
            }
        });

        binding.btnRandomFacts.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, RandomFactsActivity.class));
            }
        });
        binding.btnThisDayThatYear.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Utils.isNetworkAvailable(MainActivity.this))
                {

                    Calendar cal = Calendar.getInstance();
                    dd = cal.get(Calendar.DAY_OF_MONTH);
                    mm = cal.get(Calendar.MONTH) + 1;

                    pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Just a moment");
                    pd.show();
                    todaysFact();
                }
                else
                {
                    Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnHistory.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        binding.btnRange.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View dialogView = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_edittexts, null);
                final AlertDialog.Builder numberDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                numberDialogBuilder.setTitle("Range Numbers");
                numberDialogBuilder.setView(dialogView);
                final EditText firstNumber = dialogView.findViewById(R.id.etFirstNumber);
                final EditText secondNumber = dialogView.findViewById(R.id.etSecondNumber);
                numberDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {


                    }
                });
                numberDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                final AlertDialog numberDialog = numberDialogBuilder.create();
                numberDialog.show();
                numberDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (rangeSeriesValidData("range", firstNumber, secondNumber))
                        {
                            if (Utils.isNetworkAvailable(MainActivity.this))
                            {
                                numberDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, RangeActivity.class)
                                        .putExtra("min", Integer.parseInt(firstNumber.getText().toString().trim()))
                                        .putExtra("max", Integer.parseInt(secondNumber.getText().toString().trim())));
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "No internet connection found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }
        });

        binding.btnSeries.btnHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View dialogView = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_edittexts, null);
                final AlertDialog.Builder numberDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                numberDialogBuilder.setTitle("Series of facts");
                numberDialogBuilder.setView(dialogView);
                final EditText firstNumber = dialogView.findViewById(R.id.etFirstNumber);
                final EditText secondNumber = dialogView.findViewById(R.id.etSecondNumber);

                firstNumber.setHint("Enter first number (eg. 1)");
                secondNumber.setHint("Enter second number (eg. 100)");

                numberDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {


                    }
                });
                numberDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                final AlertDialog numberDialog = numberDialogBuilder.create();
                numberDialog.show();
                numberDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (rangeSeriesValidData("series", firstNumber, secondNumber))
                        {
                            if (Utils.isNetworkAvailable(MainActivity.this))
                            {
                                numberDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, SeriesActivity.class)
                                        .putExtra("firstNum", Integer.parseInt(firstNumber.getText().toString().trim()))
                                        .putExtra("secondNum", Integer.parseInt(secondNumber.getText().toString().trim())));
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "No internet connection found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }
        });

    }

    private boolean rangeSeriesValidData(String rangeOrSeries, EditText firstNumber, EditText secondNumber)
    {
        if (rangeOrSeries.equals("range"))
        {
            if (firstNumber.getText().toString().isEmpty())
            {
                firstNumber.requestFocus();
                Toast.makeText(this, "Enter minimum number", Toast.LENGTH_SHORT).show();
            }
            else if (secondNumber.getText().toString().isEmpty())
            {
                secondNumber.requestFocus();
                Toast.makeText(this, "Enter maximum number", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int min = Integer.parseInt(firstNumber.getText().toString());
                int max = Integer.parseInt(secondNumber.getText().toString());
                if (min == max)
                {
                    Toast.makeText(this, "Both numbers can\'t be same", Toast.LENGTH_SHORT).show();
                }
                else if (min > max)
                {
                    Toast.makeText(this, "Minimum number can\'t be greater than maximum number", Toast.LENGTH_LONG).show();
                }
                else
                {
                    return true;
                }

            }
        }
        else if (rangeOrSeries.equals("series"))
        {
            if (firstNumber.getText().toString().isEmpty())
            {
                firstNumber.requestFocus();
                Toast.makeText(this, "Enter first number", Toast.LENGTH_SHORT).show();
            }
            else if (secondNumber.getText().toString().isEmpty())
            {
                secondNumber.requestFocus();
                Toast.makeText(this, "Enter second number", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int min = Integer.parseInt(firstNumber.getText().toString());
                int max = Integer.parseInt(secondNumber.getText().toString());
                if (min == max)
                {
                    Toast.makeText(this, "Both numbers can\'t be same", Toast.LENGTH_SHORT).show();
                }
                else if (min > max)
                {
                    Toast.makeText(this, "First number can\'t be greater than second number", Toast.LENGTH_LONG).show();
                }
                else
                {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.change_theme:
                int currentTheme = Utils.getTheme(MainActivity.this);
                if (currentTheme <= 1)
                {
                    Utils.setTheme(MainActivity.this, 2);
                }
                else if (currentTheme == 2)
                {
                    Utils.setTheme(MainActivity.this, 1);
                }

                recreateActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void todaysFact()
    {
        apiCall = apiService.getDateFact(dd, mm);
        apiCall.enqueue(new Callback<FactResponse>()
        {
            @Override
            public void onResponse(Call<FactResponse> call, Response<FactResponse> response)
            {
                if (response != null && response.isSuccessful())
                {
                    final String fact = response.body().getText();
                    if (pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.this_day_that_year)
                            .setMessage(fact)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Next", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    todaysFact();
                                }
                            })
                            .setNeutralButton("Share", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    String shareFact = "Do you know ?\n\n" + fact + "\n\n#TDTY #ThisDayThatYear";
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND)
                                            .setType("text/plain")
                                            .putExtra(Intent.EXTRA_TEXT, shareFact);
                                    startActivity(Intent.createChooser(shareIntent, "Share with"));
                                }
                            })
                            .create().show();
                }
            }

            @Override
            public void onFailure(Call<FactResponse> call, Throwable t)
            {
                if (pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(MainActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void recreateActivity()
    {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
