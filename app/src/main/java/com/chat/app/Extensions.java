package com.chat.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class Extensions {

    static String USERDATA = "userData";
    static String USERNAME = "username";

    /**
     * This Extension has been using for showing Toast messages
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This Extension has been using for hide keyboard
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This Extension has been using for set padding for safe area
     */
    public static void adjustFullScreen(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * This method is used to check if a string is empty or null.
     */
    public static boolean checkEmptyString(Object obj) {
        if (obj == null)
            return true;
        else
            return TextUtils.isEmpty(obj.toString().trim());
    }

    /**
     * This method is used for validating email id
     */
    public static boolean checkEmailPattern(Object obj) {
        if (obj == null) return false;
        return !Patterns.EMAIL_ADDRESS.matcher(obj.toString().trim()).matches();
    }

    /**
     * This method is used for show validation errors
     */
    public static void showError(TextInputLayout inputLayout, String errorMessageRes) {
        inputLayout.setErrorEnabled(true);
        inputLayout.setError(errorMessageRes);
    }

    /**
     * This method is used for remove validation errors
     */
    public static void clearError(TextInputLayout inputLayout) {
        inputLayout.setErrorEnabled(false);
        inputLayout.setError(null);
    }

    /**
     * This function is used for checking internet connection
     */
    public static boolean isConnect(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    isConnected = true;
                }
            }
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        if (!isConnected) {
//            hideProgress(); // Assuming HideProgress and DebugToast are methods in your context
            showToast(context, context.getString(R.string.check_internet));
        }
        return isConnected;
    }

    private static ProgressDialog dialog;

    /**
     * This function is used for showing a progress dialog
     */
    public static void showProgress(Context context) {
        dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage(context.getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * This function is used for hiding the progress dialog
     */
    public static void hideProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}