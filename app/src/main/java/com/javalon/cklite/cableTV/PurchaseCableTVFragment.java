package com.javalon.cklite.cableTV;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.javalon.cklite.PayRequest;
import com.javalon.cklite.PostRequest;
import com.javalon.cklite.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

import static com.javalon.cklite.PayRequest.SUCCESSFUL;

public class PurchaseCableTVFragment extends Fragment {

    public static final String CABLE_TV = "cableTV";

    private static final String verifyCardNumberURL = "https://www.nellobytesystems.com/APIBuyCableTV.asp?";
    private static final String baseURL = "https://www.nellobytesystems.com/APIBuyCableTV.asp?";
    private static final String queryURL = "https://www.nellobytesystems.com/APIQuery.asp?";
    public static Context context;
    public static Activity activity;
    private static String cardNumber;
    private static int expiryMonth;
    private static int expiryYear;
    private static String cvv;
    public static final String USER_ID = "UserID";
    public static final String userId = "CK10128079";
    public static final String API_KEY = "APIKey";
    public static final String apiKey = "58R7DJLGF886OG4945XZNVO50123A9S30K83D8N61SYMUV9LVMM8210YAB2831ZU";
    public static final String CABLETV = "CableTV";
    public static final String PACKAGE_TYPE = "PACKAGE";
    public static final String SMART_CARD_NO = "SmartCardNo";
    public static final String CALL_BACK_URL = "CallBackURL";
    public static final String call_back_url = "www.google.com";
    public static final String ORDER_ID = "OrderID";
    private static final String TAG = "ckLite";
    private int index = 0;
    private String cable;
    private String[] cablePackages;
    private String cableAmt;
    private static String cableAmount;
    private static ProgressDialog verifyDialog;

    // text views
    TextView cardNumberTextView;
    TextView expiryDate;
    TextView cvvText;

    EditText expiryDateEditText;

    public PurchaseCableTVFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cableTView = inflater.inflate(R.layout.fragment_purchase_cable_tv, container, false);
        final EditText packageEditTxt = cableTView.findViewById(R.id.packageType);
        final String cableType = getActivity().getIntent().getStringExtra(CABLE_TV);
        final String[][] cableTypes = {
                getResources().getStringArray(R.array.dstvPackages),
                getResources().getStringArray(R.array.gotvPackages),
                getResources().getStringArray(R.array.starTimesPackages)
        };
        packageEditTxt.setText(cableTypes[Integer.parseInt(cableType) - 1][0]);  // set default value for cable types
        context = getContext();
        activity = getActivity();

