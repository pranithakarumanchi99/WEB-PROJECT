package app.psychic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import app.psychic.R;
import app.psychic.databinding.ActivityChooseTypeBinding;
import app.psychic.utils.Constants;

public class ChooseTypeActivity extends AppCompatActivity {

    ActivityChooseTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_type);

        binding.student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMain("Computer Programming");
            }
        });

        binding.employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMain("General Knowledge");
            }
        });

        binding.unemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMain("Aptitude");
            }
        });

    }

    private void moveToMain(String type) {
        Constants.selectedSet = type;
        Intent intent= new Intent(this, MainActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        this.finish();
    }
}
