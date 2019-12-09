package com.ippementa.ipem.model.school;

import java.util.ArrayList;

public class AvailableSchoolsModel extends ArrayList<AvailableSchoolsModel.Item> {

    public class Item {

        public long id;

        public String acronym;

        public String name;

    }
}
