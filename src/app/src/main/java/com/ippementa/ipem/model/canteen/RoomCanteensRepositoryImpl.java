package com.ippementa.ipem.model.canteen;

import java.io.IOException;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RoomCanteensRepositoryImpl extends CanteensRepository {

    @Override
    @Query("SELECT * FROM Canteen WHERE schoolId = :schoolId")
    List<Canteen> canteens(long schoolId) throws IOException;

    @Insert
    void insertAll(Canteen... canteens);

    @Query("DELETE FROM Canteen")
    void clearTable();
}
