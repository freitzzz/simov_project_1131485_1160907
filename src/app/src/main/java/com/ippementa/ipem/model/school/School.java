package com.ippementa.ipem.model.school;

import com.ippementa.ipem.model.canteen.Canteen;

import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Models school entity as a relational database table
 * More info at: https://github.com/ipp-ementa/iped-documentation/wiki/Architecture#models-structure
 */
@Entity
public class School {

    @PrimaryKey
    public long id;

    public String acronym;

    public String name;

    public List<Canteen> canteens;

    public School(){}

}
