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

    @Override
    @Query("SELECT * FROM Canteen WHERE id = :canteenId AND :schoolId > -1")
    Canteen canteen(long schoolId, long canteenId) throws IOException;

    @Query("DELETE FROM Canteen")
    void clearTable();
}
