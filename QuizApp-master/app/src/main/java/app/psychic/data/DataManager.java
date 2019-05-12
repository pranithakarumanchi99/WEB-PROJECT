package app.psychic.data;

import android.content.Context;
import android.content.SharedPreferences;

import app.psychic.App;

public class DataManager {

    private static final String PREFERENCES = "app.psychic.data";
    private static final String IS_LOGIN = "IS_LOGIN";

    private static DataManager instance;
    private static final String EMAIL = "EMAIL";
    private static final String NAME = "NAME";
    private static final String PHONE = "PHONE";
    private SharedPreferences preferences;

    private DataManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public static DataManager newInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public void setIsLogin(boolean flag) {
        if (!flag) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    App.getDatabase().clearAllTables();
                }
            }).start();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, flag);
        editor.apply();
    }

    public boolean isLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getName() {
        return preferences.getString(NAME, "");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public String getPhone() {
        return preferences.getString(PHONE, "");
    }

    public void setPhone(String phone) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PHONE, phone);
        editor.apply();
    }
}
