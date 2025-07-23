package com.example.store.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.store.R;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private EditText etFullName;
    private Spinner spinnerColor;
    RadioGroup radio_group;
    List<TextView> all_textViews;
    private Button btnSave;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Αρχικοποίηση Views
        etFullName = findViewById(R.id.etFullName);
        spinnerColor = findViewById(R.id.spinnerColor);
        radio_group = findViewById(R.id.radioGroup);
        btnSave = findViewById(R.id.btnSave);
        backBtn = findViewById(R.id.imageBackBtn);

        all_textViews = List.of(
                findViewById(R.id.FS_textview),
                findViewById(R.id.BC_textview)
        );

        // Φόρτωση αποθηκευμένων προτιμήσεων
        loadPreferences();

        // Αποθήκευση όταν πατηθεί το κουμπί
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Αποθήκευση Ονοματεπώνυμου
        editor.putString("FullName", etFullName.getText().toString());

        // Αποθήκευση χρώματος
        editor.putString("BackgroundColor", spinnerColor.getSelectedItem().toString());

        // Αποθήκευση μεγέθους γραμματοσειράς
        int radio_id = radio_group.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = radio_group.findViewById(radio_id);
        String text_size  = selectedRadioButton.getText().toString();
        editor.putString("FontSize", text_size);

        editor.apply(); // Εφαρμογή αλλαγών
        loadPreferences();

        Toast.makeText(this, "Your settings have been saved", Toast.LENGTH_SHORT).show();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Φόρτωση Ονοματεπώνυμου
        etFullName.setText(sharedPreferences.getString("FullName", ""));

        // Φόρτωση επιλογής φόντου
        String backgroundColor = sharedPreferences.getString("BackgroundColor", "Pink");
        ScrollView scrollView = findViewById(R.id.scrollView);
        switch (backgroundColor) {
            case "Pink":
                spinnerColor.setSelection(0);
                scrollView.setBackgroundResource(R.drawable.background_pink);
                break;
            case "Blue":
                spinnerColor.setSelection(1);
                scrollView.setBackgroundResource(R.drawable.background_blue);
                break;
            case "Green":
                spinnerColor.setSelection(2);
                scrollView.setBackgroundResource(R.drawable.background_green);
                break;
        }

        // Φόρτωση επιλογής μεγέθους γραμματοσειράς
        String text_size = sharedPreferences.getString("FontSize","Medium");
        int size_value = 14;
        switch (text_size) {
            case "Small":
                size_value = 10;
                radio_group.check(R.id.radioButton);
                break;
            case "Medium":
                //size_value = 14;
                radio_group.check(R.id.radioButton2);
                break;
            case "Big":
                size_value = 20;
                radio_group.check(R.id.radioButton3);
                break;
        }
        for (TextView textView : all_textViews) {
            textView.setTextSize(size_value);
        }

    }

    private int getIndexFromSpinner(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // Default επιλογή
    }
}