package app.psychic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import app.psychic.R;
import app.psychic.databinding.ActivityRegisterBinding;
import app.psychic.models.Response;
import app.psychic.models.User;
import app.psychic.network.NetworkService;
import app.psychic.network.NetworkServiceBuilder;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    showLoading("Registering..");
                    User user = new User();
                    user.setName(binding.name.getText().toString());
                    user.setEmail(binding.email.getText().toString());
                    user.setPhone(binding.phone.getText().toString());
                    user.setPassword(binding.password.getText().toString());
                    String userId = myRef.push().getKey();
                    if (userId != null) {
                        user.setUserId(userId);
                        myRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    hideLoading();
                                    showMessage("Registration Successful, Login to continue");
                                    registerSuccess();
                                } else {
                                    showMessage("Failed to register");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void register(User user) {
        NetworkService service = NetworkServiceBuilder.buildMain();
        showLoading("Registering..");
        service.registerUser(user.getEmail(), user.getName(), user.getPhone(), user.getPassword()).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(@NonNull Call<Response<User>> call, @NonNull retrofit2.Response<Response<User>> response) {
                hideLoading();
                if (response.body() != null) {
                    app.psychic.models.Response<User> userResponse = response.body();
                    showMessage(userResponse.getMessage());
                    if (userResponse.isStatus()) {
                        registerSuccess();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<app.psychic.models.Response<User>> call, @NonNull Throwable t) {
                hideLoading();
                showMessage(t.getMessage());
            }
        });
    }

    private void registerSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate() {
        if (binding.name.getText().length() == 0) {
            showMessage("Name required");
            binding.name.setError("Name Required");
            return false;
        } else if (binding.email.getText().length() == 0) {
            showMessage("Email required");
            binding.email.setError("Email Required");
            return false;
        } else if (binding.password.getText().length() == 0) {
            showMessage("Password required");
            binding.password.setError("Password Required");
            return false;
        } else if (binding.phone.getText().length() == 0) {
            showMessage("Phone required");
            binding.phone.setError("Phone Required");
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLoading(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new ProgressDialog(RegisterActivity.this);
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
