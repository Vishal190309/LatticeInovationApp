package com.latticeInnovations.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.latticeInnovations.app.controller.AppController;
import com.latticeInnovations.app.data.Repository;
import com.latticeInnovations.app.databinding.ActivityRegisterBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setEndIconInvisible();
        checkTextView();
        enableCounter();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.list_item);
        binding.genderDropdown.setAdapter(adapter);


        binding.pincodeText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                checkDistrict();
            }
        });
        binding.date.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                checkDate();
            }
        });

        binding.calenderPicker.setOnClickListener(view -> {
            Log.d("Clicked", "Clicked");
            binding.setIsVisible(!binding.getIsVisible());
        });

        binding.calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            binding.date.setText(getString(R.string.date_text,dayOfMonth,month,year));
            binding.setIsVisible(false);
        });

        binding.registerButton.setOnClickListener(view -> {
            if (!checkMobilenumber() | !checkFullName() | !checkAge() | !checkAddress() | !checkDistrict()) {
                binding.scrollView2.smoothScrollTo(0, 0);
                Snackbar.make(binding.scrollView2, "Please fill all the details", Snackbar.LENGTH_SHORT);
            } else {
                Intent intent = new Intent(RegisterActivity.this,WeatherActivity.class);
                intent.putExtra("city",binding.districText.getText().toString().trim());

                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }

        });
        binding.checkPincodeButton.setOnClickListener(view ->
            checkPincode()
        );

    }


    private void checkTextView() {
        binding.mobileNumberText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.mobileNumberTextLayout.setEndIconVisible(editable.toString().trim().length() == 10);
                binding.mobileNumberTextLayout.setError(null);
            }
        });

        binding.fullNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.fullNameTextLayout.setEndIconVisible(editable.toString().trim().length() > 5);
                binding.fullNameTextLayout.setError(null);
            }
        });
        binding.addressLine1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.addressLine1TextLayout.setEndIconVisible(editable.toString().trim().length() >= 3);
                binding.addressLine1TextLayout.setError(null);
            }
        });
    }

    private boolean checkAge() {
        String Age = binding.ageText.getText().toString().trim();
        if (Age.equals("")) {
            binding.dateTextLayout.setError("Date can't be empty");
            binding.setIsMargin(false);
            return false;
        }
        binding.setIsMargin(true);
        binding.mobileNumberTextLayout.setError(null);
        return true;
    }

    private boolean checkMobilenumber() {
        String MobileNumber = Objects.requireNonNull(binding.mobileNumberText.getText()).toString().trim();
        if (MobileNumber.equals("")) {
            binding.mobileNumberTextLayout.setError("Mobile number can't be empty");
            return false;
        } else if (MobileNumber.length() < 10) {
            binding.mobileNumberTextLayout.setError("Please enter a valid mobile number");
            return false;
        }
        binding.mobileNumberTextLayout.setError(null);
        return true;
    }

    public void checkDate() {
        boolean isMargin;
        if (Objects.requireNonNull(binding.date.getText()).toString().trim().length() > 5) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("ddMMyyyy");

            Date date = null;
            try {
                date = format.parse(String.valueOf(binding.date.getText()));

                System.out.println(date);
                isMargin = true;

            } catch (ParseException e) {
                try {
                    date = format1.parse(String.valueOf(binding.date.getText()));
                    isMargin = true;
                } catch (ParseException parseException) {
                    try {
                        date = format2.parse(String.valueOf(binding.date.getText()));
                        isMargin = true;
                    } catch (ParseException exception) {
                        binding.date.setText("");
                        isMargin = false;
                        binding.dateTextLayout.setError("Please type a valid date(dd-mm-yyyy , dd/mm/yyyy or ddmmyyyy)");

                        exception.printStackTrace();
                    }

                }

            }
            if (isMargin) {
                binding.dateTextLayout.setError(null);
            }
            if (date != null) {
                calculateAge(date);
            }
            binding.setIsMargin(isMargin);
        } else {
            binding.dateTextLayout.setError("Please type a valid date(dd-mm-yyyy , dd/mm/yyyy or ddmmyyyy)");
            binding.setIsMargin(false);
        }


    }

    private void calculateAge(Date date) {
        Log.d("TAG", "calculateAge: " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int userYear = calendar.get(Calendar.YEAR);
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        Log.d("TAG", "calculateAge: " + actualYear + " " + userYear);
        binding.ageText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
        if (actualYear-userYear>=0) {
            binding.ageText.setText(String.valueOf(actualYear - userYear));
        }else {
            binding.dateTextLayout.setError("Please type a valid date(dd-mm-yyyy , dd/mm/yyyy or ddmmyyyy)");
        }

    }

    private boolean checkAddress() {
        String address = Objects.requireNonNull(binding.addressLine1.getText()).toString().trim();
        if (address.equals("")) {
            binding.addressLine1TextLayout.setError("Address can't be empty");
            return false;
        } else if (address.length() < 3) {
            binding.addressLine1TextLayout.setError("Address can't have less that 3 characters");
            return false;
        }
        binding.addressLine1TextLayout.setError(null);
        return true;
    }

    private boolean checkFullName() {
        String FullName = Objects.requireNonNull(binding.fullNameText.getText()).toString().trim();
        if (FullName.equals("")) {
            binding.fullNameTextLayout.setError("Full Name can't be empty");
            return false;
        } else if (FullName.length() < 6) {
            binding.fullNameTextLayout.setError("Full Name can't have less than 6 characters");
            return false;
        }
        binding.fullNameTextLayout.setError(null);
        return true;
    }

    private boolean checkDistrict() {
        String District = binding.districText.getText().toString().trim();
        if (District.equals("")) {
            binding.pincodeTextLayout.setError("check the pincode");
            binding.pincodeTextLayout.setEndIconVisible(false);
            return false;
        }
        binding.pincodeTextLayout.setEndIconVisible(true);
        binding.pincodeTextLayout.setError(null);
        return true;
    }

    private void checkPincode() {
        String Pincode = Objects.requireNonNull(binding.pincodeText.getText()).toString().trim();

        if (Pincode.length() == 6) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setView(R.layout.dialogue_layout);
            AlertDialog dialog = alertDialog.create();
            dialog.show();

            new Repository(Pincode, AppController.getInstance().getResourceProvider()).getPincode((district, state) -> {

                if (district.equals("") || state.equals("")) {
                    binding.pincodeTextLayout.setError("check the pincode");
                    binding.pincodeTextLayout.setEndIconVisible(false);
                    binding.districText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.state.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    binding.districText.setText(district);
                    binding.state.setText(state);
                    binding.pincodeTextLayout.setEndIconVisible(true);
                    binding.districText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
                    binding.state.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
                    binding.pincodeTextLayout.setError(null);
                }
                dialog.dismiss();
            });

        }



    }

    private void setEndIconInvisible() {
        binding.mobileNumberTextLayout.setEndIconVisible(false);
        binding.fullNameTextLayout.setEndIconVisible(false);
        binding.addressLine1TextLayout.setEndIconVisible(false);
        binding.pincodeTextLayout.setEndIconVisible(false);
        binding.setIsMargin(true);

    }

    private void enableCounter() {
        binding.mobileNumberText.setOnFocusChangeListener((view, b) -> {
            binding.mobileNumberTextLayout.setCounterEnabled(b);
        });

        binding.fullNameText.setOnFocusChangeListener((view, b) -> {
            binding.fullNameTextLayout.setCounterEnabled(b);
        });
        binding.addressLine1.setOnFocusChangeListener((view, b) -> {
            binding.addressLine1TextLayout.setCounterEnabled(b);
        });
        binding.addressLine2.setOnFocusChangeListener((view, b) -> {
            binding.addressLine2TextLayout.setCounterEnabled(b);
        });
        binding.pincodeText.setOnFocusChangeListener((view, b) -> {
            binding.pincodeTextLayout.setCounterEnabled(b);
        });

    }

}