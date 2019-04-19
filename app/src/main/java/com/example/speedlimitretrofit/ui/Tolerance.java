package com.example.speedlimitretrofit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import com.example.speedlimitretrofit.R;

public class Tolerance extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tolerance_input);
    }

    //set tolerance
    public void setTol()
    {
        //retrieve range of tolerance that was entered
        EditText thresh = findViewById(R.id.thresh);

    }
}
