package com.evertrend.tiger.common.bean.mapview.utils;

import android.graphics.Bitmap;

import com.evertrend.tiger.common.bean.mapview.mapdata.MapDataColor;

public class EvertrendImageUtil {
    private final static String TAG = "EvertrendImageUtil";

    private EvertrendImageUtil() {
    }

    public static Bitmap createImage(byte[] buffer, int width, int height) {
        int[] rawData = new int[buffer.length];
        int alpha;

        //原思岚灰0+0x80=128，白127+0x80=255，黑-127+0x80=1；ROS灰-1，白0，黑100；
        for (int i = 0; i < buffer.length; i++) {
//            int grey = 0x80 + buffer[i];
            int grey = 128;
            switch (buffer[i]) {
                case 0:
                    grey = 255;
                    break;
                case 100:
                    grey = 1;
                    break;
            }
            grey = MapDataColor.GREY2RGB_TABLE[grey];
            alpha = (grey == 127) ? 0 : 0xFF;

            rawData[i] = alpha << 24 | grey << 16 | grey << 8 | grey;
        }
        return Bitmap.createBitmap(rawData, width, height, Bitmap.Config.ARGB_8888);
    }
}
