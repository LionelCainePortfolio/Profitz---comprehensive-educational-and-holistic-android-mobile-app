package com.profitz.app.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class RoundCard{
    int a,x,y;
    float bmr;
    public Bitmap setEffect(Bitmap bm,float radius,int color){
        bm=bm.copy(Bitmap.Config.ARGB_8888,true);
        bmr=radius*bm.getWidth();
        for(y=0;y<bmr;y++){
            a=(int)(bmr-Math.sqrt(y*(2*bmr-y)));
            for(x=0;x<a;x++){
                bm.setPixel(x,y,color);
            }
        }
        for(y=0;y<bmr;y++){
            a=(int)(bm.getWidth()-bmr+Math.sqrt(y*(2*bmr-y)));
            for(x=a;x<bm.getWidth();x++){
                bm.setPixel(x,y,color);
            }
        }
        for(y=(int)(bm.getHeight()-bmr);y<bm.getHeight();y++){
            a=(int)(bm.getWidth()-bmr+Math.sqrt(Math.pow(bmr,2)-Math.pow(bmr+y-bm.getHeight(),2)));
            for(x=a;x<bm.getWidth();x++){
                bm.setPixel(x,y,color);
            }
        }
        for(y=(int)(bm.getHeight()-bmr);y<bm.getHeight();y++){
            a=(int)(bmr-Math.sqrt(Math.pow(bmr,2)-Math.pow(bmr+y-bm.getHeight(),2)));
            for(x=0;x<a;x++){
                bm.setPixel(x,y,color);
            }
        }
        return bm;
    }
    public Drawable setEffect(Drawable d,float radius,int color){
        return new BitmapDrawable(Resources.getSystem(),setEffect(((BitmapDrawable)d).getBitmap(),radius,color));
    }
}
