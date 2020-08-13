package com.example.rusheta;

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

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MyContactsAdapter extends RecyclerView.Adapter {

    private ArrayList<ContactDTO> contactList;
    private Context context;


    MyContactsAdapter(Context context, ArrayList<ContactDTO> contactList){
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new MyContactsAdapter.ContactTextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactDTO contact = (ContactDTO) contactList.get(position);
        ((MyContactsAdapter.ContactTextHolder) holder).bind(contact);

        ((ContactTextHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                String name = contactList.get(position).getDisplayName();
                String number = contactList.get(position).getPhoneList().get(0).getDataValue();
                String contactId = String.valueOf(contactList.get(position).getContactId());
                String AESKey = "Dummy value";
                Chat chat = new Chat(contactId, number, name, AESKey);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Chat", chat);
                ((Activity) context).setResult(RESULT_OK,returnIntent);
                ((Activity) context).finish();
            }
        });
    }

    private class ContactTextHolder extends RecyclerView.ViewHolder{

        TextView contactText;
        ConstraintLayout parentLayout;
        ContactTextHolder(View itemView){
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            contactText = (TextView) itemView.findViewById(R.id.text_contact_name);
        }

        void bind(ContactDTO contact) {
            contactText.setText(contact.getDisplayName());
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
