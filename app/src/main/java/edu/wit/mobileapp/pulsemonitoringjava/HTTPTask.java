package edu.wit.mobileapp.pulsemonitoringjava;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class HTTPTask extends AsyncTask<String, Void, String> {
    private String TAG = "myTag";
    private WeakReference<Context> mContext;
    private String method;
    private ArrayList<Patient> dataList;
    HTTPTask(Context context, String method, ArrayList<Patient> dataList) {
        mContext = new WeakReference<>(context);
        this.method = method;
        this.dataList = dataList;
    }
    @Override
    protected String doInBackground(String... params) {
        JSONObject requestConfig = new JSONObject();
        try {
            requestConfig = new JSONObject(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (method.equals("GET")){
            return doGet(requestConfig);
        } else if (method.equals("POST")) {
            return doPost(requestConfig);
        } else {
            return "";
        }
    }

    private HttpURLConnection connect(String urlString) throws IOException{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (this.method.equals("POST")) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
            } else if (this.method.equals("GET")) {
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
            }
            return urlConnection;
    }

    private String doGet(JSONObject requestConfig) {
        String result = "";
        try {
            String urlString = requestConfig.getString("urlString");
            HttpURLConnection urlConnection = connect(urlString);
            result = readResponse(urlConnection);
        } catch (Exception e) {

            Log.e(TAG, "Error: " + e.toString());
        }
        return result;
    }

    private String doPost(JSONObject requestConfig){
        String TAG = "myTag";
        Log.v(TAG, "doInBackground is called");
        String result = "";
        try {
            String urlString = requestConfig.getString("urlString");
            JSONObject jsonData = requestConfig.getJSONObject("data");
            HttpURLConnection urlConnection = connect(urlString);
            streamDataToServer(urlConnection, jsonData);
            result = readResponse(urlConnection);
            Log.v(TAG, "Finished transferring data");
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    private void streamDataToServer (HttpURLConnection urlConnection, JSONObject jsonData) throws IOException {
        urlConnection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.addRequestProperty("Content-Length", Integer.toString(jsonData.toString().length()));
        OutputStream out = urlConnection.getOutputStream();
        out.write(
                jsonData.toString().getBytes(StandardCharsets.UTF_8),
                0,
                jsonData.toString().getBytes(StandardCharsets.UTF_8).length
        );
    }

    private String readResponse (HttpURLConnection urlConnection) throws IOException {
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String read;
        while((read = br.readLine())!=null){
            sb.append(read);
        }
        br.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String data){
        super.onPostExecute(data);
        Log.v(TAG, data);
        try {
            if (this.method.equals("GET")){
                JSONObject response = new JSONObject(data);
                JSONArray patientsJSONArray = response.getJSONArray("patients");
                for (int i = 0; i < patientsJSONArray.length(); i++) {
                    String firstName = patientsJSONArray.getJSONObject(i).getString("firstName");
                    String lastName = patientsJSONArray.getJSONObject(i).getString("lastName");
                    String DOB = patientsJSONArray.getJSONObject(i).has("DOB") ? patientsJSONArray.getJSONObject(i).getString("DOB"): "";
                    Patient currPatient = new Patient(firstName, lastName, DOB);
                    dataList.add(currPatient);
                }
                Activity activity = (Activity) mContext.get();
                PatientListAdapter adapter = new PatientListAdapter(activity, 0, dataList);
                ListView patientList = activity.findViewById(R.id.ListView01);
                patientList.setAdapter(adapter);
            } else {
                Activity activity = (Activity) mContext.get();
                activity.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
