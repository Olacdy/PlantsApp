package com.example.plantsapp;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Plant {
    private String plant_name;
    private Bitmap plantImage;
    private int days_before_watering = 1;
    private boolean status = true;
    private Date nextWatering;

    public Plant(String plant_name, Bitmap plantImage, int days_before_watering, Date nextWatering)  {
        this(plant_name, plantImage, days_before_watering);
        this.nextWatering = nextWatering;
        this.update();
    }

    public Plant(String plant_name, Bitmap plantImage, int days_before_watering)  {
        this.plant_name = plant_name;
        this.plantImage = plantImage;
        this.days_before_watering = days_before_watering;
        if (this.days_before_watering <= 0) this.days_before_watering = 1;
        this.refresh();
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public int getDays_before_watering() {
        return days_before_watering;
    }

    public void setDays_before_watering(int days_before_watering) {
        this.days_before_watering = days_before_watering;
    }

    public Bitmap getPlantImage() {
        return this.plantImage;
    }

    public void setPlantImage(Bitmap plantImage) {
        this.plantImage = plantImage;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public Date getNextWatering(){
        return this.nextWatering;
    }

    public void setNextWatering(Date nextWatering){
        this.nextWatering = nextWatering;
    }

    public void update()
    {
        if (this.getRemainingPercentage() < 10) this.status = false;
        else this.status = true;
    }

    public void refresh()
    {
        this.nextWatering = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(this.nextWatering);
        c.add(Calendar.DATE, this.days_before_watering);
        this.nextWatering = c.getTime();
    }

    //For demonstration purposes
    public void refreshDemo(){
        this.nextWatering = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
        System.out.println(this.nextWatering);
        Calendar c = Calendar.getInstance();
        c.setTime(this.nextWatering);
        c.add(Calendar.DATE, this.days_before_watering - 15);
        this.nextWatering = c.getTime();
        System.out.println(this.nextWatering);
    }

    public int getRemainingPercentage() {
        double milsec_till_next_watering = (this.nextWatering.getTime() - new Date().getTime());
        double percentage = ((milsec_till_next_watering / TimeUnit.DAYS.toMillis(this.days_before_watering))) * 100;
        percentage = (percentage <= 0) ? 0 : percentage;
        return (int) percentage;
    }
}
