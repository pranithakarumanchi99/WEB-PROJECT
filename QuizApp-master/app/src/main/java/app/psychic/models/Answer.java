package app.psychic.models;

import java.io.Serializable;

public class Answer implements Serializable {

    private String answer;
    private float threshold;
    private boolean checked;

    public Answer(){

    }

    public Answer(String answer, int threshold) {
        this.answer = answer;
        this.threshold = threshold;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
