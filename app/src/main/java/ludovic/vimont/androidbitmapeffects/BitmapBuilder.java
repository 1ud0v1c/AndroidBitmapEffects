package ludovic.vimont.androidbitmapeffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;

import java.nio.IntBuffer;

public class BitmapBuilder {
    private static final float BITMAP_SCALE = 1.0f;
    private static final float BLUR_RADIUS = 15.0f;
    private static final int RGB_MASK = 0x00FFFFFF;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Bitmap centerCrop(Bitmap original) {
        int x = original.getWidth()/2 - original.getWidth()/4;
        int y = original.getHeight()/2 - original.getHeight()/4;

        int width  = original.getWidth() - original.getWidth()/3;
        int height = original.getHeight() - original.getHeight()/2;

        return Bitmap.createBitmap(original, x, y, width, height);
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap, float cornersSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, cornersSize, cornersSize, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap source) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap destination = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
        destination.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return destination;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap invert(Bitmap original) {
        Bitmap inversion = original.copy(Bitmap.Config.ARGB_8888, true);

        int width = inversion.getWidth();
        int height = inversion.getHeight();
        int pixels = width * height;

        int[] pixel = new int[pixels];
        inversion.getPixels(pixel, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels; i++) {
            pixel[i] ^= RGB_MASK;
        }
        inversion.setPixels(pixel, 0, width, 0, 0, width, height);

        return inversion;
    }

    public static Bitmap toGrayscale(Bitmap original, float saturation) {
        int width = original.getWidth();
        int height = original.getHeight();

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(original, 0, 0, paint);

        return out;
    }

    public static Bitmap toSepia(Bitmap original) {
        int depth = 20;
        int red, green, blue, pixel;
        int height = original.getHeight();
        int width = original.getWidth();

        Bitmap sepia = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width * height];

        original.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            pixel = pixels[i];

            red = (pixel >> 16) & 0xFF;
            green = (pixel >> 8) & 0xFF;
            blue = pixel & 0xFF;

            red = green = blue = (red + green + blue) / 3;
            red += (depth * 2);
            green += depth;

            if (red > 255) red = 255;
            if (green > 255) green = 255;
            pixels[i] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
        }
        sepia.setPixels(pixels, 0, width, 0, 0, width, height);
        return sepia;
    }

    public static Bitmap glowEffect(Bitmap original, int glowRadius, int glowColor) {
        int margin = 24;
        int halfMargin = margin / 2;

        Bitmap alpha = original.extractAlpha();
        Bitmap out =  Bitmap.createBitmap(original.getWidth() + margin, original.getHeight() + margin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);

        Paint paint = new Paint();
        paint.setColor(glowColor);

        // Outer glow, For Inner glow set Blur.INNER
        paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER));
        canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);
        canvas.drawBitmap(original, halfMargin, halfMargin, null);
        alpha.recycle();

        return out;
    }


    public static Bitmap reflectionEffect(Bitmap original, int reflectionGap) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(original, 0, height/2, width, height/2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(original, 0, 0, null);

        Paint deafaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, original.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);

        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }


    public static Bitmap sketchEffect(Context context, Bitmap original) {
        Bitmap grayScaleBitamp = BitmapBuilder.toGrayscale(original, 0.0f);
        Bitmap invertedBitmap = BitmapBuilder.invert(grayScaleBitamp);
        Bitmap blurInvertedBitmap = BitmapBuilder.blur(context,  invertedBitmap);
        return BitmapBuilder.ColorDodgeBlend(blurInvertedBitmap, grayScaleBitamp);
    }

    private static int colordodge(int in1, int in2) {
        float image = (float)in2;
        float mask = (float)in1;
        return ((int) ((image == 255) ? image:Math.min(255, (((long)mask << 8 ) / (255 - image)))));

    }

    public static Bitmap ColorDodgeBlend(Bitmap source, Bitmap layer) {
        Bitmap base = source.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap blend = layer.copy(Bitmap.Config.ARGB_8888, false);

        IntBuffer buffBase = IntBuffer.allocate(base.getWidth() * base.getHeight());
        base.copyPixelsToBuffer(buffBase);
        buffBase.rewind();

        IntBuffer buffBlend = IntBuffer.allocate(blend.getWidth() * blend.getHeight());
        blend.copyPixelsToBuffer(buffBlend);
        buffBlend.rewind();

        IntBuffer buffOut = IntBuffer.allocate(base.getWidth() * base.getHeight());
        buffOut.rewind();

        while (buffOut.position() < buffOut.limit()) {
            int filterInt = buffBlend.get();
            int srcInt = buffBase.get();

            int redValueFilter = Color.red(filterInt);
            int greenValueFilter = Color.green(filterInt);
            int blueValueFilter = Color.blue(filterInt);

            int redValueSrc = Color.red(srcInt);
            int greenValueSrc = Color.green(srcInt);
            int blueValueSrc = Color.blue(srcInt);

            int redValueFinal = BitmapBuilder.colordodge(redValueFilter, redValueSrc);
            int greenValueFinal = BitmapBuilder.colordodge(greenValueFilter, greenValueSrc);
            int blueValueFinal = BitmapBuilder.colordodge(blueValueFilter, blueValueSrc);

            int pixel = Color.argb(255, redValueFinal, greenValueFinal, blueValueFinal);
            buffOut.put(pixel);
        }

        buffOut.rewind();
        base.copyPixelsFromBuffer(buffOut);
        blend.recycle();

        return base;
    }
}