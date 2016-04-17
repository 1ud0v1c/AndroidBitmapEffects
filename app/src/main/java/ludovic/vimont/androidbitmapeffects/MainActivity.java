package ludovic.vimont.androidbitmapeffects;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int GET_FROM_GALLERY = 3;
    private ImageView bitmapView;
    private Drawable currentImage;
    private ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentImage = getDrawable(R.drawable.cat);
        bitmapView = (ImageView) findViewById(R.id.bitmapView);
        bitmapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

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
        buttons.add((Button) findViewById(R.id.cartoon));

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                currentImage = new BitmapDrawable(getResources(), bitmap);
                bitmapView.setImageDrawable(currentImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBitmap(String methodToExecute) {
        BitmapWorkerTask task = new BitmapWorkerTask(getApplicationContext(), bitmapView);
        task.execute(methodToExecute);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal:
                bitmapView.setImageDrawable(currentImage);
                break;
            case R.id.flip:
                loadBitmap("flip");
                break;
            case R.id.grayscale:
                loadBitmap("toGrayscale");
                break;
            case R.id.invert:
                loadBitmap("invert");
                break;
            case R.id.blur:
                loadBitmap("blur");
                break;
            case R.id.centerCrop:
                loadBitmap("centerCrop");
                break;
            case R.id.sepia:
                loadBitmap("sepia");
                break;
            case R.id.glow:
                loadBitmap("glow");
                break;
            case R.id.reflection:
                loadBitmap("reflection");
                break;
            case R.id.rounded_corners:
                loadBitmap("rounded_corners");
                break;
            case R.id.cartoon:
                loadBitmap("cartoon");
                break;
        }
    }
}
