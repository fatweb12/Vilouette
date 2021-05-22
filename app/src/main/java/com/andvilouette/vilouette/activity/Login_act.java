package com.andvilouette.vilouette.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.TextView;

import com.andvilouette.vilouette.Connectivity.RestClient;
import com.andvilouette.vilouette.DataObject.User;
import com.andvilouette.vilouette.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Login_act extends AppCompatActivity {
    AppCompatButton btn_login;
    AppCompatTextView tvforgotPass;
    TextView SignUP;
    ProgressDialog dialog;
    EditText etname, edt_passwrd;
    
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_login_act);
        etname = (EditText) findViewById(R.id.etName);
        edt_passwrd = (EditText) findViewById(R.id.etPassword);
        btn_login = findViewById(R.id.login);
        tvforgotPass= findViewById(R.id.tvForgotPassword);

        btn_login.setOnClickListener(v -> {

            attemptLogin();
        });
        tvforgotPass.setOnClickListener(v -> {
            Intent in = new Intent(getApplicationContext(), Forgotpassword_act.class);
            startActivity(in);
        });


        SignUP = findViewById(R.id.signup);
        SignUP.setOnClickListener(v -> {
            Intent in = new Intent(getApplicationContext(), Signup_Act.class);
            startActivity(in);
        });
    }

    private void attemptLogin() {

        etname = (EditText) findViewById(R.id.etName);
        edt_passwrd = findViewById(R.id.etPassword);

        String username = etname.getText().toString().trim();
        String password = edt_passwrd.getText().toString().trim();


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
            focusView = etname;
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
                    (dialog, which) -> {

                    });
            focusView = edt_passwrd;
            cancel = true;
        } else {
            System.out.println("!Valid");
        }


        if (cancel) {
            focusView.requestFocus();
        } else {

            new UserLogin(username, password).execute();

        }
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    @SuppressLint("StaticFieldLeak")
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
            dialog = new ProgressDialog(Login_act.this);
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

                String str_url = Login_act.this.getString(R.string.url_live);
                String normalUrl = str_url + "account/Api/login";

                // String normalUrl = "http://onestoptrade.co.nz/api/users/login";
                android.util.Log.i("login", normalUrl + "?" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("username", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Login_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(Login_act.this);
            builder.setMessage(newUser.getMessage());
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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

                    etname.setText("");
                    edt_passwrd.setText("");

                }
            });
            builder.show();

           } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login_act.this);
               // builder.setTitle("Login error!");
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