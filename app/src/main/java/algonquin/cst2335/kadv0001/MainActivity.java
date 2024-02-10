package algonquin.cst2335.kadv0001;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        // Initializing SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        // Get email EditText, setting value from SharedPreferences
        EditText emailEditText = findViewById(R.id.TextEmailAddress);
        String savedEmail = prefs.getString("LoginName", "");
        emailEditText.setText(savedEmail);

        // OnClickListener
        Button loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(v -> {
            // Geting email address from EditText
            String email = emailEditText.getText().toString();

            // Saving email address to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", email);
            editor.apply();

            // Start SecondActivity
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", email);
            startActivity(nextPage);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart() - The application is now visible on screen.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume() - The application is now responding to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop() - The application is no longer visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy() - Any memory used by the application is freed.");
    }
}