package algonquin.cst2335.kadv0001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import algonquin.cst2335.kadv0001.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.kadv0001.databinding.SentMessageBinding;
import algonquin.cst2335.kadv0001.databinding.ReceiveMessageBinding;

public class ChatRoom extends AppCompatActivity {

    // Binding class instance for accessing views in activity_chat_room.xml layout
    private ActivityChatRoomBinding binding;

    // ViewModel instance for managing UI-related data in a lifecycle-conscious way
    private ChatRoomViewModel chatModel;

    // Adapter for the RecyclerView to manage data collection and bind it to the view
    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    // Formatting
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Replace the contents of a view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initializing ViewModel
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        // Adapter setup for RecyclerView
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            // Inflating appropriate layout for message
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Check the viewType to determine which message layout to inflate
                if (viewType == 0) { // Layout for sent messages
                    SentMessageBinding sentBinding = SentMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                    return new MyRowHolder(sentBinding.getRoot());
                } else { // Layout for received messages
                    ReceiveMessageBinding receiveBinding = ReceiveMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                    return new MyRowHolder(receiveBinding.getRoot());
                }
            }

            // Binding message data to ViewHolder / Replaceing the contents of a view
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // Get the message at this position
                ChatMessage message = chatModel.messages.getValue().get(position);
                // Bind the message text and timestamp to the TextViews
                holder.messageText.setText(message.getMessage());
                holder.timeText.setText(message.getTimeSent());
            }

            // Determining number of items
            @Override
            public int getItemCount() {
                return chatModel.messages.getValue().size();
            }

            // Determiningg type of view to inflate
            @Override
            public int getItemViewType(int position) {
                ChatMessage message = chatModel.messages.getValue().get(position);
                return message.isSentButton() ? 0 : 1; // 0 for sent, 1 for received
            }
        };

        // Setting up RecyclerView / Set the adapter for the RecyclerView and define its layout manager
        binding.recycleView.setAdapter(myAdapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        // Sending button click listener
        binding.sendButton.setOnClickListener(v -> {
            sendMessage(true); // Sending a message
        });

        // Receiving button click listener
        binding.receiveButton.setOnClickListener(v -> {
            sendMessage(false); // Receiving a message
        });
    }

    // Send or receive messages
    private void sendMessage(boolean isSent) {
        // Get the message text from the input field
        String messageText = binding.textInput.getText().toString();
        if (!messageText.isEmpty()) {
            String currentDateandTime = sdf.format(new Date());
            // Create a new ChatMessage object with the message text, timestamp, and sent/received status
            ChatMessage newMessage = new ChatMessage(messageText, currentDateandTime, isSent);
            // Add the new message to the ViewModel
            chatModel.addMessage(newMessage);
            // Notify the adapter that an item was inserted to refresh the view
            myAdapter.notifyItemInserted(chatModel.messages.getValue().size() - 1);
            binding.textInput.setText("");
        }
    }

    // ViewHolder class for RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        // Constructor that gets the TextViews from the itemView
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing the TextViews using findViewById
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}
