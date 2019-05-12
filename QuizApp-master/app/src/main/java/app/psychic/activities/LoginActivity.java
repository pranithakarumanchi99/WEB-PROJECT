package app.psychic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import app.psychic.R;
import app.psychic.data.DataManager;
import app.psychic.databinding.ActivityLoginBinding;
import app.psychic.models.User;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private ProgressDialog dialog;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        FirebaseApp.initializeApp(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");


        if (DataManager.newInstance(this).isLogin()) {
            loginSuccess();
        }

        final GenericTypeIndicator<Map<String, User>> genericTypeIndicator = new GenericTypeIndicator<Map<String, User>>() {
        };


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  showLoading("Logging in..");
                if (validate()) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, User> map = dataSnapshot.getValue(genericTypeIndicator);
                            hideLoading();
                            i = 0;
                            if (map != null) {
                                for (User user : map.values()) {
                                    if (user.getEmail().equals(binding.email.getText().toString()) && user.getPassword().equals(binding.password.getText().toString())) {
                                        DataManager.newInstance(LoginActivity.this).setIsLogin(true);
                                        DataManager.newInstance(LoginActivity.this).setName(user.getName());
                                        loginSuccess();
                                    } else {
                                        i++;
                                    }
                                }
                                if (i == map.values().size()) {
                                    showMessage("Invalid login credentials");
                                }
                            } else {
                                showMessage("Invalid login credentials");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            showMessage(databaseError.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void loginSuccess() {
        Intent intent = new Intent(this, DescriptionActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate() {
        if (binding.email.getText().length() == 0) {
            showMessage("Email required");
            binding.email.setError("Email Required");
            return false;
        } else if (binding.password.getText().length() == 0) {
            showMessage("Password required");
            binding.password.setError("Password Required");
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void moveToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void showLoading(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new ProgressDialog(LoginActivity.this);
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
