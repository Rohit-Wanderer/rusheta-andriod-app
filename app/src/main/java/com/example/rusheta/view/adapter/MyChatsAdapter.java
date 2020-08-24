package com.example.rusheta.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rusheta.view.ui.ChatActivity;
import com.example.rusheta.R;
import com.example.rusheta.service.model.Chat;

import java.util.ArrayList;

public class MyChatsAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Chat> chatList;

    public MyChatsAdapter(Context context, ArrayList<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new MyChatsAdapter.ContactTextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat contact = chatList.get(position);
        ((MyChatsAdapter.ContactTextHolder) holder).bind(contact);

        ((MyChatsAdapter.ContactTextHolder) holder).parentLayout.setOnClickListener(view -> {
            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            String name = chatList.get(position).getName();
            String number = chatList.get(position).getPhone();
            String contactId = chatList.get(position).getContactId();
            String AESKey = chatList.get(position).getAESKey();
            Chat chat = new Chat(contactId, number, name, AESKey);
            Intent chatIntent = new Intent(context, ChatActivity.class);
            chatIntent.putExtra("Chat",chat);
            ((Activity) context).startActivityForResult(chatIntent,2);
        });
    }

    private class ContactTextHolder extends RecyclerView.ViewHolder{

        TextView contactText;
        ConstraintLayout parentLayout;
        ContactTextHolder(View itemView){
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            contactText = itemView.findViewById(R.id.text_contact_name);
        }

        void bind(Chat contact) {
            contactText.setText(contact.getName());
        }

    }

    @Override
    public int getItemCount() {return chatList.size();    }
}
