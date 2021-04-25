package com.example.plantsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.plantsapp.utils.BitMapToString;
import static com.example.plantsapp.utils.StringToBitMap;

public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public DBHelper(Context context) {
        super(context, "Plants.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Plants(id integer primary key AUTOINCREMENT, name TEXT, watering_frequency integer, image text, next_watering int)");
        List<Plant> plants = utils.getDummyList(this.context);
        for (Plant plant: plants) {
            ContentValues contentValues=new ContentValues();
            contentValues.put("name", plant.getPlant_name());
            contentValues.put("watering_frequency", plant.getDays_before_watering());
            contentValues.put("next_watering", plant.getNextWatering().getTime());
            contentValues.put("image", BitMapToString(plant.getPlantImage()));
            DB.insert("Plants",null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Plants");
    }

    public Boolean insertPlant(Plant plant) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", plant.getPlant_name());
        contentValues.put("watering_frequency", plant.getDays_before_watering());
        contentValues.put("next_watering", plant.getNextWatering().getTime());
        contentValues.put("image", BitMapToString(plant.getPlantImage()));
        long result=DB.insert("Plants", null, contentValues);
        DB.close();
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }


    public Boolean updatePlant(int id, Plant plant) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", plant.getPlant_name());
        contentValues.put("watering_frequency", plant.getDays_before_watering());
        contentValues.put("next_watering", plant.getNextWatering().getTime());
        contentValues.put("image", BitMapToString(plant.getPlantImage()));
        Cursor cursor = DB.rawQuery("Select * from Plants where id = ?", new String[]{ Integer.toString(id) });
        if (cursor.getCount() > 0) {
            long result = DB.update("Plants", contentValues, "id = ?", new String[]{ Integer.toString(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }}


    public Boolean deletePlant(int id)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Plants where id = ?", new String[]{ Integer.toString(id) });
        if (cursor.getCount() > 0) {
            long result = DB.delete("Plants", "id = ?", new String[]{Integer.toString(id)});
            DB.close();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void deleteAll(){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("delete from "+ "Plants");
        DB.close();
    }

    public ArrayList<Plant> getPlants()
    {
        ArrayList<Plant> plants_list = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Plants", null);
        if (cursor.moveToFirst()) {
            do {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(cursor.getString(4)));
                Plant plant = new Plant(cursor.getString(1), StringToBitMap(cursor.getString(3)), Integer.parseInt(cursor.getString(2)), calendar.getTime()) ;

                plants_list.add(plant);
            } while (cursor.moveToNext());
        }
        return plants_list;
    }
}
