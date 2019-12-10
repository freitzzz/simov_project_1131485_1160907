package com.ippementa.ipem.model.school;

import java.util.ArrayList;

public class AvailableSchoolsResponsePayload extends ArrayList<AvailableSchoolsResponsePayload.Item> {

    public class Item {

        public long id;

        public String acronym;

        public String name;

    }
}
