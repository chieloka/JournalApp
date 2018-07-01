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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichobox.journalapp.database.FeelingEntry;

import java.util.Date;
import java.util.List;

public class FeelingAdapter extends RecyclerView.Adapter<FeelingAdapter.FeelingViewHolder>  {
    private int itemsNumber;
    final private ItemClickListener itemClickListener;
    private List<FeelingEntry> mFeelingEntries;
    private Context mContext;

    public FeelingAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public FeelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.feeling_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        FeelingViewHolder holder = new FeelingViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FeelingViewHolder holder, int position) {
        // get the needed values
        FeelingEntry feelingEntry = mFeelingEntries.get(position);
        String feeling = feelingEntry.getFeeling();
        Date postDate = feelingEntry.getPostDate();
        String day = (String) DateFormat.format("dd",   postDate);
        String month = (String) DateFormat.format("MMM",  postDate);
        String year = (String) DateFormat.format("yyyy", postDate);
        String dDate = (String) DateFormat.format("E MMM yyyy", postDate);
        String monthYear = month+" "+year;

        String[] feelingSplit = feeling.split("\n", 2);
        if(feelingSplit[0].length() > 25){
            feelingSplit[0] = feelingSplit[0].substring(0,23)+"...";
        }

        //Set values
        holder.listItemFeelingView.setText(feelingSplit[0]);
        holder.listItemDayView.setText(day);
        holder.listItemDateView.setText(dDate);

    }

    @Override
    public int getItemCount() {
        if (mFeelingEntries == null) {
            return 0;
        }
        return mFeelingEntries.size();
    }

    public void setFeelings(List<FeelingEntry> feelingEntries) {
        mFeelingEntries = feelingEntries;
        notifyDataSetChanged();
    }

    class FeelingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemFeelingView;
        TextView listItemDayView;
        TextView listItemDateView;

        public FeelingViewHolder(View item) {
            super(item);

            listItemFeelingView = (TextView) item.findViewById(R.id.tv_item_feeling);
            listItemDayView = (TextView) item.findViewById(R.id.tv_day);
            listItemDateView = (TextView) item.findViewById(R.id.tv_date);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mFeelingEntries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(elementId);
        }
    }

    public List<FeelingEntry> getFeelings() {
        return mFeelingEntries;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
}
