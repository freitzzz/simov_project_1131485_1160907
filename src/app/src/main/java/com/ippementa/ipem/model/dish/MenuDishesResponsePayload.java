package com.ippementa.ipem.model.dish;

import java.util.ArrayList;

public class MenuDishesResponsePayload extends ArrayList<MenuDishesResponsePayload.Item> {

    public class Item {

        public long id;

        public String type;

        public String description;

    }
}
