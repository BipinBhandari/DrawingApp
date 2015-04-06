package bipin.drawingapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import customviews.DrawingSurface;

public class MainActivity extends Activity {
    @InjectView(R.id.drawing_surface) DrawingSurface drawingSurface;
    @InjectView(R.id.btnChangeColor) Button btnChangeColor;
    @InjectView(R.id.btnSave) Button btnSave;
    @InjectView(R.id.drawing_surface_background)
    FrameLayout drawingSurfaceBackground;
    @InjectView(R.id.eraser)
    ToggleButton btnEraser;
    @InjectView(R.id.image_preview)
    ImageView imagePreview;

    @InjectView(R.id.choosePhoto)
    Button btnChoosePhoto;

    @InjectView(R.id.brushSizeSeekbar)
    SeekBar brushSeekbar;

    private int prevColor;
    private Bitmap bitmap;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        changeColor(Color.GREEN);

        drawingSurfaceBackground.setDrawingCacheEnabled(true);
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialog colorPickerDialog=new ColorPickerDialog();
                colorPickerDialog.show(getFragmentManager(), "ColorPicker");
                colorPickerDialog.setColorChangedListener(new ColorPicker.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int i) {
                        changeColor(i);
                    }
                });
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmapBg=Bitmap.createBitmap(drawingSurfaceBackground.getDrawingCache());
                Bitmap bitmapSurface=Bitmap.createBitmap(drawingSurface.getDrawingCache());
                imagePreview.setVisibility(View.VISIBLE);
                drawingSurface.setVisibility(View.GONE);
                drawingSurfaceBackground.setVisibility(View.GONE);
                Bitmap finalBitmap = overlay(bitmapBg, bitmapSurface);
                imagePreview.setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), finalBitmap));

                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                Random random=new Random();
                File file = new File(extStorageDirectory, "ic_launcher" + random.nextInt() + ".PNG");
                try {
                    FileOutputStream outputStream=new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(MainActivity.this, "File saved", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnEraser.setChecked(false);
        btnEraser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                drawingSurface.setErase(on);
            }
        });

        brushSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                drawingSurface.setStrokeWidth(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                drawingSurfaceBackground.setBackground(new BitmapDrawable(getResources(), bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);


    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    private void changeColor(int brushColor){
        drawingSurface.setDrawingColor(brushColor);
        btnChangeColor.setBackgroundColor(brushColor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
