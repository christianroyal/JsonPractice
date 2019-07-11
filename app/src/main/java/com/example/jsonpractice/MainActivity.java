package com.example.jsonpractice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noname.homeworkasync.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ShibeAdapter shibeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpURLConnection httpURLConnection;
        recyclerView= findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        new ShibeTask().execute("10");
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    class ShibeTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... strings) {
            String baseUrl = "http://shibe.online/api/shibes";
            String query = "?count=" + strings[0];
            StringBuilder result = new StringBuilder();
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(baseUrl + query);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();

            }

            Log.d(TAG, "doInBackground: " + result);

            //convert String(Json) into List<String>
            String removeBrackets= result.substring(1,result.length()-1);
            String removeQuotes= removeBrackets.replace("\"","");
            String [] urls= removeQuotes.split(",");





            return Arrays.asList(urls);
        }

        @Override
        protected void onPostExecute(List<java.lang.String> strings) {
            super.onPostExecute(strings);
            shibeAdapter= new ShibeAdapter(strings);
            recyclerView.setAdapter(shibeAdapter);
        }
    }
}
