package app.psychic.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import app.psychic.models.Question;
import app.psychic.models.Statistics;

@Database(entities = {Question.class, Statistics.class}, version = 1, exportSchema = false)
@TypeConverters({ConvertData.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract QuestionDao questionDao();

    public abstract StatisticsDao statisticsDao();
}
