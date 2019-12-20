package com.ippementa.ipem.model.menu;

import java.io.IOException;
import java.util.List;

public interface MenusRepository {

    List<Menu> menus(long schoolId, long canteenId) throws IOException;

}
