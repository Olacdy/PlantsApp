package com.example.plantsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class utils {

    public static long getMilTillTomorrow(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (c.getTimeInMillis() - System.currentTimeMillis());
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        try {
            bitmap = resizeBitmap(bitmap, 150, 150);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        catch (Exception e){
            return "null";
        }
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static List<Plant> getDummyList(Context context){
        Bitmap default_plant_img = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plant);
        List<Plant> list = new ArrayList<Plant>();
        Plant cactus = new Plant("Cactus", default_plant_img, 2);
        Plant rose = new Plant("Rose", default_plant_img, 6);
        Plant gladiolus = new Plant("Gladiolus", default_plant_img, 6);
        Plant orchidaceae = new Plant("Orchidaceae", default_plant_img, 6);
        //For demonstration purposes
        gladiolus.refreshDemo();
        rose.refreshDemo();
        orchidaceae.refreshDemo();

        orchidaceae.update();
        cactus.update();
        rose.update();
        gladiolus.update();

        list.add(cactus);
        list.add(rose);
        list.add(gladiolus);
        list.add(orchidaceae);

        return list;
    }
}
