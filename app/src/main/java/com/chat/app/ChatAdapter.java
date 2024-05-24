package com.chat.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<ChatModel> arrayChatModel;


    public ChatAdapter(Context context, ArrayList<ChatModel> arrayChatModel) {
        this.context = context;
        this.arrayChatModel = arrayChatModel;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.raw_chat_layout, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (arrayChatModel.get(position).getFlag().equalsIgnoreCase("U")) {
            holder.tvMsgMy.setText(arrayChatModel.get(position).getMessage().trim());
            holder.my.setVisibility(View.VISIBLE);
            holder.their.setVisibility(View.GONE);
        } else {
            holder.tvMsgTheir.setText(arrayChatModel.get(position).getMessage().trim());
            holder.my.setVisibility(View.GONE);
            holder.their.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayChatModel.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        TextView tvMsgMy,tvMsgTheir;
        View their, my;

        public Holder(@NonNull View itemView) {
            super(itemView);
            their = itemView.findViewById(R.id.their);
            my = itemView.findViewById(R.id.my);
            tvMsgMy = itemView.findViewById(R.id.message_body_my);
            tvMsgTheir = itemView.findViewById(R.id.message_body_their);
        }
    }
}
