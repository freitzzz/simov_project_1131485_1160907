package com.ippementa.ipem.model.menu;

import java.io.IOException;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RoomMenusRepositoryImpl extends MenusRepository {

    @Override
    @Query("SELECT * FROM Menu WHERE canteenId = :canteenId AND :schoolId > -1")
    List<Menu> menus(long schoolId, long canteenId) throws IOException;

    @Insert
    void insertAll(Menu... menus);

    @Query("DELETE FROM Menu")
    void clearTable();
}
