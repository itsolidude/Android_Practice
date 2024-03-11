package algonquin.cst2335.kadv0001;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version=1)
public abstract class MessageDatabase  extends RoomDatabase {
    public abstract ChatMessageDAO cmDAO();
}
