package app.psychic.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import app.psychic.models.Question;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM Question where examId=:examId")
    List<Question> getQuestions(String examId);

    @Query("SELECT * FROM Question where examId LIKE '%' || :set || '%'")
    List<Question> getQuestionsOfSet(String set);

    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> question);

    @Query("Delete from Question where examId=:examId")
    void deleteQuestions(String examId);
}
