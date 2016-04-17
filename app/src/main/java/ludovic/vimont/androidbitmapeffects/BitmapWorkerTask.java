package ludovic.vimont.androidbitmapeffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private Context context;
    private String methodName = "";
    private Drawable currentImage;
    private final WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(Context context, ImageView imageView) {
        this.context = context;
        imageViewReference = new WeakReference<>(imageView);
        currentImage = imageView.getDrawable();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        methodName = params[0];
        switch (methodName) {
            case "flip":
                return BitmapBuilder.flip(BitmapBuilder.drawableToBitmap(currentImage));
            case "toGrayscale":
                return BitmapBuilder.toGrayscale(BitmapBuilder.drawableToBitmap(currentImage), 0.2f);
            case "invert":
                return BitmapBuilder.invert(BitmapBuilder.drawableToBitmap(currentImage));
            case "blur":
                return BitmapBuilder.blur(context, BitmapBuilder.drawableToBitmap(currentImage));
            case "centerCrop":
                return BitmapBuilder.centerCrop(BitmapBuilder.drawableToBitmap(currentImage));
            case "sepia":
                return BitmapBuilder.toSepia(BitmapBuilder.drawableToBitmap(currentImage));
            case "glow":
                return BitmapBuilder.glowEffect(BitmapBuilder.drawableToBitmap(currentImage), 40, 0xFF25BBEF);
            case "reflection":
                return BitmapBuilder.reflectionEffect(BitmapBuilder.drawableToBitmap(currentImage), 4);
            case "rounded_corners":
                return BitmapBuilder.getRoundedCornerImage(BitmapBuilder.drawableToBitmap(currentImage), 50);
            case "cartoon":
                return BitmapBuilder.sketchEffect(context, BitmapBuilder.drawableToBitmap(currentImage));
            default:
                return BitmapBuilder.drawableToBitmap(currentImage);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}