package com.theappfactory.ChattrBox.SimpleNewsChat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class Source {
    public Source() {
    }

    public Source(String id, String name) {
        this.sourceId = id;
        this.getName = name;
    }

//    @NonNull
//    @PrimaryKey (autoGenerate = true)
//    private int intSourceId;

    @NonNull
    @PrimaryKey (autoGenerate = false)
    @SerializedName("id")
    String sourceId;

    @SerializedName("name")
    String getName;

//    @NonNull
//    public int getIntSourceId() {
//        return intSourceId;
//    }
//
//    public void setIntSourceId(@NonNull int intSourceId) {
//        this.intSourceId = intSourceId;
//    }

    public String getId() {
        return sourceId;
    }

    public String getName() {
        return getName;
    }

}
