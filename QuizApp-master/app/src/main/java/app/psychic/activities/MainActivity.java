package app.psychic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import app.psychic.App;
import app.psychic.R;
import app.psychic.adapters.CardStackAdapter;
import app.psychic.databinding.ActivityMainBinding;
import app.psychic.models.Answer;
import app.psychic.models.Question;
import app.psychic.models.Statistics;
import app.psychic.utils.Constants;

public class MainActivity extends AppCompatActivity implements CardStackListener {

    ActivityMainBinding binding;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private ProgressDialog dialog;
    private List<Answer> answers = new ArrayList<>();
    List<Question> list = new ArrayList<Question>();
    boolean overview;
    public static float score;
    static Map<Integer, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        manager = new CardStackLayoutManager(this, this);
        adapter = new CardStackAdapter(this);
        initialize();
        getQuestions();

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!overview) {
                    if (answers.size() > binding.progress.getProgress() - 1) {
                        if (answers.get(binding.progress.getProgress() - 1) != null) {
                            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                                    .setDirection(Direction.Right)
                                    .setDuration(200)
                                    .setInterpolator(new AccelerateInterpolator())
                                    .build();
                            manager.setSwipeAnimationSetting(setting);
                            binding.cardStackView.swipe();
                        } else {
                            showMessage("Please select the option to continue");
                        }
                    } else {
                        showMessage("Please select the option to continue");
                    }
                } else {
                    SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Right)
                            .setDuration(200)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting);
                    binding.cardStackView.swipe();
                }

            }
        });

        binding.doneButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                binding.cardStackView.swipe();

            }
        });

        binding.doneButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                binding.cardStackView.rewind();

            }
        });
    }

    public void insertAnswer(Integer position, String answerPos) {
        map.put(position, answerPos);
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    private void getQuestions() {

        if (getIntent().getExtras().containsKey("overview")) {
            overview = true;
            binding.buttonContainerOverview.setVisibility(View.VISIBLE);
            binding.buttonContainer.setVisibility(View.INVISIBLE);
        } else {
            binding.buttonContainerOverview.setVisibility(View.GONE);
            binding.buttonContainer.setVisibility(View.VISIBLE);
            map.clear();
        }

        showLoading("Fetching data");

        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        if (getIntent().getStringExtra("type").equalsIgnoreCase("Computer Programming")) {
            myRef = database.getReference("questions1");
        } else if (getIntent().getStringExtra("type").equalsIgnoreCase("General Knowledge")) {
            myRef = database.getReference("questions2");
        } else {
            myRef = database.getReference("questions3");
        }

        final GenericTypeIndicator<List<Question>> typeIndicator = new GenericTypeIndicator<List<Question>>() {
        };

        Random r = new Random();
        int low = 2;
        int high = 25;
        final int result = r.nextInt(high - low) + low;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<Question> yourStringArray = dataSnapshot.getValue(typeIndicator);

                    Collections.shuffle(yourStringArray);
                    Collections.shuffle(yourStringArray);
                    Collections.shuffle(yourStringArray);

                    String examId = Constants.selectedSet + "" + System.currentTimeMillis();
                    if (getIntent().getExtras().containsKey("overview")) {
                        examId = getIntent().getExtras().getString("examId");
                    }

                    if (getIntent().getExtras().containsKey("overview")) {
                        adapter.setOverView(true);
                    }

                    if (overview) {
                        list = App.getDatabase().questionDao().getQuestions("" + examId);
                        adapter.setQuestions(list);
                    } else {
                        Constants.examId = examId;
                        if (yourStringArray.size() > result && yourStringArray.size() <= result + 20) {
                            for (int i = result; i < result + 20; i++) {
                                yourStringArray.get(i).setExamId("" + examId);
                            }
                            App.getDatabase().questionDao().insertQuestions(yourStringArray.subList(result, result + 20));
                        } else {
                            for (int i = 0; i < yourStringArray.size(); i++) {
                                yourStringArray.get(i).setExamId("" + examId);
                            }
                            App.getDatabase().questionDao().insertQuestions(yourStringArray);
                        }
                        list = App.getDatabase().questionDao().getQuestions("" + examId);
                        adapter.setQuestions(list);
                    }
                    hideLoading();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("******", "Failed to read value." + databaseError.getMessage());
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (overview) {
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    public void submitAnswer(int position, Answer answer) {
        if (answer != null) {
            if (answers.size() > position && answers.get(position) != null) {
                answers.set(position, answer);
            } else {
                answers.add(position, answer);
            }
        }

        if (position == list.size() - 1 && !overview) {
            App.getDatabase().questionDao().deleteQuestions(Constants.examId);
            App.getDatabase().questionDao().insertQuestions(adapter.getQuestions());
        }
    }

    private void initialize() {
        List<Direction> HORIZONTAL = Arrays.asList();
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        binding.cardStackView.setLayoutManager(manager);
        binding.cardStackView.setAdapter(adapter);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {
        binding.progress.setProgress(binding.progress.getProgress() - 1);
    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {
        binding.progress.setProgress(position + 1);

        if (overview) {
            binding.feedbacktext.setText("");
            binding.feedback.setVisibility(View.GONE);
        } else {
            binding.feedbacktext.setText("Thanks for your valuable participation");
            binding.feedback.setVisibility(View.VISIBLE);
        }
        if (position == 20) {
            binding.cardStackView.setVisibility(View.GONE);
            binding.progress.setVisibility(View.GONE);
            binding.doneButton.setVisibility(View.GONE);
            binding.feedbackLayout.setVisibility(View.VISIBLE);
            if (!overview) {
                score = 0.0f;
                for (Answer answer : answers) {
                    score += answer.getThreshold();
                }
            }

            Statistics statistics = new Statistics();
            statistics.setType(Constants.selectedSet);
            statistics.setScore(score);
            App.getDatabase().statisticsDao().insertStatistics(statistics);

            if (overview) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent);
                finish();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        } else {
            binding.cardStackView.setVisibility(View.VISIBLE);
            binding.progress.setVisibility(View.VISIBLE);
            binding.doneButton.setVisibility(View.VISIBLE);
            binding.feedbackLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLoading(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage(message);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    if (!isFinishing()) {
                        dialog.show();
                    }
                } else {
                    hideLoading();
                    showLoading(message);
                }
            }
        });
    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }


}
