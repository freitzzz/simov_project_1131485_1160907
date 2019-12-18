package com.ippementa.ipem.model.menu;

import java.io.IOException;

public interface MenusRepository {

    AvailableCanteenMenusResponsePayload availableMenus(long schoolId, long canteenId) throws IOException;

}
