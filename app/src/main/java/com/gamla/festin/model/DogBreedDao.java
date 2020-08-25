package com.gamla.festin.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DogBreedDao {
    @Insert
    List<Long> insertAll(DogBreed... dogs);

    @Query("SELECT * FROM dogbreed")
    List<DogBreed> getAllDogs();

    @Query("SELECT * FROM dogbreed WHERE uui = :dogId")
    DogBreed getDog(int dogId);

    @Query("DELETE FROM dogbreed")
    void deleteAddDogs();
}
