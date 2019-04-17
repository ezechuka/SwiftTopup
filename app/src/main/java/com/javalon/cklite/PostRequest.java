package com.javalon.cklite;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
// = "https://www.nellobytesystems.com/APIBuyAirTime.asp?";
public class PostRequest {
    private static JSONObject baseParamsObj;
    private static JSONObject queryParamsObj;
    private static String baseURL;
    private static String queryURL;
    private static Context context;
    public static String orderID = "";
    private static String status = "";
    private static ProgressDialog requestDialog;
    private static String[] statusArray = {"INSUFFICIENT_APIBALANCE"};
    private static final String TAG = "CKLITE";
    private static JSONObject queryResponse;

    // default constructor
    public PostRequest() { }

    public PostRequest(String baseURL, String queryURL, JSONObject baseParamsObj, JSONObject queryParamsObj, Context context) {
        PostRequest.baseURL = baseURL;
        PostRequest.queryURL = queryURL;
        PostRequest.baseParamsObj = baseParamsObj;
        PostRequest.queryParamsObj = queryParamsObj;
        this.context = context;
    }

    public JSONObject getQueryParamsObj() {
        return queryParamsObj;
    }

    public void setQueryParamsObj(JSONObject queryParamsObj) {
        this.queryParamsObj = queryParamsObj;
    }

    public JSONObject getBaseParams() {
        return baseParamsObj;
    }

    public void setBaseParams(JSONObject baseParamsObj) {
        PostRequest.baseParamsObj = baseParamsObj;
    }

    public JSONObject getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(JSONObject queryResponse) {
        PostRequest.queryResponse = queryResponse;
    }

    public void buy() {
        new SendRequest().execute();
    }

    private static class SendRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            requestDialog = new ProgressDialog(context);
            requestDialog.setTitle("Please wait...");
            requestDialog.setMessage("Sending request");
            requestDialog.setCancelable(false);
            requestDialog.show();
        }

        @Override
        protected String doInBackground(Void... aVoid) {
            try {
                URL baseURL = new URL(PostRequest.baseURL);
                HttpsURLConnection reqConn = (HttpsURLConnection) baseURL.openConnection();
                reqConn.setConnectTimeout(15000);
                reqConn.setReadTimeout(20000);
                reqConn.setRequestMethod("POST");
                reqConn.setDoInput(true);
                reqConn.setDoOutput(true);

                // send request to the API
                OutputStream os = reqConn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(getPostDataString(baseParamsObj));
                bw.flush();
                bw.close();

                // read request
                int responseCode = reqConn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(reqConn.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    isr.close();
                    reqConn.disconnect();
                    String responseValue = sb.toString();
                    JSONObject responseObj = new JSONObject(responseValue);
                    status = (String) responseObj.get("status");
                    Log.e(TAG, status);
                    if (status.equals("INSUFFICIENT_APIBALANCE")) {
                        return status;
                    }
                    status = (String) responseObj.get("orderid");
                    Log.e(TAG, "Post Request1: " + responseObj.toString());
                    queryParamsObj.put("OrderID", orderID);

                    // send query request to API
                    Log.e(TAG, "Post Request2: " + queryParamsObj.toString());
                    URL queryConnURL = new URL(queryURL);
                    HttpsURLConnection queryConn = (HttpsURLConnection) queryConnURL.openConnection();
                    queryConn.setReadTimeout(15000);
                    queryConn.setConnectTimeout(20000);
                    queryConn.setDoInput(true);
                    queryConn.setDoOutput(true);
                    queryConn.setRequestMethod("POST");
                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new OutputStreamWriter(queryConn.getOutputStream(), "UTF-8"));
                    bufferedWriter.write(getPostDataString(queryParamsObj));
                    Log.e(TAG, "Post data: " + getPostDataString(queryParamsObj));
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    // read request from query request
                    InputStreamReader inputStreamReader = new InputStreamReader(queryConn.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String value;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((value = bufferedReader.readLine()) != null) {
                        stringBuilder.append(value);
                    }

                    inputStreamReader.close();
                    bufferedReader.close();
                    queryConn.disconnect();
                    return stringBuilder.toString();
                }

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                jsonException.getMessage();
            } catch (UnsupportedEncodingException usee) {
                usee.printStackTrace();
                usee.getMessage();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                ioException.getMessage();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            requestDialog.dismiss();
            Log.e(TAG, "onPostExecute: " + s);
            if ((!status.isEmpty()) && (status.equals("INSUFFICIENT_APIBALANCE"))) {
                new MaterialStyledDialog.Builder(context)
                        .setTitle("Oops!")
                        .setDescription("Insufficient fund :-( ")
                        .setHeaderDrawable(R.drawable.all_networks)
                        .setPositiveText("GOT IT!")
                        .withDivider(true)
                        .build()
                        .show();
            } else if ((!s.isEmpty()) && (!s.equals("INSUFFICIENT_APIBALANCE"))) {
                try {
                    queryResponse = new JSONObject(s);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                    jsonException.getMessage();
                }
            } else {
                new MaterialStyledDialog.Builder(context)
                        .setTitle("Oops!")
                        .setDescription("There may be something wrong with your data connection :-( ")
                        .setHeaderDrawable(R.drawable.all_networks)
                        .setPositiveText("GOT IT!")
                        .withDivider(true)
                        .withDialogAnimation(true, Duration.SLOW)
                        .setCancelable(false)
                        .build()
                        .show();
            }
        }

        private String getPostDataString(JSONObject params) throws JSONException, UnsupportedEncodingException {
            StringBuilder stringBuilder = new StringBuilder();
            boolean first = true;

            Iterator<String> keys = params.keys();
            while (keys.hasNext()) {
                String key = keys.next();   // get the key
                Object value = params.get(key); // get the value of the key

                if (first)
                    first = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(value.toString(), "UTF-8"));
            }

            return stringBuilder.toString();
        }
    }
}