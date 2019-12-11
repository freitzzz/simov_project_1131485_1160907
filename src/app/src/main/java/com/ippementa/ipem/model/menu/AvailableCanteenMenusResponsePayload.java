package com.ippementa.ipem.model.menu;

import java.util.ArrayList;

public class AvailableCanteenMenusResponsePayload extends ArrayList<AvailableCanteenMenusResponsePayload.Item> {

    public class Item {

        public long id;

        public String type;

    }
}
