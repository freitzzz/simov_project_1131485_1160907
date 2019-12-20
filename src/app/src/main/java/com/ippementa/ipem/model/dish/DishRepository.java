package com.ippementa.ipem.model.dish;

import java.io.IOException;
import java.util.List;

public interface DishRepository {

    List<Dish> dishes(long schoolId, long canteenId, long menuId) throws IOException;

}
