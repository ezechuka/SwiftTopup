package com.javalon.cklite.dataBundle;


import android.graphics.Typeface;
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

import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

import static com.javalon.cklite.PayRequest.SUCCESSFUL;

public class PurchaseDataBundleFragment extends Fragment {

    private final String[][] allDataPlans = {
            {"1000", "2000", "5000"},   // mtn
            {"1600.01", "3750.01", "5000.01", "6000.01", "8000.01", "12000.01", "16000.01"}, // glo
            {"500.01", "1000.01", "1500.01", "2500.01", "4000.01"},     // nine mobile
            {"1500.01", "3500.01", "7000.01", "10000.01"} // airtel
    };
    public static final String MOBILE_NETWORK = "MobileNetwork";
    private static final String baseURL = "https://www.nellobytesystems.com/APIBuy.asp?";
    private static final String queryURL = "https://www.nellobytesystems.com/APIQuery.asp?";
    private static String cardNumber;
    private static int expiryMonth;
    private static int expiryYear;
    private static String cvv;
    public static final String USER_ID = "UserID";
    public static final String userId = "CK10128079";
    public static final String API_KEY = "APIKey";
    public static final String apiKey = "58R7DJLGF886OG4945XZNVO50123A9S30K83D8N61SYMUV9LVMM8210YAB2831ZU";
    public static final String DATA_PLAN = "DataPlan";
    public static final String MOBILE_NUMBER = "MobileNumber";
    public static final String USERID = "CK10128079";
    public static final String CALL_BACK_URL = "CallBackURL";
    public static final String call_back_url = "www.google.com";
    public static final String ORDER_ID = "OrderID";
    private static final String TAG = "ckLite";
    private int index = 0;
    private String dataPlan;    // chosen data plan
    private String[] dataPlans; // data plans for a particular network
    private String dataAmt;     // amount and data plan size for a particular data plan
    private String dataAmount;  // amount for chosen data plan
    private String data;

    // text views
    TextView cardNumberTextView;
    TextView expiryDate;
    TextView cvvText;

    EditText expiryDateEditText;

    public PurchaseDataBundleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dataBundleView = inflater.inflate(R.layout.fragment_purchase_data_bundle, container, false);
        final EditText dataPlanEditTxt = dataBundleView.findViewById(R.id.dataPlan);
        final String mobileNetwork = getActivity().getIntent().getStringExtra(MOBILE_NETWORK);
        final String[][] networkPlans = {getResources().getStringArray(R.array.mtnDataPlans),
                getResources().getStringArray(R.array.gloDataPlans),
                getResources().getStringArray(R.array.nineMobileDataPlans),
                getResources().getStringArray(R.array.airtelDataPlans)};
        dataPlanEditTxt.setText(networkPlans[Integer.parseInt(mobileNetwork)-1][0]);  // set default value for data plans
        String stringAmount = networkPlans[Integer.parseInt(mobileNetwork)-1][0];
        stringAmount = stringAmount.replace(",", "");
        dataAmount = stringAmount.substring(stringAmount.indexOf("N") + 1, stringAmount.length());
        dataBundleView.findViewById(R.id.infoBalance).setSelected(true);
        // display showcase view
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(dataBundleView.findViewById(R.id.dataPlans), "Select data plan", getString(R.string.data_description))
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

