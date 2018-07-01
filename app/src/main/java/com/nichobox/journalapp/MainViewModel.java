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

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nichobox.journalapp.database.AppDatabase;
import com.nichobox.journalapp.database.FeelingEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<FeelingEntry>> feelings;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        feelings = appDatabase.feelingDao().loadAllFeelings();
    }

    public LiveData<List<FeelingEntry>> getFeelings() {
        return feelings;
    }
}
