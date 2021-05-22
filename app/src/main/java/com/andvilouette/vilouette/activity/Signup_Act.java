package com.andvilouette.vilouette.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andvilouette.vilouette.Connectivity.RestClient;
import com.andvilouette.vilouette.DataObject.User;
import com.andvilouette.vilouette.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Signup_Act extends AppCompatActivity {

    EditText edt_username, edt_passwrd, edt_email, edt_website, edt_Phone, edt_lastname, edt_confrmpassword;
    Button btn_register;
    ProgressDialog dialog;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_signup_);

        edt_username = findViewById(R.id.etName);
        edt_passwrd = findViewById(R.id.etPassword);
        edt_email = findViewById(R.id.etEMail);
        edt_confrmpassword = findViewById(R.id.etConfirmPassword);
        btn_register = findViewById(R.id.btn_register);

        edt_Phone = findViewById(R.id.etPHn);
        // back = findViewById(R.id.back);
       /* back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn_act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                attemptLogin();

            }
        });

    }


    private void attemptLogin() {

        edt_username = (EditText) findViewById(R.id.etName);
        edt_passwrd = (EditText) findViewById(R.id.etPassword);

        String username = edt_username.getText().toString().trim();
        String password = edt_passwrd.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String phn = edt_Phone.getText().toString().trim();
        String Confirm_password = edt_confrmpassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.etPassword)).setError("This field is required");
            focusView = edt_passwrd;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            ((EditText) findViewById(R.id.etName)).setError("This field is required");
            focusView = edt_username;
            cancel = true;
        }

        if (password.length() < 8 && !isValidPassword(password)) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setTitle("Wrong password Format");
            String msg1 = "# must contains one lowercase characters";
            String msg2 = "# must contains one uppercase characters";
            String msg3 = "# must contains one digit from 0-9,";
            String msg4 = "#  must contains one special symbols in the list \\\"@#$%\\\"";
            String msg5 = "# length at least 8 characters";


            dlgAlert.setMessage(msg1 + "\n" + msg2 + "\n" + msg3 + "\n" + msg4 + "\n" + msg5);


            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            focusView = edt_passwrd;
            cancel = true;
        } else {
            System.out.println("!Valid");
        }
        if (Confirm_password.length() < 8 && !isValidPassword(Confirm_password)) {
            System.out.println("Not Valid");
        } else {
            System.out.println("@Valid");
        }

        if (TextUtils.isEmpty(Confirm_password)) {
            ((EditText) findViewById(R.id.etConfirmPassword)).setError("This field is required");
            focusView = edt_confrmpassword;
            cancel = true;
        }


        if (!password.equalsIgnoreCase(Confirm_password)) {
            ((EditText) findViewById(R.id.etPassword)).setError("Password does not match!");
            focusView = edt_passwrd;
            cancel = true;
        }


        if (!Confirm_password.equalsIgnoreCase(password)) {
            ((EditText) findViewById(R.id.etConfirmPassword)).setError("Password does not match!");
            focusView = edt_confrmpassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Log.d("VAlues", username + password + email + phn);
            new UserSignUp(username, password,Confirm_password, email, phn).execute();

        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        /*  final String PASSWORD_PATTERN = ("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");*/
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public class UserSignUp extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mConPassword;

        private final String mEmail;
        private final String mPHone;


        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserSignUp(String username, String password, String confirmpassword,String email, String phone) {
            mUsername = username;
            mPassword = password;
            mConPassword = confirmpassword;

            mEmail = email;
            mPHone = phone;

            newUser.setEmailAddress(mEmail);
            newUser.setPassword(mPassword);
            newUser.setUsername(mUsername);
            newUser.setUser_phn(mPHone);
            newUser.setConfirmpassword(mConPassword);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Signup_Act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("email", newUser.getEmailAddress());
                params.put("password", newUser.getPassword());

                String str_url = Signup_Act.this.getString(R.string.url_live);
                String normalUrl = str_url + "account/Api/signUp";

                //  String normalUrl = "https://wedding.geek.fatweb.co.nz/api/users/register";
                android.util.Log.i("login", normalUrl);


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());
                jObject.accumulate("confirm_password", newUser.getConfirmpassword());

                jObject.accumulate("name", newUser.getUsername());
                jObject.accumulate("phone", newUser.getUser_phn());


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Signup_Act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("error")) {
                                if (response.getString("error").contains("true")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);
                             /*   prefs.put(RestoAllergyApplication.thisUser, new Gson().toJson(newUser));
                                prefs.put(RestoAllergyApplication.VERSION, newUser.getAndroidVersion());
                                android.util.Log.i("prefs", prefs.getString(RestoAllergyApplication.thisUser));*/
                            } else {
                                success = false;

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        android.util.Log.i("JSONArray response", response.toString());
                        success = true;
                    }
                });

                return success;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);
            if (success) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Signup_Act.this);
                //  builder.setTitle("Registration successful");
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        new UserLogin(edt_email.getText().toString(), edt_passwrd.getText().toString()).execute();


                    }
                });
                builder.show();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Signup_Act.this);
                builder.setTitle("Registration error!");
                builder.setMessage(newUser.getMessage());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


            }
        }
    }

    public class UserLogin extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserLogin(String username, String password) {
            mUsername = username;
            mPassword = password;
            newUser.setEmailAddress(mUsername);
            newUser.setPassword(mPassword);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Signup_Act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("username", newUser.getEmailAddress());
                params.put("password", newUser.getPassword());

                String str_url = Signup_Act.this.getString(R.string.url_live);
                String normalUrl = str_url + "account/Api/login";

                // String normalUrl = "http://onestoptrade.co.nz/api/users/login";
                android.util.Log.i("login", normalUrl + "?" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("username", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Signup_Act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("error")) {
                                if (response.getString("error").contains("true")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);


                            } else {
                                success = false;

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        android.util.Log.i("JSONArray response", response.toString());
                        success = true;
                    }
                });

                return success;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);
            if (success) {
                String username = newUser.getUsername();
                String passwrd = newUser.getPassword();
                String email = newUser.getEmailAddress();

                String userid = newUser.getId();

                // Toast.makeText(SignIn_act.this, "" + username + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", username);
                editor.putString("password", passwrd);
                editor.putString("email", email);
                editor.putString("customerID", userid);

                editor.apply();
                Intent intent = new Intent(getApplicationContext(), Homenav_act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                edt_username.setText("");
                edt_passwrd.setText("");
               /* AlertDialog.Builder builder = new AlertDialog.Builder(Signup_Act.this);
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();*/

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Signup_Act.this);
                builder.setTitle("Login error!");
                builder.setMessage(newUser.getMessage());

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        }


    }
}