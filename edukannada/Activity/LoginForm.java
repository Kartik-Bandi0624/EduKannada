package com.example.edukannada.Activity;


import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.edukannada.Application.ConnectivityReceiver;
import com.example.edukannada.HelperClass.GlobalServerUrl;
import com.example.edukannada.HelperClass.Manager;
import com.example.edukannada.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;


public class LoginForm extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    Button btnLogin;
    EditText editEmail, editPassword;
    TextView forgetPassword, login, register, signupNow;
    String username, password;
    AlertDialog dialog = null;
    String user_role;
    //    String ref_no;
    CheckBox rememberMe;
    AppUpdateManager appUpdateManager;
    int APP_UPDATE = 10;
//    SharedPreferences sharedPreferences;

    private static final Pattern PASSWORD_PATTERN=Pattern.compile(
            "(?=.*[0-9])"+
            "(?=.*[a-z])"+
            "(?=.*[A-Z])"+
            "(?=.*[@#$%^&+=])"+
            "(?=\\S+$)"+
            ".{6,12}");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        editEmail = findViewById(R.id.emailAddress);
        editPassword = findViewById(R.id.passwordMain);
        rememberMe = findViewById(R.id.remember_me);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        signupNow = findViewById(R.id.signup_now);

        login.setOnClickListener(v -> startActivity(new Intent(LoginForm.this, LoginForm.class)));

        register.setOnClickListener(v -> startActivity(new Intent(LoginForm.this, CreateAccount.class)));

        signupNow.setOnClickListener(v -> startActivity(new Intent(LoginForm.this, CreateAccount.class)));

        SharedPreferences pref = getSharedPreferences(Manager.PREF_NAME, 0);
        boolean checkRemember = pref.getBoolean(Manager.remember_Me, false);
        String user_name = pref.getString(Manager.Username, null);
        String pass_word = pref.getString(Manager.password, null);

        if (checkRemember) {
            editEmail.setText(user_name);
            editPassword.setText(pass_word);
            rememberMe.setChecked(true);
        }

        btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(v -> {
            username = editEmail.getText().toString();
            password = editPassword.getText().toString();
            //validation
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                editEmail.setError("enter email id");
                editEmail.requestFocus();
            } else if (password.isEmpty()) {
                editPassword.setError("entered weak password");
                editPassword.requestFocus();
            } else {
                showProgress();
                ValidateUser(username, password);
                startActivity(new Intent(LoginForm.this, Dashboard.class));
            }
        });

        forgetPassword = findViewById(R.id.forget_password);
        forgetPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginForm.this, ForgetPassword.class);
            startActivity(intent);
        });

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.i("updateInfo", "appUpdateInfo.updateAvailability() = " + appUpdateInfo.updateAvailability());
            Log.i("updateInfo", "appUpdateInfo.getResult().toString() = " + appUpdateInfo);

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE, this, APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("AppAvailable", "onCreate: not available");
            }
        });
    }

    //rest service for login
    public void ValidateUser(final String username, final String password) {
        final String url = GlobalServerUrl.url + "login";

        RequestParams params = new RequestParams();

        params.add("emailId", username);
        params.add("password", password);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 202) {
                    try {
                        JSONObject userinfo = new JSONObject(new String(bytes));
                        user_role = userinfo.getString("user_role");
                        String mobile_no = userinfo.getString("mobile_no");
                        String referred_by = userinfo.getString("referred_by");
                        Log.d("caste", "onSuccess: " + userinfo);
                        Manager Manager = new Manager(getApplicationContext());
                        Manager.createLoginSession(
                                userinfo.getString("userid"),
                                userinfo.getString("user_name"),
                                userinfo.getString("user_role"),
                                userinfo.getString("ref_no"),
                                userinfo.getString("BGName"),
                                userinfo.getString("caste"),
                                mobile_no, referred_by);
                        Log.d("caste", "onSuccess: " + userinfo.getString("caste"));
                        String status = userinfo.getString("status");
                        dismissDialog();

                        if ((user_role.equalsIgnoreCase("PremiumUser") || user_role.equalsIgnoreCase("SilverUser")) && status.equalsIgnoreCase("active")) {
                            Manager.loginPreference(username, password, rememberMe.isChecked(), true);
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (user_role.equalsIgnoreCase("FreeUser") && status.equalsIgnoreCase("active")) {
                            dismissDialog();

                            Manager.loginPreference(username, password, rememberMe.isChecked(), false);

                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.putExtra("referred_by", referred_by);
                            intent.putExtra("mobile_no", mobile_no);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Free user can not use android app", Toast.LENGTH_LONG).show();

                        } else if (status.equalsIgnoreCase("expires")) {
                            Manager.loginPreference(username, password, rememberMe.isChecked(), false);

                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.putExtra("referred_by", referred_by);
                            intent.putExtra("mobile_no", mobile_no);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (i == 204) {
                    dismissDialog();
                    Toast.makeText(getApplicationContext(), "User name and password is not valid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                System.out.println(i + "status ");
//                dismissDialog();
//                if (i >= 500 || i == 0) {
//                    Toast.makeText(getApplicationContext(), "Server error, try after some time" + GlobalServerUrl.url + "login", Toast.LENGTH_LONG).show();
//                } else if (i >= 400) {
//                    Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("app update", "onActivityResult: app download failed");
                Toast.makeText(getApplicationContext(), "EduKannada needs an update", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

//    private void popupSnackBarForCompleteUpdate() {
//
//        Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), "New app is ready!", Snackbar.LENGTH_INDEFINITE);
//
//        snackbar.setAction("Install", view -> {
//            if (appUpdateManager != null) {
//                appUpdateManager.completeUpdate();
//            }
//        });
//        snackbar.setActionTextColor(getResources().getColor(R.color.white));
//        snackbar.show();
//    }


    private void showSnack(boolean isConnected) {
        String message;
        int color;
        int backgroundColor;

        if (isConnected) {
            message = "Back Online";
            color = Color.WHITE;
            backgroundColor = ContextCompat.getColor(this, R.color.linear);

        } else {
            message = "No Connection";
            color = Color.WHITE;
            backgroundColor = Color.BLACK;
        }
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(backgroundColor);
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(color);
        snackbar.show();
    }

    @SuppressLint("SetTextI18n")
    private void showProgress() {
        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);


        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Please wait...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(18);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setAttributes(layoutParams);

            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            Log.d("dialog", "dismissDialog: " + "dismiss");
            Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


//    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
//        @Override
//        public void onStateUpdate(InstallState state) {
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                popupSnackBarForCompleteUpdate();
//            } else if (state.installStatus() == InstallStatus.INSTALLED) {
//                if (appUpdateManager != null) {
//                    appUpdateManager.unregisterListener(installStateUpdatedListener);
//                }
//            } else {
//                Log.i("", "InstallStateUpdatedListener: state: " + state.installStatus());
//            }
//        }
//    };
}

//    TextView logIn, register, forgetPassword, signupNow;
//    EditText email_address, password_main;
//    Button loginBtn;
//    SharedPreferences sharedPreferences;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_form);
//
//        logIn = findViewById(R.id.login);
//        register = findViewById(R.id.register);
//        forgetPassword = findViewById(R.id.forget_password);
//        signupNow = findViewById(R.id.signup_now);
//        loginBtn = findViewById(R.id.login_btn);
//        email_address = findViewById(R.id.emailAddress);
//        password_main = findViewById(R.id.passwordMain);
//
//        logIn.setOnClickListener(v -> {
//            startActivity(new Intent(LoginForm.this, LoginForm.class));
//        });
//
//        forgetPassword.setOnClickListener(v -> {
//            startActivity(new Intent(LoginForm.this, ForgetPassword.class));
//        });
//
//        register.setOnClickListener(v -> {
//            startActivity(new Intent(LoginForm.this, CreateAccount.class));
//        });
//
//        signupNow.setOnClickListener(v -> {
//            startActivity(new Intent(LoginForm.this, CreateAccount.class));
//        });
//
//        sharedPreferences = getSharedPreferences("Login_from", MODE_PRIVATE);
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Loading..");
//        progressDialog.setMessage("Please wait..");
//
//        loginBtn.setOnClickListener(v -> {
//            progressDialog.show();
//
//            String email = email_address.getText().toString();
//            String password1 = password_main.getText().toString();
//
//            if (email.isEmpty()) {
//                email_address.setError("enter email id");
//                email_address.requestFocus();
//            } else if (password1.isEmpty()) {
//                password_main.setError("enter password");
//                password_main.requestFocus();
//            }
//            progressDialog.hide();
//            if ((email.equals("kartikbandi123@gmail.com") && password1.equals("k123"))
//                    || (email.equals("kartikkv246@gmail.com") && password1.equals("kv246"))
//                    || (email.equals("kartikashu124@gmail.com") && password1.equals("ka124"))
//                    || (email.equals("kartikbandi163@gmail.com") && password1.equals("k163"))) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("email", email);
//                editor.putString("password1", password1);
////                editor.commit();
//                /* OR */
//                editor.apply();
//                startActivity(new Intent(LoginForm.this, Dashboard.class));
//                Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Invalid email id or password", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//}