import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

import com.example.speedlimitretrofit.R;


public class settingslayout extends Activity {

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the user interface layout for this activity
        setContentView(R.layout.settingslayout);

    }

    //set new tolerance based on user input
    public void tolerance_set(View view) {

        Intent intent = new Intent(this, display_Tolerance.class);
        startActivity(intent);
    }


}

