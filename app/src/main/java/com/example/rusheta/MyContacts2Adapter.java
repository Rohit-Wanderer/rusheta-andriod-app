package com.example.rusheta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyContacts2Adapter extends RecyclerView.Adapter {

    private List<Contacts2> contactList;
    private Context context;
    SignalProtocolKeyGen signalProtocolKeyGen;

    MyContacts2Adapter(Context context, List<Contacts2> contactList, SignalProtocolKeyGen signalProtocolKeyGen) {
        this.contactList = contactList;
        this.context = context;
        this.signalProtocolKeyGen = signalProtocolKeyGen;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new MyContacts2Adapter.ContactTextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contacts2 contact = contactList.get(position);
        ((MyContacts2Adapter.ContactTextHolder) holder).bind(contact);

        ((ContactTextHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                String name = contactList.get(position).getName();
                String number = contactList.get(position).getPhone();
                String contactId = contactList.get(position).getContactId();
                String identityKey = contactList.get(position).getIdentityKey();
                String ephemeralKey = contactList.get(position).getEphemeralKey();
                String signature = contactList.get(position).getSignature();

                try {
                    PublicKey identityKeyPublicKey = (PublicKey) ObjectSerializationClass.getObjectFromString(identityKey);
                    PublicKey ephemeralKeyPublicKey = (PublicKey) ObjectSerializationClass.getObjectFromString(ephemeralKey);

                    Log.i("ServerSignature", signature);
                    Log.i("ServerSignature", identityKeyPublicKey.toString());
                    Log.i("ServerSignature", ephemeralKeyPublicKey.toString());
                    if (signalProtocolKeyGen.verifyKey(identityKeyPublicKey, ephemeralKeyPublicKey, signature)) {
                        byte[] AESKey = signalProtocolKeyGen.getAESKey(identityKeyPublicKey, ephemeralKeyPublicKey);
                        String AESKeyString = new String(Base64.encode(AESKey, Base64.DEFAULT));
                        Log.i("AESKeyString::::::::::::::::::::::", AESKeyString);
                        Chat chat = new Chat(contactId, number, name, AESKeyString);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Chat", chat);
                        ((Activity) context).setResult(RESULT_OK, returnIntent);
                        ((Activity) context).finish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }

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

        void bind(Contacts2 contact) {
            contactText.setText(contact.getName());
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
