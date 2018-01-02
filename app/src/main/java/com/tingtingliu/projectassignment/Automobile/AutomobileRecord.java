package com.tingtingliu.projectassignment.Automobile;

import java.sql.Date;

/**
 * Created by justinparo on 2017-12-29.
 */

public class AutomobileRecord {

    private Integer id, liters, price, km, date;
    //private Date date;

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
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLiters() {
        return liters;
    }

    public void setLiters(Integer liters) {
        this.liters = liters;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }
}
