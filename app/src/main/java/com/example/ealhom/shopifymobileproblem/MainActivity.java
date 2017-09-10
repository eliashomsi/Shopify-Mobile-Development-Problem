package com.example.ealhom.shopifymobileproblem;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView output = null;
    String json = null;

    // This is the async class that is used for network
    public class OkHttpHandler  extends AsyncTask {
        OkHttpClient client = new OkHttpClient();



        @Override
        protected void onPostExecute(Object s) {
            super.onPostExecute(s);
            output.setText(s.toString());
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Object doInBackground(Object[] objects) {
            double napelonPrice = 0;
            int bronzeBags = 0;
            String url = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                // code everything here
                String responseString = response.body().string();
                JSONObject object = new JSONObject(responseString);
                JSONArray orders = object.getJSONArray("orders");
                for (int i = 0; i < orders.length(); i++) {
                    JSONObject order = orders.getJSONObject(i);
                    if(order.has("customer") && order.getJSONObject("customer") != null) {
                        JSONObject customer = order.getJSONObject("customer");
                        String first_name = customer.getString("first_name") + " ";
                        String last_name = customer.getString("last_name") + " ";
                        if(first_name.trim().toLowerCase().equals("napoleon") && last_name.trim().toLowerCase().equals("batz")) {
                            napelonPrice += order.getDouble("total_price");
                        }
                    }

                    if(order.has("line_items")) {
                        JSONArray items = order.getJSONArray("line_items");
                        for(int k=0; k< items.length(); k++) {
                            JSONObject item = items.getJSONObject(k);
                            if(item.getString("title").trim().toLowerCase().equals("awesome bronze bag")) {
                                bronzeBags+= item.getInt("quantity");
                            }
                        }
                    }

                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }

            String result =  "\nThe total amount spent by Napoleon Batz is " + napelonPrice + "\n the total number of Awesome Bronze Bags sold is " + bronzeBags + "\n";
            return result;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define connections
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        output = (TextView) findViewById(R.id.output_application);

        Button start=  (Button) findViewById(R.id.button2);

        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String url = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
                OkHttpHandler  handler = new OkHttpHandler ();
                AsyncTask response = handler.execute(url);
                output.setText("done sending the request");
            }
        });
       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
