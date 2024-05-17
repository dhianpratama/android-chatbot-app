package com.example.ash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Message> messages;
    private RecyclerView recyclerView;
    private recyclerAdapter adapter;
    private ImageButton sendButton;
    private EditText msgInput;
    private getRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        request = new getRequest(this);

        recyclerView = findViewById(R.id.recyclerView);
        // Set RecyclerView layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Set an animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        messages = new ArrayList<>();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username").toString();

        adapter = new recyclerAdapter(messages, username);
        recyclerView.setAdapter(adapter);

        sendButton = (ImageButton) findViewById(R.id.msgButton);
        msgInput = (EditText) findViewById(R.id.msgInput);
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgInput.getText().toString();
                if (message.length() != 0) {
                    messages.add(new Message(true, message));
                    int newPosition = messages.size() - 1;
                    adapter.notifyItemInserted(newPosition);
                    recyclerView.scrollToPosition(newPosition);
                    msgInput.setText("");
                    getReply(message);
                }
            }
        });


    }

    private void getReply(String message) {
        request.getResponse(message, new getRequest.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("REQUEST ERROR", message);
            }

            @Override
            public void onResponse(String reply) {
                messages.add(new Message(false, reply));
                int newPosition = messages.size() - 1;
                adapter.notifyItemInserted(newPosition);
                recyclerView.scrollToPosition(newPosition);
            }
        });

    }
}