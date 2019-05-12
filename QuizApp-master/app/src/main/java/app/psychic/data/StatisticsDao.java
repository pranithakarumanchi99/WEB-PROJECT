package app.psychic.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import app.psychic.models.Statistics;

@Dao
public interface StatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStatistics(Statistics statistics);

    @Query("SELECT * FROM Statistics WHERE type = :type")
    List<Statistics> getStatistics(String type);
}

