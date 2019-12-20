package com.ippementa.ipem.model.canteen;

import java.util.ArrayList;

public class AvailableCanteensResponsePayload extends ArrayList<AvailableCanteensResponsePayload.Item> {

    public class Item {

        public long id;

        public String name;

        public Item(){}

    }
}
