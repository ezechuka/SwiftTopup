package com.javalon.cklite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Charge;

public class PayRequest {

    private static Charge charge;
    private Activity currentActivity;
    private Context context;
    private static ProgressDialog payDialog;
    public static boolean SUCCESSFUL;

    public PayRequest(Charge charge, Activity currentActivity, Context context) {
        PayRequest.charge = charge;
        this.currentActivity = currentActivity;
        this.context = context;
    }

    public void execute() {
        PaystackSdk.chargeCard(currentActivity, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(final Transaction transaction) {
                SUCCESSFUL = true;
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Successful: " + transaction.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, final Transaction transaction) {
                SUCCESSFUL = false;
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Not successful: " + transaction.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}

//    private class PayTask extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            payDialog = new ProgressDialog(context);
//            payDialog.setTitle("Please wait...");
//            payDialog.setMessage("Sending request");
//            payDialog.setCancelable(false);
//            payDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            PaystackSdk.chargeCard(currentActivity, charge, new Paystack.TransactionCallback() {
//                @Override
//                public void onSuccess(final Transaction transaction) {
//                    successful = true;
//                    currentActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(context, "Successful: " + transaction.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void beforeValidate(Transaction transaction) {
//
//                }
//
//                @Override
//                public void onError(Throwable error, final Transaction transaction) {
//                    currentActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            successful = false;
//                            Toast.makeText(context, "Not successful: " + transaction.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            });
//
//            return successful;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean status) {
//            super.onPostExecute(status);
//            payDialog.dismiss();
//        }
//
//    }