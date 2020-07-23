package com.basicphones.basicweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.KeyEvent;

public class Utility {
    public static String capitalize(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String windDegreeToTextualDescription(double degree) {
        if (degree>337.5) {
            return "N";
        }
        else if (degree>292.5) {
            return "NW";
        }
        else if(degree>247.5) {
            return "W";
        }
        else if(degree>202.5) {
            return "SW";
        }
        else if(degree>157.5) {
            return "S";
        }
        else if(degree>122.5) {
            return "SE";
        }
        else if(degree>67.5) {
            return "E";
        }
        else if(degree>22.5) {
            return "NE";
        }
        return "N";
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager ConnectionManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        } else {
            if (!networkInfo.isConnected()) {
                return false;
            }
        }

        return true;
    }

    public static boolean isConfirmButton(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_BUTTON_A:
                return true;
            default:
                return false;
        }
    }

}
