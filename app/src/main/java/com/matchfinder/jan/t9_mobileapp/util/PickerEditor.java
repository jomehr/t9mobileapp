package com.matchfinder.jan.t9_mobileapp.util;

import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by Jan on 20.01.2018.
 */

public class PickerEditor {
    //In this utility class implement functions to edit pickers at runtime

    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText) try {
                Field selectorWheelPaintField = numberPicker.getClass()
                        .getDeclaredField("mSelectorWheelPaint");
                selectorWheelPaintField.setAccessible(true);
                ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                ((EditText) child).setTextColor(color);
                numberPicker.invalidate();
                return;
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
