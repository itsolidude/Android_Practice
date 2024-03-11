package algonquin.cst2335.kadv0001;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time_sent")
    private String timeSent;

    @ColumnInfo(name = "is_sent_button")
    private boolean isSentButton;

    // Constructors
    public ChatMessage() {
        // Empty constructor for ROOM
    }

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }



    public void setSentButton(boolean sentButton) {
        isSentButton = sentButton;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
