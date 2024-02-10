package algonquin.cst2335.kadv0001;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initializing SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("PhoneNumber", "");

        EditText phoneNumberEditText = findViewById(R.id.phoneNumber);
        phoneNumberEditText.setText(savedPhoneNumber);

        // Initializing ImageView
        profileImage = findViewById(R.id.profileImage);

        // Check if the picture file exists and load it
        String filename = "Picture.png";
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage);
        }

        Intent fromPrevious = getIntent();
        String email = fromPrevious.getStringExtra("EmailAddress");

        Button callButton = findViewById(R.id.callButton);

        TextView welcomeTextView = findViewById(R.id.Welcome);
        welcomeTextView.setText("Welcome back " + email);

        callButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberEditText.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (callIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(callIntent);
            }
        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getExtras() != null) {
                                Bitmap thumbnail = data.getParcelableExtra("data");
                                profileImage.setImageBitmap(thumbnail);

                                FileOutputStream fOut = null;
                                try {
                                    fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fOut != null) {
                                        try {
                                            fOut.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
        Button takePictureButton = findViewById(R.id.ChangePicture);
        takePictureButton.setOnClickListener(v -> cameraResult.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText phoneNumberEditText = findViewById(R.id.phoneNumber);
        String phoneNumber = phoneNumberEditText.getText().toString();

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }}