        ImageView mobileIcon = dataBundleView.findViewById(R.id.networkIcon);
        FloatingActionButton fabAmt = dataBundleView.findViewById(R.id.dataPlans);
        fabAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getContext())
                        .title("Select data plan")
                        .items(networkPlans[Integer.parseInt(mobileNetwork) - 1])
                        .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dataPlan = String.valueOf(text);
                                Log.e(TAG, dataPlan);
                                dataPlanEditTxt.setText(dataPlan);
                                Log.e(TAG, dataPlan);
                                index = which;
                                dialog.setSelectedIndex(which);
                                dataPlans = networkPlans[Integer.parseInt(mobileNetwork) - 1];
                                data = allDataPlans[(Integer.parseInt(mobileNetwork) - 1)][index];
                                Log.e(TAG, "data: " + data);
                                dataAmt = dataPlans[index];
                                dataAmount = dataAmt.substring(dataAmt.indexOf("N") + 1, dataAmt.length());
                                Log.e(TAG, "dataAmount: " + dataAmount);
                                dialog.dismiss();
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .show();
            }
        });

        switch (mobileNetwork) {
            case "01":
                mobileIcon.setImageResource(R.drawable.mtn_logo_1);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.mtn_logo_1);
                break;
            case "02":
                mobileIcon.setImageResource(R.drawable.glo_logo_2);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.glo_logo_2);
                break;
            case "03":
                mobileIcon.setImageResource(R.drawable.nine_mobile_logo_1);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.nine_mobile_logo_1);
                break;
            case "04":
                mobileIcon.setImageResource(R.drawable.airtel_logo_2);
                mobileIcon.setVisibility(View.VISIBLE);
                fabAmt.setImageResource(R.drawable.airtel_logo_2);
                break;
        }

        // get reference to all views
        // text views
        cardNumberTextView = dataBundleView.findViewById(R.id.credit_card_number);
        expiryDate = dataBundleView.findViewById(R.id.expireTextView);
        cvvText = dataBundleView.findViewById(R.id.cvvTextView);

        // edit texts
        final EditText cardNumberEditText = dataBundleView.findViewById(R.id.card_number_editText);
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

        expiryDateEditText = dataBundleView.findViewById(R.id.expiryDateEditText);
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

        final EditText cvvEditText = dataBundleView.findViewById(R.id.cvvEditText);
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

        final EditText mobileNumber = dataBundleView.findViewById(R.id.phoneNumberEditText);

        Button paymentButton = dataBundleView.findViewById(R.id.paymentButton);
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
                                if (TextUtils.isEmpty(mobileNumber.getText().toString())) {
                                    mobileNumber.setError("REQUIRED!");
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

                                // create databundle object
                                DataBundle dataBundle = new DataBundle();
                                dataBundle.setMobileNetwork(mobileNetwork);
                                dataBundle.setMobileNumber(mobileNumber.getText().toString());
                                dataBundle.setDataPlan(dataAmount);

                                // create json object with params
                                try {
                                    // json object for request
                                    JSONObject dataBundleJSONObject = new JSONObject();
                                    dataBundleJSONObject.put(PurchaseDataBundleFragment.USER_ID, PurchaseDataBundleFragment.userId);
                                    dataBundleJSONObject.put(PurchaseDataBundleFragment.API_KEY, PurchaseDataBundleFragment.apiKey);
                                    dataBundleJSONObject.put(PurchaseDataBundleFragment.DATA_PLAN, data);
                                    dataBundleJSONObject.put(PurchaseDataBundleFragment.MOBILE_NUMBER, dataBundle.getMobileNumber());
                                    dataBundleJSONObject.put(PurchaseDataBundleFragment.MOBILE_NETWORK, mobileNetwork);
                                    dataBundleJSONObject.put(PurchaseDataBundleFragment.CALL_BACK_URL, PurchaseDataBundleFragment.call_back_url);

                                    // json object for query
                                    JSONObject dataBundleQueryJSONObject = new JSONObject();
                                    dataBundleQueryJSONObject.put(PurchaseDataBundleFragment.USER_ID, PurchaseDataBundleFragment.userId);
                                    dataBundleQueryJSONObject.put(PurchaseDataBundleFragment.API_KEY, PurchaseDataBundleFragment.apiKey);
                                    Log.e(TAG, "PurchaseDataBundleFragment: " + dataBundleQueryJSONObject.toString());

                                    // make airtime request
                                    Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
                                    if (card.isValid()) {
                                        Charge charge = new Charge();
                                        charge.setAmount(Integer.parseInt(dataAmount)*100); // charge in kobo
                                        charge.setCard(card);
                                        charge.setCurrency("NGN");
                                        charge.setEmail("ezechukwuka9@gmail.com");

                                        PayRequest dataBundleRequest = new PayRequest(charge, getActivity(), getContext());
                                        dataBundleRequest.execute();
                                        boolean status = SUCCESSFUL;
                                        if (status) {
                                            try {
                                                PostRequest buyDataBundleRequest =
                                                        new PostRequest(baseURL, queryURL, dataBundleJSONObject, dataBundleQueryJSONObject, getContext());
                                                buyDataBundleRequest.buy();
                                                JSONObject queryResponse = buyDataBundleRequest.getQueryResponse();
                                                if (queryResponse == null)
                                                    return;
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
                                                new MaterialStyledDialog.Builder(getContext())
                                                        .setTitle(remark + "!")
                                                        .setDescription(requestSummary.toString())
                                                        .setHeaderDrawable(R.drawable.all_networks)
                                                        .withDialogAnimation(true, Duration.NORMAL)
                                                        .setPositiveText("GOT IT!")
                                                        .setScrollable(true)
                                                        .setCancelable(false)
                                                        .withDivider(true)
                                                        .build()
                                                        .show();
                                            } catch (JSONException exception) {

                                            }
                                        } else {
                                            new MaterialStyledDialog.Builder(getContext())
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
                                    } else {
                                        Toast.makeText(getContext(), "Invalid card details", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                    jsonException.printStackTrace();
                                }
                            }
                        })
                        .setNegativeText("NO")
                        .build()
                        .show();
            }
        });
        return dataBundleView;
    }
}