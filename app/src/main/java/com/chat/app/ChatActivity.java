package com.chat.app;

import static com.chat.app.Extensions.adjustFullScreen;
import static com.chat.app.Extensions.hideProgress;
import static com.chat.app.Extensions.isConnect;
import static com.chat.app.Extensions.showProgress;
import static com.chat.app.Extensions.showToast;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chat.app.databinding.ActivityChatBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    ChatAdapter mAdapter;
    ArrayList<ChatModel> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        setUpView();
        manageClicks();
        getOnBackPressedDispatcher().addCallback(callback);
    }


    private void setUpView() {
        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(this, chatList);
        binding.rvMessageview.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessageview.setAdapter(mAdapter);
    }

    private void manageClicks() {
        binding.ivSend.setOnClickListener(v -> {
            String message = binding.editText.getText().toString().trim();
            if (TextUtils.isEmpty(message)) {
                binding.editText.requestFocus();
                return;
            }
            ChatModel chatModel = new ChatModel();
            chatModel.setMessage(message);
            chatModel.setFlag("U");
            chatList.add(chatModel);
            binding.rvMessageview.smoothScrollToPosition(chatList.size() - 1);
            mAdapter.notifyDataSetChanged();

            getChatAnswer();
            binding.editText.setText("");
        });
    }

    private void getChatAnswer() {
        if (isConnect(this)) {
            showProgress(this);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userMessage", binding.editText.getText().toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

            Call<ChatModel> call = RetrofitClient.getInstance().getMyApi().getChatAnswer(body);
            call.enqueue(new Callback<ChatModel>() {
                @Override
                public void onResponse(@NonNull Call<ChatModel> call, @NonNull Response<ChatModel> response) {
                    hideProgress();
                    if (response.code() == 200) {
                        assert response.body() != null;
                        receiver(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChatModel> call, @NonNull Throwable t) {
                    hideProgress();
                    showToast(ChatActivity.this, t.getMessage());
                }
            });
        }
    }

    private void receiver(String message) {
        ChatModel chatModel = new ChatModel();
        chatModel.setMessage(message);
        chatModel.setFlag("R");
        chatList.add(chatModel);
        binding.rvMessageview.smoothScrollToPosition(chatList.size() - 1);
        mAdapter.notifyDataSetChanged();

    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            // Back is pressed... Finishing the activity
            finishAffinity();
        }
    };
}