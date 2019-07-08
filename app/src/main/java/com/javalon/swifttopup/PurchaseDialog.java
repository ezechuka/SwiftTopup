package com.javalon.swifttopup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseDialog extends DialogFragment {

    private MaterialSpinner priceTagSpinner;
    private TextView titleTextView;
    private TextView price;
    private String mobileNetwork;
    private String[] priceTag;
    private String title;
    private String amount;
    private PurchaseTransact purchaseTransact;
    private EditText mobileNumberEditText;
    private String tag;
    private String dataAmount;
    private String[][] dataTags = {
            {"1000", "2000", "5000"}, // MTN TAGS
            {"1600.01", "3750.01", "5000.01", "6000.01", "8000.01"}, // GLO TAG
            {"500.01", "1000.01", "1500.01", "2500.01", "4000.01"}, // 9mobile TAG
            {"1500.01", "3500.01", "7000.01", "10000.01"} // AIRTEL TAG
    };
    private static ArrayList<QueryTransact> queryLogs;
    private static Set<String> validPhoneNumbers;
    private ProgressDialog progressDialog;
    private static boolean payStatus;
    private static boolean validity;
    private static boolean isValidPhoneNumber;
    private static String paymentReference;

    // create callback interface
    interface BooleanCallback {
        void onSuccess(boolean result);
    }

    interface VerifyNumberCallback {
        void onSuccess(boolean result);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder purchaseDialog = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_dialog, null);
        Paper.init(getContext());
        PaystackSdk.initialize(getContext());
        PaystackSdk.setPublicKey(Constants.LIVE_PUBLIC_KEY);

        titleTextView = dialogView.findViewById(R.id.title);
        titleTextView.setText(title);

        priceTagSpinner = dialogView.findViewById(R.id.priceTag);
        priceTagSpinner.setItems(Arrays.asList(priceTag));

        mobileNumberEditText = dialogView.findViewById(R.id.mobileNumberEditText);
        mobileNumberEditText.setText(Paper.book().read(Constants.PHONE_NUMBER, ""));

        price = dialogView.findViewById(R.id.price);
        int itemID = priceTagSpinner.getSelectedIndex();
        amount = (String) priceTagSpinner.getItems().get(itemID);
        switch (tag) {
            case "1":
                amount = amount.replace("Airtime", "").replace(",", "").trim();
                price.setText("₦" + amount);
                Log.e("SwiftTopUp", amount);
                break;
            case "2":
                amount = amount.substring(amount.indexOf("N") + 1, amount.length());
                double priceAmount = Double.parseDouble(amount);
                if (priceAmount < 2500.0) {
                    priceAmount = priceAmount / (1.0 - 0.015);
                    dataAmount = String.valueOf(Math.ceil(priceAmount));
                    price.setText("₦" + dataAmount);
                } else {
                    priceAmount = (priceAmount + 100) / (1.0 - 0.015);
                    dataAmount = String.valueOf(Math.ceil(priceAmount));
                    price.setText("₦" + dataAmount);
                }
                break;
        }

        priceTagSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String value = (String) item;
                Log.e("SwiftTopUp", "Amount: " + amount);
                switch (tag) {
                    case "1":
                        amount = value.replace("Airtime", "").replace(",", "").trim();
                        price.setText("₦" + amount);
                        break;
                    case "2":
                        amount = value.substring(value.indexOf("N") + 1, value.length());
                        double priceAmount = Double.parseDouble(amount);
                        if (priceAmount < 2500.0) {
                            priceAmount = priceAmount / (1.0 - 0.015);
                            dataAmount = String.valueOf(Math.ceil(priceAmount));
                            price.setText("₦" + dataAmount);
                        } else {
                            priceAmount = (priceAmount + 100) / (1.0 - 0.015);
                            dataAmount = String.valueOf(Math.ceil(priceAmount));
                            price.setText("₦" + dataAmount);
                        }
                        break;
                }
            }
        });

        // set progress dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);

        // set query logs
        queryLogs = Paper.book().read(Constants.QUERY_LOGS, new ArrayList<QueryTransact>());

        // set valid numbers
        validPhoneNumbers = Paper.book().read(Constants.VALID_NUMBERS, new TreeSet<String>());

        Button purchaseButton = dialogView.findViewById(R.id.purchase);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tag) {
                    case "1":
                        // make airtime purchase
                        payRequest(new BooleanCallback() {
                            @Override
                            public void onSuccess(boolean result) {
                                if (result)
                                    airtimeRequest();
                            }
                        }, Integer.parseInt(amount) * 100);
                        break;
                    case "2":
                        // make data purchase
                        payRequest(new BooleanCallback() {
                            @Override
                            public void onSuccess(boolean result) {
                                if (result)
                                    dataBundleRequest();
                            }
                        }, (int)Double.parseDouble(dataAmount) * 100);
                        break;
                }
            }
        });

        purchaseDialog.setView(dialogView);
        return purchaseDialog.create();
    }

    public void setPriceTag(String[] priceTag, String mobileNetwork) {
        this.priceTag = priceTag;
        this.mobileNetwork = mobileNetwork;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private void airtimeRequest() {
        String phoneNumber = mobileNumberEditText.getText().toString();

        StringBuilder purchaseUrl = new StringBuilder();
        purchaseUrl.append("APIAirtimeV1.asp?");
        purchaseUrl.append("UserID").append("=").append(Constants.USER_ID).append("&");
        purchaseUrl.append("APIKey").append("=").append(Constants.API_KEY).append("&");
        purchaseUrl.append("MobileNetwork").append("=").append(mobileNetwork).append("&");
        purchaseUrl.append("Amount").append("=").append(amount).append("&");
        purchaseUrl.append("MobileNumber").append("=").append(phoneNumber).append("&");
        purchaseUrl.append("CallBackURL").append("=").append("www.google.com");
        Log.e("CKLite", purchaseUrl.toString());

        // make purchase request
        progressDialog.setMessage("Performing airtime purchase...");
        progressDialog.show();
        Api.getClient().purchase(purchaseUrl.toString()).enqueue(new Callback<PurchaseTransact>() {
            @Override
            public void onResponse(Call<PurchaseTransact> call, Response<PurchaseTransact> response) {
                if (response.isSuccessful()) {
                    // get current time
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                    final String time = simpleDateFormat.format(date);

                    purchaseTransact = response.body();
                    String orderID = purchaseTransact.getOrderid();

                    // make query request
                    StringBuilder queryUrl = new StringBuilder();
                    queryUrl.append("APIQueryV1.asp?");
                    queryUrl.append("UserID").append("=").append(Constants.USER_ID).append("&");
                    queryUrl.append("APIKey").append("=").append(Constants.API_KEY).append("&");
                    queryUrl.append("OrderID").append("=").append(orderID);

                    Api.getClient().queryPurchase(queryUrl.toString()).enqueue(new Callback<QueryTransact>() {
                        @Override
                        public void onResponse(Call<QueryTransact> call, Response<QueryTransact> response) {
                            // dismiss dialog
                            progressDialog.dismiss();

                            QueryTransact query = response.body();
                            query.setTime(time);
                            query.setNetworkIcon(mobileNetwork);
                            query.setTransactionReference(paymentReference);

                            queryLogs.add(0, query); // always add new query to the first position
                            Paper.book().write(Constants.QUERY_LOGS, queryLogs); // save queryLogs to internal memory

                            // clear card details
                            Paper.book().delete(Constants.STATUS);
                            Paper.book().delete(Constants.CARD_NUMBER);
                            Paper.book().delete(Constants.CVV);
                            Paper.book().delete(Constants.EXPIRY_DATE);

                            // display success dialog
                            new MaterialStyledDialog.Builder(getContext())
                                    .setTitle("Alert")
                                    .setDescription("Your airtime purchase was successful.\nThanks for your patronage :-)")
                                    .setPositiveText(R.string.ok)
                                    .setHeaderDrawable(R.drawable.all_networks)
                                    .withIconAnimation(true)
                                    .withDialogAnimation(true)
                                    .withDivider(true)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void onFailure(Call<QueryTransact> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Poor internet connection", Toast.LENGTH_LONG).show();
                    // dismiss dialog
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PurchaseTransact> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                // dismiss dialog
                progressDialog.dismiss();
            }
        });
    }

    private void dataBundleRequest() {
        String phoneNumber = mobileNumberEditText.getText().toString();
        StringBuilder purchaseUrl = new StringBuilder();
        purchaseUrl.append("APIDatabundleV1.asp?");
        purchaseUrl.append("UserID").append("=").append(Constants.USER_ID).append("&");
        purchaseUrl.append("APIKey").append("=").append(Constants.API_KEY).append("&");
        purchaseUrl.append("MobileNetwork").append("=").append(mobileNetwork).append("&");
        purchaseUrl.append("DataPlan").append("=").append(dataTags[Integer.parseInt(mobileNetwork) - 1][priceTagSpinner.getSelectedIndex()]).append("&");
        purchaseUrl.append("MobileNumber").append("=").append(phoneNumber).append("&");
        purchaseUrl.append("CallBackURL").append("=").append("www.google.com");
        Log.e("CKLite", purchaseUrl.toString());

        // make purchase request
        progressDialog.setMessage("Performing data bundle purchase...");
        progressDialog.show();
        Api.getClient().purchase(purchaseUrl.toString()).enqueue(new Callback<PurchaseTransact>() {
            @Override
            public void onResponse(Call<PurchaseTransact> call, Response<PurchaseTransact> response) {
                if (response.isSuccessful()) {
                    // get current time
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                    final String time = simpleDateFormat.format(date);

                    purchaseTransact = response.body();
                    String orderID = purchaseTransact.getOrderid();

                    // make query request
                    StringBuilder queryUrl = new StringBuilder();
                    queryUrl.append("APIQueryV1.asp?");
                    queryUrl.append("UserID").append("=").append(Constants.USER_ID).append("&");
                    queryUrl.append("APIKey").append("=").append(Constants.API_KEY).append("&");
                    queryUrl.append("OrderID").append("=").append(orderID);

                    Api.getClient().queryPurchase(queryUrl.toString()).enqueue(new Callback<QueryTransact>() {
                        @Override
                        public void onResponse(Call<QueryTransact> call, Response<QueryTransact> response) {
                            // dismiss dialog
                            progressDialog.dismiss();

                            QueryTransact query = response.body();
                            query.setTime(time);
                            query.setNetworkIcon(mobileNetwork);

                            query.setTransactionReference(paymentReference);
                            queryLogs.add(0, query); // always add new query to the first position
                            Paper.book().write(Constants.QUERY_LOGS, queryLogs); // save queryLogs to internal memory

                            // display success dialog
                            new MaterialStyledDialog.Builder(getContext())
                                    .setTitle("Alert")
                                    .setDescription("Your data purchase was successful.\nThanks for your patronage :-)")
                                    .setPositiveText(R.string.ok)
                                    .setHeaderDrawable(R.drawable.all_networks)
                                    .withIconAnimation(true)
                                    .withDivider(true)
                                    .withDialogAnimation(true)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void onFailure(Call<QueryTransact> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Poor internet connection", Toast.LENGTH_LONG).show();
                    // dismiss dialog
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PurchaseTransact> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                // dismiss dialog
                progressDialog.dismiss();
            }
        });
    }

    private void payRequest(final BooleanCallback mainCallback, final int amount) {
        final boolean cardStatus = Paper.book().read(Constants.STATUS, false);

        String phoneNumber = mobileNumberEditText.getText().toString();

        // store the phone number
        Paper.book().write(Constants.PHONE_NUMBER, phoneNumber);

        new MaterialStyledDialog.Builder(getActivity())
                .setTitle("Alert")
                .setDescription("Do you want to perform this transaction?")
                .setHeaderDrawable(R.drawable.all_networks)
                .withIconAnimation(true)
                .withDialogAnimation(true)
                .withDivider(true)
                .setNegativeText("NO")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("YES")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        // check for card
                        if (!cardStatus) {
                            Toast.makeText(getContext(), "Please setup card", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getContext(), CardDetailsActivity.class));
                            return;
                        }

                        // verify phone number
                        final String phoneNumber = mobileNumberEditText.getText().toString();
                        if (!validPhoneNumbers.contains(phoneNumber)) {
                            verifyPhoneNumberRequest(new VerifyNumberCallback() {
                                @Override
                                public void onSuccess(boolean result) {
                                    isValidPhoneNumber = result;
                                    if (isValidPhoneNumber) {
                                        // store valid phone number in intenal memory against next time
                                        validPhoneNumbers.add(phoneNumber);
                                        Paper.book().write(Constants.VALID_NUMBERS, validPhoneNumbers);

                                        // process transaction
                                        processPayment(amount, mainCallback);

                                    } else {
                                        Toast.makeText(getContext(), "The phone number you entered is not valid!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, phoneNumber);
                        } else
                            // process transaction
                            processPayment(amount, mainCallback);
                    }
                })
                .show();
    }

    private void verifyPhoneNumberRequest(
            final VerifyNumberCallback verifyNumberCallback, String phoneNumber) {

        progressDialog.setMessage("Verifying phone number...");
        progressDialog.show();

        if (TextUtils.isEmpty(phoneNumber)) {
            mobileNumberEditText.setError("REQUIRED!");
            progressDialog.dismiss();
            return;
        }

        if ((phoneNumber.length() < 11)) {
            mobileNumberEditText.setError("INVALID!");
            progressDialog.dismiss();
            return;
        }

        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            mobileNumberEditText.setError("INVALID!");
            progressDialog.dismiss();
            return;
        }

        // phone request builder
        StringBuilder phoneRequestBuilder = new StringBuilder();
        phoneRequestBuilder.append("api/validate?");
        phoneRequestBuilder.append("access_key").append("=").append(Constants.ACCESS_KEY).append("&");
        phoneRequestBuilder.append("number").append("=").append(phoneNumber).append("&");
        phoneRequestBuilder.append("country_code").append("=").append("NG").append("&");
        phoneRequestBuilder.append("format").append("=").append("1");

        Api.getPhoneClient().verifyNumber(phoneRequestBuilder.toString()).enqueue(new Callback<VerifyPhoneNumber>() {
            @Override
            public void onResponse(Call<VerifyPhoneNumber> call, Response<VerifyPhoneNumber> response) {
                if (response.isSuccessful()) {
                    VerifyPhoneNumber phoneNumber = response.body();
                    validity = phoneNumber.getValid();
                    progressDialog.dismiss();

                    verifyNumberCallback.onSuccess(validity);
                }
            }

            @Override
            public void onFailure(Call<VerifyPhoneNumber> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void processPayment(int amount, final BooleanCallback mainCallback) {
        progressDialog.setMessage("Processing Transaction");
        progressDialog.show();

        // get card details
        String cardNumber = Paper.book().read(Constants.CARD_NUMBER);
        String expiryDate = Paper.book().read(Constants.EXPIRY_DATE);
        String[] dateArray = expiryDate.split("/");
        String month = dateArray[0].trim();
        String year = dateArray[1].trim();
        String cvv = Paper.book().read(Constants.CVV);
        String email = Paper.book().read(Constants.EMAIL_ADDRESS);

        Card card = new Card(cardNumber, Integer.parseInt(month), Integer.parseInt(year), cvv);
        if (card.isValid()) {
            Charge charge = new Charge();
            charge.setCard(card);
            charge.setAmount(amount);
            charge.setCurrency("NGN");
            charge.setEmail(email);

            PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
                @Override
                public void onSuccess(Transaction transaction) {
                    payStatus = true;
                    paymentReference = transaction.getReference();
                    Toast.makeText(getContext(), "Transaction successful " + transaction.getReference(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                    mainCallback.onSuccess(payStatus);
                }

                @Override
                public void beforeValidate(Transaction transaction) {
                    Toast.makeText(getContext(), "Requesting OTP...", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Throwable error, Transaction transaction) {
                    payStatus = false;
                    Toast.makeText(getContext(),
                            "Transaction unsuccessful " + transaction.getReference(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Invalid card details", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
}