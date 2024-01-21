package algonquin.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import algonquin.cst2335.kadv0001.databinding.ActivityMainBinding;
import algonquin.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        // Initialize ViewModel
        model = new ViewModelProvider(this).get(MainViewModel.class);

        // Observing the MutableLiveData from ViewModel
        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has " + s);
        });

        // Set OnClickListener for the button
        variableBinding.myButton.setOnClickListener(v -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
    }
}