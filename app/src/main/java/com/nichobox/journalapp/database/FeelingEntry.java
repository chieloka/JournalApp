package com.nichobox.journalapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "feelings")
public class FeelingEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String feeling;
    @ColumnInfo(name = "post_date")
    private Date postDate;

    @Ignore
    public FeelingEntry(String feeling,Date postDate) {
        this.feeling = feeling;
        this.postDate = postDate;
    }

    public FeelingEntry(int id, String feeling,Date postDate) {
        this.id = id;
        this.feeling = feeling;
        this.postDate = postDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