        cableTView.findViewById(R.id.infoBalance).setSelected(true);
        // display showcase view
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(cableTView.findViewById(R.id.packages), "Select cable package", getString(R.string.cable_description))
                        // All options below are optional
                        .outerCircleColor(android.R.color.holo_blue_dark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(android.R.color.black)   // Specify a color for the target circle
                        .titleTextSize(24)                  // Specify the size (in sp) of the title text
                        .titleTextColor(android.R.color.holo_blue_dark)      // Specify the color of the title text
                        .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(android.R.color.holo_blue_dark)  // Specify the color of the description text
                        .textColor(android.R.color.white)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(android.R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
//                                .icon(Drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(final TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        view.dismiss(true);
                    }
                });
        ImageView mobileIcon = cableTView.findViewById(R.id.networkIcon);
        FloatingActionButton fabAmt = cableTView.findViewById(R.id.packages);
        fabAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getContext())
                        .title("Select data plan")
                        .items(cableTypes[Integer.parseInt(cableType) - 1])
                        .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                cable = String.valueOf(text);
                                Log.e(TAG, cable);
                                packageEditTxt.setText(cable);
                                Log.e(TAG, cable);
                                index = which;
                                dialog.setSelectedIndex(which);
                                cablePackages = cableTypes[Integer.parseInt(cableType) - 1];
                                cable = cablePackages[index];
                                Log.e(TAG, "cable: " + cable);
                                cableAmt = cablePackages[index];
                                cableAmount = cableAmt.substring(cableAmt.indexOf("N") + 1, cableAmt.length());
                                Log.e(TAG, "cableAmount: " + cableAmount);
                                dialog.dismiss();
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .show();
            }
        });

        switch (cableType) {
            case "01":
                mobileIcon.setImageResource(R.drawable.dstv_image1);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.dstv_image1);
                break;
            case "02":
                mobileIcon.setImageResource(R.drawable.gotv_logo_4);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.gotv_logo_4);
                break;
            case "03":
                mobileIcon.setImageResource(R.drawable.startimes_logo_3);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.startimes_logo_3);
                break;
        }

        // get reference to all views
        // text views
        cardNumberTextView = cableTView.findViewById(R.id.credit_card_number);
        expiryDate = cableTView.findViewById(R.id.expireTextView);
        cvvText = cableTView.findViewById(R.id.cvvTextView);

        // edit texts
        final EditText cardNumberEditText = cableTView.findViewById(R.id.card_number_editText);
        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String stringValue = String.valueOf(charSequence);
                StringBuilder sb = new StringBuilder(stringValue);
                for (int c = 0; c < stringValue.length(); c++) {
                    if ((c % 4) == 0 && (c != 0)) {
                        if (c >= 8) {
                            if (c == 12) {
                                sb.insert(c + 2, " ");
                                cardNumberTextView.setText(sb.toString());
                            } else {
                                sb.insert(c + 1, " ");
                                cardNumberTextView.setText(sb.toString());
                            }
                        } else {
                            sb.insert(c, " ");
                            cardNumberTextView.setText(sb.toString());
                        }
                    } else {
                        cardNumberTextView.setText(sb.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    cardNumberTextView.setText(getResources().getString(R.string.card_number));
            }
        });

        expiryDateEditText = cableTView.findViewById(R.id.expiryDateEditText);
        expiryDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                expiryDate.setText(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                StringBuilder builder = new StringBuilder();
                if (editable.toString().length() == 4) {
                    char[] charArray = editable.toString().toCharArray();
                    for (int i = 0; i < charArray.length; i++) {
                        if (i == 2) {
                            builder.append(" / ").append(charArray[i]);
                        } else
                            builder.append(charArray[i]);
                    }
                    expiryDate.setText(builder.toString());
                }

                if (editable.toString().isEmpty())
                    expiryDate.setText(getResources().getString(R.string.expiryDate));
            }
        });

        final EditText cvvEditText = cableTView.findViewById(R.id.cvvEditText);
        cvvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cvvText.setText(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    cvvText.setText(getResources().getString(R.string.cvv));
            }
        });

        final EditText smartCardNumber = cableTView.findViewById(R.id.smartCardNumberEditText);

        Button paymentButton = cableTView.findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("Confirm")
                        .setDescription("Do you want to proceed? :-)")
                        .setHeaderDrawable(R.drawable.all_networks)
                        .withDivider(true)
                        .withDialogAnimation(true, Duration.SLOW)
                        .setPositive("YES", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // check for empty edit text fields
                                if (TextUtils.isEmpty(smartCardNumber.getText().toString())) {
                                    smartCardNumber.setError("REQUIRED!");
                                    return;
                                }

                                if (TextUtils.isEmpty(cardNumberEditText.getText().toString())) {
                                    cardNumberEditText.setError("REQUIRED!");
                                    return;
                                }

                                if (TextUtils.isEmpty(cvvEditText.getText().toString())) {
                                    cvvEditText.setError("REQUIRED!");
                                    return;
                                }

                                if (TextUtils.isEmpty(expiryDateEditText.getText().toString())) {
                                    expiryDateEditText.setError("REQUIRED!");
                                    return;
                                }

                                // get values from the edit text fields
                                cardNumber = cardNumberEditText.getText().toString();
                                cvv = cvvEditText.getText().toString();
                                String dateAndYear = expiryDateEditText.getText().toString();
                                expiryMonth = Integer.parseInt(dateAndYear.substring(0, 2));
                                expiryYear = Integer.parseInt(dateAndYear.substring(2, dateAndYear.length()));

                                try {
                                    JSONObject verifyJSONObject = new JSONObject();
                                    verifyJSONObject.put(PurchaseCableTVFragment.USER_ID, PurchaseCableTVFragment.userId);
                                    verifyJSONObject.put(PurchaseCableTVFragment.API_KEY, PurchaseCableTVFragment.apiKey);
                                    verifyJSONObject.put(PurchaseCableTVFragment.CABLETV, cableType);
                                    verifyJSONObject.put(PurchaseCableTVFragment.SMART_CARD_NO, smartCardNumber.getText().toString());

                                    JSONObject cableTVJSONObject = new JSONObject();
                                    cableTVJSONObject.put(PurchaseCableTVFragment.USER_ID, PurchaseCableTVFragment.userId);
                                    cableTVJSONObject.put(PurchaseCableTVFragment.API_KEY, PurchaseCableTVFragment.apiKey);
                                    cableTVJSONObject.put(PurchaseCableTVFragment.CABLETV, cableType);
                                    cableTVJSONObject.put(PurchaseCableTVFragment.PACKAGE_TYPE, cableType);
                                    cableTVJSONObject.put(PurchaseCableTVFragment.SMART_CARD_NO, smartCardNumber.getText().toString());
                                    cableTVJSONObject.put(PurchaseCableTVFragment.CALL_BACK_URL, PurchaseCableTVFragment.call_back_url);

                                    JSONObject queryCableTVJSONObject = new JSONObject();
                                    queryCableTVJSONObject.put(PurchaseCableTVFragment.USER_ID, PurchaseCableTVFragment.userId);
                                    queryCableTVJSONObject.put(PurchaseCableTVFragment.API_KEY, PurchaseCableTVFragment.apiKey);
                                    new VerifyTask().execute(verifyJSONObject, cableTVJSONObject, queryCableTVJSONObject);

                                } catch (JSONException jsonException) {

                                }
                            }
                        })
                        .setNegativeText("NO")
                        .build()
                        .show();
            }

        });

        return cableTView;

    }

    private static class VerifyTask extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            verifyDialog = new ProgressDialog(context);
            verifyDialog.setTitle("Please wait");
            verifyDialog.setMessage("Verifying smart card number");
            verifyDialog.setCancelable(false);
            verifyDialog.show();
        }


        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            try {
                URL verifyURL = new URL(verifyCardNumberURL);
                HttpsURLConnection verifyConn = (HttpsURLConnection) verifyURL.openConnection();
                verifyConn.setRequestMethod("POST");
                verifyConn.setDoOutput(true);
                verifyConn.setDoOutput(true);
                verifyConn.setReadTimeout(20000);
                verifyConn.setConnectTimeout(15000);

                // write to the output stream
                OutputStream os = verifyConn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                JSONObject verifyJSONObject = jsonObjects[0];
                bw.write(getPostDataString(verifyJSONObject));
                bw.flush();
                bw.close();

                // read from the input stream
                int responseCode = verifyConn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    HttpsURLConnection readVerification = (HttpsURLConnection) verifyURL.openConnection();
                    InputStream is = readVerification.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    is.close();
                    readVerification.disconnect();
                    JSONObject verifyResponse = new JSONObject(sb.toString());
                    if (verifyResponse == null) return null;
                    String status = (String) verifyResponse.get("customer_name");
                    if (status.equals("INVALID_SMARTCARDNO")) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Invalid IUC/Smart Card Number", Toast.LENGTH_LONG).show();
                            }
                        });
                        return null;
                    } else {
                        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
                        if (card.isValid()) {
                            Charge charge = new Charge();
                            charge.setAmount(Integer.parseInt(cableAmount) * 100); // charge in kobo
                            charge.setCard(card);
                            charge.setCurrency("NGN");
                            charge.setEmail("ezechukwuka9@gmail.com");
                            charge.setTransactionCharge(1000);

                            PayRequest payCableTVRequest = new PayRequest(charge, activity, context);
                            payCableTVRequest.execute();
                            boolean success = SUCCESSFUL;
                            if (success) {
                                PostRequest cableTVRequest =
                                        new PostRequest(baseURL, queryURL, jsonObjects[1], jsonObjects[2], context);
                                cableTVRequest.buy();
                                return cableTVRequest.getQueryResponse();
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject queryResponse) {
            super.onPostExecute(queryResponse);
            verifyDialog.dismiss();
            if (queryResponse == null) {
                new MaterialStyledDialog.Builder(context)
                        .setTitle("Oops!")
                        .setDescription("There may be something wrong with your data connection :-( ")
                        .setHeaderDrawable(R.drawable.dstv_gotv_startimes)
                        .setPositiveText("GOT IT!")
                        .withDivider(true)
                        .withDialogAnimation(true, Duration.SLOW)
                        .setCancelable(false)
                        .build()
                        .show();
            }else {
                try {
                    String mobileNetwork = (String) queryResponse.get("mobileNetwork");
                    String mobileNumber = (String) queryResponse.get("mobilenumber");
                    String orderType = (String) queryResponse.get("ordertype");
                    String remark = (String) queryResponse.get("remark");
                    String date = (String) queryResponse.get("date");
                    StringBuilder requestSummary = new StringBuilder();
                    requestSummary.append("Mobile Network: \t").append(mobileNetwork).append("\n");
                    requestSummary.append("Mobile Number: \t").append(mobileNumber).append("\n");
                    requestSummary.append("Data Plan: \t").append(orderType).append("\n");
                    requestSummary.append("Date: \t").append(date);
                    new MaterialStyledDialog.Builder(context)
                            .setTitle(remark + "!")
                            .setDescription(requestSummary.toString())
                            .setHeaderDrawable(R.drawable.dstv_gotv_startimes)
                            .withDialogAnimation(true, Duration.NORMAL)
                            .setPositiveText("GOT IT!")
                            .setScrollable(true)
                            .setCancelable(false)
                            .withDivider(true)
                            .build()
                            .show();
                } catch (JSONException jsonException) {

                }
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