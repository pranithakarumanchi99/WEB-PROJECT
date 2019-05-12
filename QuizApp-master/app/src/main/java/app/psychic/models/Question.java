package app.psychic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Question implements Serializable {

    public Question() {
    }

    @PrimaryKey(autoGenerate = true)
    private Long questionId;
    private String question;
    private ArrayList<Answer> answers;

    private float percentage;
    private String examId;

    private float score;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
