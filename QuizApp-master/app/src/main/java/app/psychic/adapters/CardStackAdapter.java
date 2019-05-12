package app.psychic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import app.psychic.R;
import app.psychic.activities.MainActivity;
import app.psychic.databinding.ViewItemQuestionBinding;
import app.psychic.models.Question;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardStackViewHolder> {

    private Context context;
    private List<Question> questions;
    private LayoutInflater inflater;
    private MainActivity activity;
    private boolean overview;

    public CardStackAdapter(Context context) {
        this.context = context;
        this.activity = (MainActivity) context;
        this.questions = new ArrayList<>();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @NonNull
    @Override
    public CardStackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewItemQuestionBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_item_question, viewGroup, false);
        return new CardStackViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardStackViewHolder holder, final int position) {
        holder.binding.letter.setText(String.format("Q.%d", (position + 1)));
        holder.binding.question.setText(questions.get(position).getQuestion());
        holder.binding.answerGroup.removeAllViews();
        for (int i = 0; i < questions.get(position).getAnswers().size(); i++) {
            RadioButton button = new RadioButton(context);
            button.setId(i);
            button.setChecked(questions.get(position).getAnswers().get(i).isChecked());
            questions.get(position).getAnswers().get(i).setChecked(false);
            button.setText(questions.get(position).getAnswers().get(i).getAnswer());
            if (overview) {
                button.setEnabled(false);
                if (activity.getMap() != null && activity.getMap().get(position) != null && Objects.equals(activity.getMap().get(position), button.getText().toString())) {
                    button.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    button.setTextSize(18);
                    button.setText(button.getText() + " - Correct Answer");
                    button.setSelected(true);
                }

                if (questions.get(position).getAnswers().get(i).getThreshold() == 1.0f) {
                    button.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    button.setTextSize(18);

                    button.setText(button.getText() + " - Your Answer");
                    button.setChecked(true);
                }

            }
            holder.binding.answerGroup.addView(button);
        }
        holder.binding.answerGroup.clearCheck();
        holder.binding.answerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                if (button != null && !overview) {
                    questions.get(holder.getAdapterPosition()).getAnswers().get(button.getId()).setChecked(button.isChecked());
                    activity.insertAnswer(position, button.getText().toString());
                    activity.submitAnswer(holder.getAdapterPosition(), questions.get(holder.getAdapterPosition()).getAnswers().get(button.getId()));

                    for(int k = 0 ; k < questions.get(position).getAnswers().size() ; k++){
                        if(button.getText().toString().equals(questions.get(position).getAnswers().get(k).getAnswer())){
                            if((questions.get(position).getAnswers().get(k).getThreshold() == 1.0f)){
                                questions.get(position).setScore(1);
                            }
                            else{
                                questions.get(position).setScore(0);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    public void setOverView(boolean b) {
        this.overview = b;
    }

    static class CardStackViewHolder extends RecyclerView.ViewHolder {
        ViewItemQuestionBinding binding;

        CardStackViewHolder(ViewItemQuestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
