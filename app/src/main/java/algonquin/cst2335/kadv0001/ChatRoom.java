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

    // ViewModel object for managing and storing chat data
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

        // Building the Room database and obtainig the DAO for database operations
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, "message_database").allowMainThreadQueries().build();
        ChatMessageDAO cmDAO = db.cmDAO();

        //Defining the adapter for the RecyclerView
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            //Creating new rows for the RecyclerView
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Determining which type of message layout to inflate based on the viewType
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
                // Geting the message from the ViewModel and setting text views according
                ChatMessage message = chatModel.messages.getValue().get(position);
                holder.messageText.setText(message.getMessage());
                holder.timeText.setText(message.getTimeSent());
            }

            // Returning the size of the messages list
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

        //Configuring the RecyclerView with a layout manager and the adapter
        binding.recycleView.setAdapter(myAdapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        // Sending button click listener / Seting the OnClickListener for the 'send' button
        binding.sendButton.setOnClickListener(v -> {
            sendMessage(true); // Sending a message
        });

        // Receiving button click listener / Seting the OnClickListener for the 'receive' button
        binding.receiveButton.setOnClickListener(v -> {
            sendMessage(false); // Receiving a message
        });
    }

    // Handling send or receive messages
// Send or receive messages
    private void sendMessage(boolean isSent) {
        String messageText = binding.textInput.getText().toString();
        if (!messageText.isEmpty()) {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(messageText, currentDateandTime, isSent);

            // Add the new message to the ViewModel list
            chatModel.addMessage(newMessage);
            // Notify the adapter to update the RecyclerView
            myAdapter.notifyItemInserted(chatModel.messages.getValue().size() - 1);
            // Clear the input field
            binding.textInput.setText("");

            // Insert the new message into the database
            // You should do this in a background thread to avoid blocking the main thread
            new Thread(() -> {
                MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                        MessageDatabase.class, "message_database").build();
                ChatMessageDAO cmDAO = db.cmDAO();
                // Insert the message into the database
                cmDAO.insertMessage(newMessage);
            }).start();
        }
    }

// ViewHolder class for RecyclerView to hold and manage the view for each message
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
                                // Store the deleted message and position
                                ChatMessage deletedMessage = chatModel.messages.getValue().get(position);
                                chatModel.messages.getValue().remove(position);
                                myAdapter.notifyItemRemoved(position);

                                // Show the Snackbar with Undo button
                                Snackbar snackbar = Snackbar.make(itemView, "Message deleted", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Undo", view -> {
                                    // Insert the message back into the list at the original position
                                    chatModel.messages.getValue().add(position, deletedMessage);
                                    myAdapter.notifyItemInserted(position);
                                });

                                // Show the Snackbar
                                snackbar.show();
                            }
                        })
                        .create()
                        .show();
            });
        }
    }
    }