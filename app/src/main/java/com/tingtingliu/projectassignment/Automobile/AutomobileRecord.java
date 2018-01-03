package com.tingtingliu.projectassignment.Automobile;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by justinparo on 2017-12-29.
 */

public class AutomobileRecord {

    private Integer id, liters, price, km, date;
    //private Date date;

    public AutomobileRecord() {
        //Empty Constructor
    }

    public AutomobileRecord(Integer id, Integer liters, Integer price, Integer km, Integer date) {
        this.id = id;
        this.liters = liters;
        this.price = price;
        this.km = km;
        this.date = date;
    }

    public AutomobileRecord(Integer liters, Integer price, Integer km) {
        this.liters = liters;
        this.price = price;
        this.km = km;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLiters() {
        return this.liters;
    }

    public void setLiters(Integer liters) {
        this.liters = liters;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getKm() {
        return this.km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getDate() {
        return this.date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }
}
