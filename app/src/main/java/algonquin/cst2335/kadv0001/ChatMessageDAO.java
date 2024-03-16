package algonquin.cst2335.kadv0001;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO{
    @Insert
    long insertMessage(ChatMessage message);

    @Query("SELECT * from ChatMessage")
    List<ChatMessage> getAllMessages();
    @Delete
    void deleteMessage(ChatMessage message);

    @Query("DELETE FROM ChatMessage")
    void deleteAllMessages();
}

