package edu.wit.mobileapp.pulsemonitoringjava;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class CreatePatient extends Activity {
    private static String TAG="myTag";
    TextView firstNameTextView, lastNameTextView, DOBTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_patient_layout);
        firstNameTextView = findViewById(R.id.firstName);
        lastNameTextView = findViewById(R.id.lastName);
        DOBTextView = findViewById(R.id.DOB);
        Button createPatientBtn = findViewById(R.id.createPatientBtn);
        Button createPatientCancelBtn = findViewById(R.id.patientCreationCancelBtn);

        createPatientBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                HTTPTask task = new HTTPTask(CreatePatient.this, "POST", new ArrayList<Patient>());
                JSONObject dataToSend = new JSONObject();
                JSONObject requestConfig = new JSONObject();
                try {
                    dataToSend.put("firstName", firstNameTextView.getText().toString());
                    dataToSend.put("lastName", lastNameTextView.getText().toString());
                    dataToSend.put("DOB", DOBTextView.getText().toString());
                    requestConfig.put("data", dataToSend);
                    requestConfig.put("urlString", "http://165.227.254.178:4000/patients");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                task.execute(requestConfig.toString());
            }
        });

        createPatientCancelBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
