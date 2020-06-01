package edu.wit.mobileapp.pulsemonitoringjava;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PatientListAdapter extends ArrayAdapter<Patient> {
    private LayoutInflater mInflater;
    public PatientListAdapter(Context context, int resource, List<Patient> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public View getView (int position, View convertView, final ViewGroup parent) {
        Patient patient = getItem(position);
        View view = mInflater.inflate(R.layout.patient_list_item, null);
        TextView firstName;
        firstName = view.findViewById(R.id.firstName);
        firstName.setText(patient.firstName);

        TextView lastName;
        lastName = view.findViewById(R.id.lastName);
        lastName.setText(patient.lastName);

        TextView DOB;
        DOB = view.findViewById(R.id.DOB);
        DOB.setText(patient.DOB);

        Button diagnoseBtn = view.findViewById(R.id.diagnoseBtn);
        diagnoseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(parent.getContext(), DiagnoseActivity.class);
                parent.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
