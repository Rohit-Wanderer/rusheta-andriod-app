package com.example.rusheta.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rusheta.R;
import com.example.rusheta.service.model.UserMessage;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_TEXT_SENT = 1;
    private static final int VIEW_TYPE_TEXT_RECEIVED = 2;
    private static final int VIEW_TYPE_IMAGE_SENT = 3;
    private static final int VIEW_TYPE_IMAGE_RECEIVED = 4;

    private Context context;
    private ArrayList<UserMessage> messageList;
    private String username;

    public MyListAdapter(Context context, ArrayList<UserMessage> messageList, String username) {
        this.context = context;
        this.messageList = messageList;
        this.username = username;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        UserMessage message = messageList.get(position);

        return message.getMsgType();

    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case VIEW_TYPE_TEXT_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_msg_sent, parent, false);
                return new SentTextHolder(view);
            case VIEW_TYPE_TEXT_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_msg_rec, parent, false);
                return new ReceivedTextHolder(view);
            case VIEW_TYPE_IMAGE_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_img_sent, parent, false);
                return new SentImageHolder(view);
            case VIEW_TYPE_IMAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_img_rec, parent, false);
                return new ReceivedImageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserMessage message = (UserMessage) messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TEXT_SENT:
                ((SentTextHolder) holder).bind(message);
                break;
            case VIEW_TYPE_TEXT_RECEIVED:
                ((ReceivedTextHolder) holder).bind(message);
                break;
            case VIEW_TYPE_IMAGE_SENT:
                ((SentImageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_IMAGE_RECEIVED:
                ((ReceivedImageHolder) holder).bind(message);
        }
    }

    private class SentTextHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentTextHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeReceived());
        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        ImageView imageView;

        SentImageHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(UserMessage message) {
            String image = message.getMessage();
            try {
                byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imageView.setImageBitmap(bitmap);
                timeText.setText(message.getTimeReceived());
            } catch (Exception e) {
                e.getMessage();
            }

        }
    }

    private class ReceivedTextHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedTextHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeReceived());
            nameText.setText(message.getSender());

            // Insert the profile image from the URL into the ImageView.
            //   Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder {
        TextView timeText, nameText;
        ImageView messageImage, profileImage;

        ReceivedImageHolder(View itemView) {
            super(itemView);

            messageImage = itemView.findViewById(R.id.image_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(UserMessage message) {
            String image = message.getMessage();
            try {
                byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                messageImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.getMessage();
            }
            timeText.setText(message.getTimeReceived());
            nameText.setText(message.getSender());
        }
    }

}
