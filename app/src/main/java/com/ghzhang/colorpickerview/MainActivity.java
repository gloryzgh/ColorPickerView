package com.ghzhang.colorpickerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColorPickerView colorPickerView = ((ColorPickerView) findViewById(R.id.pickerView));
        textView = ((TextView) findViewById(R.id.textView));
        colorPickerView.setOnColorSelectListener(new ColorPickerView.ColorSelectListener() {
            @Override
            public void onColorChange(int currentValue) {
                textView.setTextColor(currentValue);
            }
        });
    }
}
