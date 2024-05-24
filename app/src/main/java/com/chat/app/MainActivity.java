package com.chat.app;

import static com.chat.app.Extensions.USERDATA;
import static com.chat.app.Extensions.USERNAME;
import static com.chat.app.Extensions.adjustFullScreen;
import static com.chat.app.Extensions.checkEmptyString;
import static com.chat.app.Extensions.clearError;
import static com.chat.app.Extensions.hideKeyboard;
import static com.chat.app.Extensions.showError;
import static com.chat.app.Extensions.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.chat.app.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        manageClicks();
        setUpView();
        getOnBackPressedDispatcher().addCallback(callback);
    }

    private void setUpView() {
        binding.usernameTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.usernameTil));
    }

    private void manageClicks() {
        binding.goCardView.setOnClickListener(v -> {
            if (isValid()) {
                hideKeyboard(this, binding.getRoot());
                    showToast(this, "Login Successfully");
                    sharedPreferences = getSharedPreferences(USERDATA, MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(USERNAME, Objects.requireNonNull(binding.usernameTiet.getText()).toString());
                    editor.commit();
                    startActivity(new Intent(this, ChatActivity.class));
            }
        });
    }

    private boolean isValid() {
        if (checkEmptyString(binding.usernameTiet.getText())) {
            showError(binding.usernameTil, getString(R.string.please_enter_username));
            return false;
        } else {
            clearError(binding.usernameTil);
        }
        return true;
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            // Back is pressed... Finishing the activity
            finishAffinity();
        }
    };
}