package edu.wit.mobileapp.pulsemonitoringjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity {
    private ArrayList<Patient> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        FloatingActionButton addPatientBtn = findViewById(R.id.addPatientBtn);

        JSONObject requestConfig = new JSONObject();
        try {
            requestConfig.put("urlString", "http://165.227.254.178:4000/patients");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HTTPTask getPatients = new HTTPTask(this, "GET", dataList);
        getPatients.execute(requestConfig.toString());

        addPatientBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PatientListActivity.this, CreatePatient.class);
                startActivity(intent);
            }
        });
    }
}
