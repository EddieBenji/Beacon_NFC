package com.example.user.location;

/**
 * Created by Josafat on 19/04/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackgroundTask extends AsyncTask<String, Void, String> {


    String get_url = "http://www.javascriptkit.com/dhtmltutors/javascriptkit.json";

    Context ctx;
    Activity activity;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private String jsonInfo = "{\"title\":\"javascriptkit.com\",\"link\":\"http:\\/\\/www.javascriptkit.com\",\"description\":\"JavaScript tutorials and over 400+ free scripts!\",\"language\":\"en\",\"items\":[{\"title\":\"Document Text Resizer\",\"link\":\"http:\\/\\/www.javascriptkit.com\\/script\\/script2\\/doctextresizer.shtml\",\"description\":\"This script adds the ability for your users to toggle your webpage's font size, with persistent cookies then used to remember the setting\"},{\"title\":\"JavaScript Reference- Keyboard\\/ Mouse Buttons Events\",\"link\":\"http:\\/\\/www.javascriptkit.com\\/jsref\\/eventkeyboardmouse.shtml\",\"description\":\"The latest update to our JS Reference takes a hard look at keyboard and mouse button events in JavaScript, including the unicode value of each key.\"},{\"title\":\"Dynamically loading an external JavaScript or CSS file\",\"link\":\"http:\\/\\/www.javascriptkit.com\\/javatutors\\/loadjavascriptcss.shtml\",\"description\":\"External JavaScript or CSS files do not always have to be synchronously loaded as part of the page, but dynamically as well. In this tutorial, see how.}";

    public BackgroundTask(Context ctx) {
        this.ctx = ctx;
        activity = (Activity) ctx;
    }


    @Override
    protected void onPreExecute() {
        builder = new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Please hold on second..");
        progressDialog.setMessage("Connecting you to server");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];

        if (method.equals("do_get")) {
            try {
                URL url = new URL(get_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;


                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");

                httpURLConnection.disconnect();
                Thread.sleep(5000);

                return stringBuilder.toString().trim();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {
        try {
            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(json);
            this.jsonInfo = jsonObject.toString();
            Log.i("JSON: ", this.jsonInfo);

//            JSONArray jsonArray = jsonObject.getJSONArray("title");
//            JSONObject JO = jsonArray.getJSONObject(0);
//            String code = JO.getString("code");
//            String message = JO.getString("message");

//
//            if (code.equals("reg_true")) {
//                showDialogue("Registration was successful!", message, code);
//            } else if (code.equals("reg_false")) {
//                showDialogue("Registration failed", message, code);
//            } else if (code.equals("login_true")) {
//                Intent intent = new Intent(activity, MenuActivity.class);
//                intent.putExtra("message", message);
//                activity.startActivity(intent);
//            } else if (code.equals("login_false")) {
//                showDialogue("Login error", message, code);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showDialogue(String title, String message, String code)

    {
        builder.setTitle(title);

        if (code.equals("reg_true") || code.equals("reg_false")) {

            builder.setMessage(message);
            builder.setPositiveButton("Alright", new DialogInterface.OnClickListener() {
                @Override

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (code.equals("login_false")) {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText email, password;
                    dialog.dismiss();
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public String getJsonInfo() {
        return jsonInfo;
    }
}

