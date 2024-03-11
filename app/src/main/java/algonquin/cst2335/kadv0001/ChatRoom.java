package algonquin.cst2335.kadv0001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import algonquin.cst2335.kadv0001.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.kadv0001.databinding.SentMessageBinding;
import algonquin.cst2335.kadv0001.databinding.ReceiveMessageBinding;

public class ChatRoom extends AppCompatActivity {

    // Binding for layout and ViewModel for data
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel chatModel;

    // Adapter for RecyclerView
    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    // Formatting
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initializing ViewModel
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        // Initialize your database and DAO (for simplicity, but consider async handling for production)
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, "message_database").allowMainThreadQueries().build();
        ChatMessageDAO cmDAO = db.cmDAO();
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            // Inflating appropriate layout for message
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) { // Layout for sent messages
                    SentMessageBinding sentBinding = SentMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                    return new MyRowHolder(sentBinding.getRoot());
                } else { // Layout for received messages
                    ReceiveMessageBinding receiveBinding = ReceiveMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                    return new MyRowHolder(receiveBinding.getRoot());
                }
            }

            // Binding message data to ViewHolder
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage message = chatModel.messages.getValue().get(position);
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

        // Setting up RecyclerView
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
        String messageText = binding.textInput.getText().toString();
        if (!messageText.isEmpty()) {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(messageText, currentDateandTime, isSent);
            chatModel.addMessage(newMessage);
            myAdapter.notifyItemInserted(chatModel.messages.getValue().size() - 1);
            binding.textInput.setText("");
        }
    }

    // ViewHolder class for RecyclerView
// ViewHolder class for RecyclerView
// ViewHolder class for RecyclerView
    private class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

            // Set click listener for the entire row
            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog, cl) -> {
                            // Do nothing if "No" is clicked
                        })
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            // Perform deletion action
                            if (position != RecyclerView.NO_POSITION) {
                                chatModel.messages.getValue().remove(position);
                                myAdapter.notifyItemRemoved(position);

                                Snackbar.make(itemView, "You deleted message #" + position, Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .create()
                        .show();
            });
        }
    }
    }