package com.example.mcpro;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mcpro.ChatMessage;
import com.example.mcpro.ChatAdapter;
public class ChatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    EditText messageInput;
    Button sendButton;
    List<ChatMessage> chatMessages = new ArrayList<>();
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String chatId = getIntent().getStringExtra("chat_id");

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        adapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                chatMessages.add(new ChatMessage(text, true)); // user message
                chatMessages.add(new ChatMessage("Response to: " + text, false)); // fake counsellor response
                adapter.notifyItemRangeInserted(chatMessages.size() - 2, 2);
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                messageInput.setText("");
            }
        });
    }

    void sendMessage(String chatID, String message , String senderType){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lamp.ms.wits.ac.za/home/s2808870/send_message.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST,url,response->{},error->{}){
            @Override
            protected Map<String , String> getParams(){
                Map<String , String> params = new HashMap<>();
                params.put("chat_ID" , chatID);
                params.put("message" , message);
                params.put("sender_type",senderType);
                return params;
            }
        };
        queue.add(postRequest);

    }

    void fetchMessage(String chatID){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lamp.ms.wits.ac.za/home/s2808870/get_message.php?chat_id="+chatID;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET , url , null,
                response->{
                    chatMessages.clear();
                    for(int i = 0 ; i < response.length() ; i++){
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String msg = obj.getString("message");
                            boolean isUser = obj.getString("sender_type").equals("user");
                            chatMessages.add(new ChatMessage(msg,isUser));
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error->{}
        );
        queue.add(request);
    }
}