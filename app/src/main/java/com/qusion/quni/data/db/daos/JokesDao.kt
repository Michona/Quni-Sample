package com.qusion.quni.data.db.daos

import androidx.room.*
import com.qusion.quni.domain.entities.JokeDto
import kotlinx.coroutines.flow.Flow

@Dao
interface JokesDao {

    @Query("SELECT * FROM latest_joke")
    fun getLatestRandomJoke(): Flow<JokeDto?>

    /** It updates the latest Joke we have in the [latest_joke] table.]*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joke: JokeDto)
}