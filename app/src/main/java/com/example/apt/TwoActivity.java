package com.example.apt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.annotation.Arouter;

@Arouter(path="two")
public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
}
