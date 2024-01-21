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
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        model = new ViewModelProvider(this).get(MainViewModel.class);

        TextView textView = variableBinding.textview;
        Button myButton = variableBinding.myButton;
        EditText myEdit = variableBinding.myedittext;

        myButton.setOnClickListener(v -> {
            String editString = myEdit.getText().toString();
            textView.setText("Your edit text has : " + editString);
        });
    }
}