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

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.nichobox.journalapp.database.AppDatabase;
import com.nichobox.journalapp.database.FeelingEntry;

import java.util.Date;

public class ViewEntryActivity extends AppCompatActivity {

    private int entryId;
    private AppDatabase appDatabase;
    private TextView tv_date;
    private TextView tv_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        tv_date = findViewById(R.id.tv_entry_date);
        tv_body = findViewById(R.id.tv_entry_body);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            entryId = Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT));

            PostViewModelFactory factory = new PostViewModelFactory(appDatabase, entryId);
            final PostViewModel viewModel = ViewModelProviders.of(this, factory).get(PostViewModel.class);

            viewModel.getFeeling().observe(this, new Observer<FeelingEntry>() {
                @Override
                public void onChanged(@Nullable FeelingEntry feelingEntry) {
                    viewModel.getFeeling().removeObserver(this);
                    displayEntry(feelingEntry);
                }
            });

        }else{
            finish();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEntryActivity.this, UpdateEntryActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(entryId));
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void displayEntry(FeelingEntry feelingEntry) {
        Date postDate = feelingEntry.getPostDate();
        String dDate = (String) DateFormat.format("dd MMMM yyyy", postDate);
        tv_date.setText(dDate);
        tv_body.setText(feelingEntry.getFeeling());

        getSupportActionBar().setTitle(dDate);
    }

}
