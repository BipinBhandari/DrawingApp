package bipin.drawingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.larswerkman.holocolorpicker.ColorPicker;

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

    @InjectView(R.id.brushSizeSeekbar)
    SeekBar brushSeekbar;

    private int prevColor;

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmapBg=Bitmap.createBitmap(drawingSurfaceBackground.getDrawingCache());
//                Drawable drawableBg=new BitmapDrawable(getApplicationContext().getResources(),bitmapBg);

                Bitmap bitmapSurface=Bitmap.createBitmap(drawingSurface.getDrawingCache());
//                Drawable drawableSurface=new BitmapDrawable(getApplicationContext().getResources(),bitmapSurface);

//                Bitmap bitmapClone = Bitmap.createBitmap(bitmapBg.getWidth(), bitmapBg.getHeight(), bitmapBg.getConfig());
//                Canvas c = new Canvas(bitmapClone);
//                c.drawBitmap(bitmapBg, new Matrix(), null);
//                c.drawBitmap(bitmapClone, 0,0, null);
//                drawableBg.draw(c);
//                drawableSurface.draw(c);

                imagePreview.setVisibility(View.VISIBLE);
                drawingSurface.setVisibility(View.GONE);

                drawingSurfaceBackground.setVisibility(View.GONE);
                imagePreview.setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), overlay(bitmapBg, bitmapSurface)));
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
