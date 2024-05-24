package com.chat.app;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("process_chat")
    Call<ChatModel> getChatAnswer(@Body RequestBody params);
}
