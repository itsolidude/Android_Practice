package algonquin.cst2335.kadv0001;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    // MutableLiveData holding a list of ChatMessage objects
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();

    // Constructor for MutableLiveData with an empty ArrayList
    public ChatRoomViewModel() {
        messages.setValue(new ArrayList<>());
    }

    // Addiing a message to the current list and update MutableLiveData
    public void addMessage(ChatMessage message) {
        // Retrieve the current list of messages
        ArrayList<ChatMessage> currentMessages = messages.getValue();

        // If the list is not null, add  new message to it
        if (currentMessages != null) {
            currentMessages.add(message);
            // Update MutableLiveData with modified list
            messages.postValue(currentMessages);
        }
    }
}