package com.ippementa.ipem.model.dish;

import java.io.IOException;

public interface DishRepository {

    MenuDishesResponsePayload dishes(long schoolId, long canteenId, long menuId) throws IOException;

}
