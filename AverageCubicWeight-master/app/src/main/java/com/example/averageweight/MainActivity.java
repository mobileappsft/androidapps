package com.example.averageweight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String PRIMARY_URL = "http://wp8m3he1wt.s3-website-ap-southeast-2.amazonaws.com";
    String INITIAL_API = "/api/products/1";

    Double[] weightsArray = new Double[10000000];
    int numOfAirConditioners = 0;

    public void asyncTaskExcutor(String urlPart){
        DownloadTask task = new DownloadTask();
        String executingURL = PRIMARY_URL+urlPart;
        task.execute(executingURL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asyncTaskExcutor(INITIAL_API);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String nextURL="";
            String objectsInfo ="";

            try {
                JSONObject jsonObject = new JSONObject(result);
                objectsInfo = jsonObject.getString("objects");
                nextURL = jsonObject.getString("next");

                    JSONArray arr = new JSONArray(objectsInfo);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonPart = arr.getJSONObject(i);
                        if(jsonPart.getString("category").equals("Air Conditioners")) {
                            String size = jsonPart.getString("size");
                            JSONObject dimensions = new JSONObject(size);
                            Double weight = ((dimensions.getDouble("width")/100) * (dimensions.getDouble("length")/100)*(dimensions.getDouble("height")/100))*250;
                            weightsArray[numOfAirConditioners]=weight;
                            numOfAirConditioners++;
                        }
                    }

                    if(nextURL.equals("null")) {
                        Double totalWeight=0.0;
                        for(int i =0; i<numOfAirConditioners; i++) {
                            totalWeight = totalWeight+weightsArray[i];
                        }
                        Double average = totalWeight/numOfAirConditioners;
                        String finalResult = String.format("%.2f kg", average);
                        TextView myAwesomeTextView = (TextView)findViewById(R.id.averageWeightTextView);
                        myAwesomeTextView.setText(finalResult);
                        Log.i("FinalResult", finalResult);
                    }
                    else{
                        asyncTaskExcutor(nextURL);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
