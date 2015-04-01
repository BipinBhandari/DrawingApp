package bipin.drawingapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by view9 on 3/29/15.
 */
public class ColorPickerDialog extends DialogFragment{
    @InjectView(R.id.picker)
    ColorPicker picker;
    @InjectView(R.id.svbar)
    SVBar svBar;
    @InjectView(R.id.opacitybar)
    OpacityBar opacityBar;
    @InjectView(R.id.saturationbar)
    SaturationBar saturationBar;
    @InjectView(R.id.valuebar)
    ValueBar valueBar;

    private ColorPicker.OnColorChangedListener colorChangedListener;

    public void setColorChangedListener(ColorPicker.OnColorChangedListener colorChangedListener){
        this.colorChangedListener=colorChangedListener;
        if (picker!=null) picker.setOnColorChangedListener(colorChangedListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_color_picker, container, false);
        ButterKnife.inject(this, view);
        initialize();
        return view;
    }

    private void initialize(){
        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        if (colorChangedListener!=null){
            picker.setOnColorChangedListener(colorChangedListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
