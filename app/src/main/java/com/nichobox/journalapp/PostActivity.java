/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nichobox.journalapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.nichobox.journalapp.database.AppDatabase;
import com.nichobox.journalapp.database.FeelingEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private EditText dateET;
    private EditText feelingET;
    private Calendar myCalendar;
    private String existingFeeling;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        myCalendar = Calendar.getInstance();
        existingFeeling = "";

        dateET = findViewById(R.id.dateEditText);
        feelingET = findViewById(R.id.feelingEditText);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PostActivity.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Date currDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(currDate);
        //set date editText field to current date
        dateET.setText(today);
    }

    //onSaveBtnClicked is called after the user clicks the save button
    public void onSaveBtnClicked(View view) {
        date = new Date();
        String dDate = dateET.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Date finalDate = date;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Attempt to fetch Any previous entry with the same date if it exist
                final FeelingEntry oldFeeling = appDatabase.feelingDao().loadFeelingByDate(finalDate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(oldFeeling != null){
                            //an old entry for the selected date exists so update the old record by merging them
                            updateFeeling(oldFeeling);
                        }else{
                            //no old record was found so save as new entry
                            saveFeeling();
                        }

                    }
                });
            }
        });



    }

    //Update old record with the new entry that share same date
    private void updateFeeling(FeelingEntry oldFeeling) {
        String dFeeling = oldFeeling.getFeeling()+"\n\n"+feelingET.getText().toString();
        final FeelingEntry feelingEntry = new FeelingEntry(dFeeling, date);
        final int fId = oldFeeling.getId();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                feelingEntry.setId(fId);
                appDatabase.feelingDao().updateFeeling(feelingEntry);
                finish();
            }
        });
    }

    //Save new entry
    private void saveFeeling() {
        String dFeeling = feelingET.getText().toString();

        final FeelingEntry feelingEntry = new FeelingEntry(dFeeling, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.feelingDao().insertFeeling(feelingEntry);
                finish();
            }
        });
    }


    //Update the date EditText view with the currently selected date from the datepicker
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateET.setText(sdf.format(myCalendar.getTime()));
    }
}
