package com.ippementa.ipem.model.canteen;

import com.ippementa.ipem.model.school.School;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Models canteen entity as a relational database table
 * More info at: https://github.com/ipp-ementa/iped-documentation/wiki/Architecture#models-structure
 */
@Entity(foreignKeys = @ForeignKey(entity = Canteen.class, parentColumns = "id", childColumns = "schoolId"))
public class Canteen {

    @PrimaryKey
    public long id;

    @ForeignKey(entity = School.class, parentColumns = "id", childColumns = "schoolId")
    public long schoolId;

    public String name;

    @Embedded
    public Location location;

    //public List<Menu> menus;

    public Canteen(){}

    public static class Location{

        public double latitude;

        public double longitude;

        public Location(){}

    }

}
