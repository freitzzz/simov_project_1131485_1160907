package com.ippementa.ipem.model.school;

import java.io.IOException;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RoomSchoolsRepositoryImpl extends SchoolsRepository {

    @Override
    @Query("SELECT * FROM School")
    List<School> availableSchools() throws IOException;

    @Insert
    void insertAll(School... schools);
}
