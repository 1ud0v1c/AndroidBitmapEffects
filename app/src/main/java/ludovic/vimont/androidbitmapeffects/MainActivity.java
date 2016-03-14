package ludovic.vimont.androidbitmapeffects;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView bitmapView;
    private ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitmapView = (ImageView) findViewById(R.id.bitmapView);
        buttons.add((Button) findViewById(R.id.normal));
        buttons.add((Button) findViewById(R.id.flip));
        buttons.add((Button) findViewById(R.id.grayscale));
        buttons.add((Button) findViewById(R.id.reflection));
        buttons.add((Button) findViewById(R.id.invert));
        buttons.add((Button) findViewById(R.id.blur));
        buttons.add((Button) findViewById(R.id.centerCrop));
        buttons.add((Button) findViewById(R.id.sepia));
        buttons.add((Button) findViewById(R.id.glow));
        buttons.add((Button) findViewById(R.id.rounded_corners));

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Drawable imageDrawable = bitmapView.getDrawable();
        switch (view.getId()) {
            case R.id.normal:
                bitmapView.setImageDrawable(getDrawable(R.drawable.cat));
                break;
            case R.id.flip:
                bitmapView.setImageBitmap(BitmapBuilder.flip(BitmapBuilder.drawableToBitmap(imageDrawable)));
                break;
            case R.id.grayscale:
                bitmapView.setImageBitmap(BitmapBuilder.toGrayscale(BitmapBuilder.drawableToBitmap(imageDrawable), 0.2f));
                break;
            case R.id.invert:
                bitmapView.setImageBitmap(BitmapBuilder.invert(BitmapBuilder.drawableToBitmap(imageDrawable)));
                break;
            case R.id.blur:
                bitmapView.setImageBitmap(BitmapBuilder.blur(getApplicationContext(), BitmapBuilder.drawableToBitmap(imageDrawable)));
                break;
            case R.id.centerCrop:
                bitmapView.setImageBitmap(BitmapBuilder.centerCrop(BitmapBuilder.drawableToBitmap(imageDrawable)));
                break;
            case R.id.sepia:
                bitmapView.setImageBitmap(BitmapBuilder.toSepia(BitmapBuilder.drawableToBitmap(imageDrawable)));
                break;
            case R.id.glow:
                bitmapView.setImageBitmap(BitmapBuilder.glowEffect(BitmapBuilder.drawableToBitmap(imageDrawable), 40, 0xFF25BBEF));
                break;
            case R.id.reflection:
                bitmapView.setImageBitmap(BitmapBuilder.reflectionEffect(BitmapBuilder.drawableToBitmap(imageDrawable), 4));
                break;
            case R.id.rounded_corners:
                bitmapView.setImageBitmap(BitmapBuilder.getRoundedCornerImage(BitmapBuilder.drawableToBitmap(imageDrawable), 50));
                break;
        }
    }
}
