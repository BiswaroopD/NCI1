package com.example.biswaroop.nci;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

@Entity
public class Source {
    public Source() {
    }

    public Source(String id, String name) {
        this.sourceId = id;
        this.getName = name;
    }

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    String sourceId;

    @SerializedName("name")
    String getName;

    public String getId() {
        return sourceId;
    }

    public String getName() {
        return getName;
    }
}
