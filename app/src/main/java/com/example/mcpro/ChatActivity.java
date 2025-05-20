package com.example.mcpro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    EditText messageInput;
    Button sendButton;
    List<ChatMessage> chatMessages = new ArrayList<>();
    ChatAdapter adapter;
    String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("chat_id");

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        adapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        fetchMessage(chatId); // Load existing messages

        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                // Optimistically show user message
                chatMessages.add(new ChatMessage(text, true));
                adapter.notifyItemInserted(chatMessages.size() - 1);
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

                sendMessage(chatId, text, "user"); // Send to backend
                messageInput.setText("");

                // Re-fetch messages from server to get counselor response
                fetchMessage(chatId);
            }
        });
    }

    void sendMessage(String chatID, String message, String senderType) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lamp.ms.wits.ac.za/home/s2808870/send_message.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Optional: Toast or log success
                },
                error -> {
                    Toast.makeText(ChatActivity.this, "Failed to send message.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chat_ID", chatID);
                params.put("message", message);
                params.put("sender_type", senderType);
                return params;
            }
        };
        queue.add(postRequest);
    }

    void fetchMessage(String chatID) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lamp.ms.wits.ac.za/home/s2808870/get_message.php?chat_id=" + chatID;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    chatMessages.clear(); // Clear old messages to avoid duplication
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String msg = obj.getString("message");
                            boolean isUser = obj.getString("sender_type").equals("user");
                            chatMessages.add(new ChatMessage(msg, isUser));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                },
                error -> {
                    Toast.makeText(ChatActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
    }
}
