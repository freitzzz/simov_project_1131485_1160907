package com.ippementa.ipem.model.dish;

import java.io.IOException;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RoomDishRepositoryImpl extends DishRepository {

    @Override
    @Query("SELECT * FROM Dish WHERE menuId = :menuId AND :canteenId > -1 AND :schoolId > -1")
    List<Dish> dishes(long schoolId, long canteenId, long menuId) throws IOException;

    @Insert
    void insertAll(Dish... dishes);

    @Query("DELETE FROM Dish")
    void clearTable();

}
