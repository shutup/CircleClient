package com.shutup.circle.common;

import android.graphics.Color;
import android.util.Log;

import com.shutup.circle.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shutup on 2017/4/24.
 */

public class StringUtils {
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int generateColorFromString(String string) {
        String str = md5(string);
        if (str.length()==32) {
            String colorStr = "#"+str.substring(0,2)+str.substring(15,17)+str.substring(30,32);
            if (BuildConfig.DEBUG) Log.d("StringUtils", colorStr);
            return Color.parseColor(colorStr);
        }else {
            return Color.GRAY;
        }
    }
}
