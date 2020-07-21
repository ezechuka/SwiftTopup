package com.javalon.swifttopup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class CardDetailsActivity extends AppCompatActivity {

    // edit texts
    private TextInputEditText cardNumberEditText;
    private TextInputEditText expiryDateEditText;
    private TextInputEditText cvvEditText;
    private TextInputEditText emailEditText;

    // text views
    private TextView cardNumberTextView;
    private TextView expiryDateTextView;
    private TextView cvvTextView;
    private MaterialButton confirmButton;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Paper.init(getApplicationContext());

        // get reference to all views
        // text views
        cardNumberTextView = findViewById(R.id.credit_card_number);
        expiryDateTextView = findViewById(R.id.expireTextView);
        cvvTextView = findViewById(R.id.cvvTextView);
        emailTextView = findViewById(R.id.emailTextView);

        // edit texts
        cardNumberEditText = findViewById(R.id.card_number_editText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        emailEditText = findViewById(R.id.email_edit_text);

        // check if user has entered card details
        String cardNumber = Paper.book().read(Constants.CARD_NUMBER, "");
        String expiryDate = Paper.book().read(Constants.EXPIRY_DATE, "");
        String cvv = Paper.book().read(Constants.CVV, "");
        String email = Paper.book().read(Constants.EMAIL_ADDRESS, "");

        if (!cardNumber.isEmpty()) {
            cardNumberTextView.setText(cardNumber);
            cardNumberEditText.setText(cardNumber.replace(" ", ""));
        }

        if (!expiryDate.isEmpty()) {
            expiryDateTextView.setText(expiryDate);
            expiryDate = expiryDate.replace(" ", "").replace("/", "");
            expiryDateEditText.setText(expiryDate);
        }

        if (!cvv.isEmpty()) {
            cvvEditText.setText(cvv);
        }

        if (!email.isEmpty()) {
            emailEditText.setText(email);
            emailTextView.setText(email);
        }

//         listeners
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

        expiryDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                expiryDateTextView.setText(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                StringBuilder builder = new StringBuilder();
                if (editable.toString().length() == 4) {
                    char[] charArray = editable.toString().toCharArray();
                    for (int i = 0; i < charArray.length; i++) {
                        if (i == 2) {
                            builder.append("/").append(charArray[i]);
                        } else
                            builder.append(charArray[i]);
                    }
                    expiryDateTextView.setText(builder.toString());
                }

                if (editable.toString().isEmpty())
                    expiryDateTextView.setText(getResources().getString(R.string.expiryDate));
            }
        });

        cvvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cvvTextView.setText(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    cvvTextView.setText(getResources().getString(R.string.cvv));
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailTextView.setText(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    emailTextView.setText(getResources().getString(R.string.email_address));
            }
        });

        // get reference to confirm button
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNumber = cardNumberEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();
                String cvv = cvvEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (TextUtils.isEmpty(cardNumber)) {
                    cardNumberEditText.setError("REQUIRED!");
                    return;
                }

                if (cardNumber.length() < 16) {
                    cardNumberEditText.setError("INVALID!");
                }

                if (TextUtils.isEmpty(expiryDate)) {
                    expiryDateEditText.setError("REQUIRED!");
                    return;
                }

                if (expiryDate.length() < 4) {
                    expiryDateEditText.setError("INVALID!");
                    return;
                }

                if (TextUtils.isEmpty(cvv)) {
                    cvvEditText.setError("REQUIRED!");
                    return;
                }

                if (cvv.length() != 3) {
                    cvvEditText.setError("INVALID!");
                    return;
                }

                if (email.isEmpty()) {
                    emailEditText.setError("REQUIRED!");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("INVALID!");
                    return;
                }

                Paper.book().write(Constants.STATUS, true);
                Paper.book().write(Constants.CARD_NUMBER, cardNumberTextView.getText().toString());
                Paper.book().write(Constants.EXPIRY_DATE, expiryDateTextView.getText().toString());
                Paper.book().write(Constants.CVV, cvvEditText.getText().toString());
                Paper.book().write(Constants.EMAIL_ADDRESS, emailEditText.getText().toString());

                Toast.makeText(getApplicationContext(), "Details confirmed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CardDetailsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_menu) {
            MaterialAlertDialogBuilder clearDialog = new MaterialAlertDialogBuilder(CardDetailsActivity.this);
            clearDialog.setTitle("Alert");
            clearDialog.setMessage("Do you want to clear card details?");
            clearDialog.setCancelable(true);
            clearDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Paper.book().delete(Constants.STATUS);
                    Paper.book().delete(Constants.CARD_NUMBER);
                    Paper.book().delete(Constants.EXPIRY_DATE);
                    Paper.book().delete(Constants.CVV);
                    Paper.book().delete(Constants.EMAIL_ADDRESS);

//                    Toast.makeText(getApplicationContext(), "Details cleared", Toast.LENGTH_LONG).show();
                    // clear all edit texts
                    cardNumberEditText.setText("");
                    cvvEditText.setText("");
                    expiryDateEditText.setText("");
                    emailEditText.setText("");

                    // reset all text views
                    cardNumberTextView.setText(R.string.card_number);
                    cvvTextView.setText(R.string.cvv);
                    expiryDateTextView.setText(R.string.expiryDate);
                    emailTextView.setText(R.string.email_address);

                    // send focus to card edit text
                    cardNumberEditText.requestFocus();
                }
            });

            clearDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            clearDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}