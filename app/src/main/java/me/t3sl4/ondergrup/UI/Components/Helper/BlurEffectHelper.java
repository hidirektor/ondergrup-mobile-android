package me.t3sl4.ondergrup.UI.Components.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurEffectHelper {

    public static Bitmap blur(Context context, Bitmap image, float radius) {
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        RenderScript rs = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(rs, image);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        theIntrinsic.setRadius(radius); // 0 < radius <= 25
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}