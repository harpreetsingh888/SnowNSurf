package com.example.harpreet.snownsurf;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by harpreet on 10/06/16.
 */
public class JsonHandling extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        String returnString = "";

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            returnString = stringBuffer.toString();
            return returnString;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
