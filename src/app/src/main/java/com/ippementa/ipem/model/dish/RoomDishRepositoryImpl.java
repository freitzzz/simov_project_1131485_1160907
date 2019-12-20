package com.ippementa.ipem.model.dish;

import java.io.IOException;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RoomDishRepositoryImpl extends DishRepository {

    @Override
    @Query("SELECT * FROM Dish WHERE menuId = :menuId")
    List<Dish> dishes(long schoolId, long canteenId, long menuId) throws IOException;

    @Insert
    void insertAll(Dish... dishes);

}
