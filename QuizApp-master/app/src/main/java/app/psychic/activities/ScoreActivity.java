package app.psychic.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import app.psychic.R;
import app.psychic.data.DataManager;
import app.psychic.databinding.ActivityScoreBinding;
import app.psychic.models.Answer;
import app.psychic.models.Question;
import app.psychic.utils.Constants;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_score);

        binding.score.setText(String.format("%s", MainActivity.score));

        binding.overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("type", Constants.selectedSet);
                intent.putExtra("overview", "true");
                intent.putExtra("examId", Constants.examId);
                startActivity(intent);
            }
        });

        binding.retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("type", Constants.selectedSet);
                intent.putExtra("examId", Constants.examId);
                startActivity(intent);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

        binding.statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getBaseContext(), StatisticsActivity.class);
                intent.putExtra("type", Constants.selectedSet);
                intent.putExtra("examId", Constants.examId);
                startActivity(intent);
            }
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        DataManager.newInstance(ScoreActivity.this).setIsLogin(false);
                        Intent intent = new Intent(ScoreActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        ScoreActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create().show();
    }
}
