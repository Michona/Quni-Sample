package com.qusion.quni.domain.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "latest_joke")
data class JokeDto(
    /* We keep a constant PrimaryKey since we need a single entity in this table. */
    @PrimaryKey
    val databaseId: Int = 1,

    @SerializedName("id")
    val id: String,
    @SerializedName("joke")
    val joke: String,
    @SerializedName("status")
    val status: Int
)