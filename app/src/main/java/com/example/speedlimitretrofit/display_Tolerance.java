package com.example.speedlimitretrofit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import com.example.speedlimitretrofit.R;

public class display_Tolerance extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tolerance_input);
    }

    //set tolerance
    private void setTol()
    {
        //retrieve range of tolerance that was entered
        EditText lowThresh = findViewById(R.id.lowThresh);
        EditText highThresh = findViewById(R.id.highThresh);
        String low = lowThresh.getText().toString();
        String high = highThresh.getText().toString();

        if(low.equals("") || high.equals(""))
        {
            //display message please put in valid range
        }
        int lowT = Integer.parseInt(low);
        int highT = Integer.parseInt(high);

        if(lowT < 0 || lowT > 70 || highT < 5 || highT > 100)
        {
            //display infeasible values
        }
        else if (lowT > highT || lowT == highT)
        {
            //display error
        }

        //set tolerance using lowT and highT
    }
}
