package com.vikash.todotask;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Random;

public class ColorGenerator {

    final static Random mRandom = new Random(System.currentTimeMillis());

    public static int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(128))/2;
        final int green = (baseGreen + mRandom.nextInt(128))/2;
        final int blue = (baseBlue + mRandom.nextInt(128))/2;

        return Color.rgb(red, green, blue);
    }

    public static int colorFader(int color){

        final int baseRed=Color.red(color);
        final int baseGreen=Color.green(color);
        final int baseBlue=Color.blue(color);

        final float t=0.6f;
        final int red=(int)(baseRed+(255-baseRed)*t);
        final int blue=(int)(baseBlue+(255-baseBlue)*t);
        final int green=(int)(baseGreen+(255-baseGreen)*t);

        return Color.rgb(red,green,blue);

    }

    public static int colorForPosition(int position, Context context){
        int color=context.getResources().getColor(R.color.colorPrimary);
        switch(position%5){
            case 0:
                color=context.getResources().getColor(R.color.firstTile);
                break;
            case 1:
                color=context.getResources().getColor(R.color.secondTile);
                break;
            case 2:
                color=context.getResources().getColor(R.color.thirdTile);
                break;
            case 3:
                color=context.getResources().getColor(R.color.fourthTile);
                break;
            case 4:
                color=context.getResources().getColor(R.color.fifthTile);
                break;
        }

        return color;
    }

}
