package app.psychic;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;
import app.psychic.data.AppDatabase;

public class App extends Application {

    private static App appInstance;
    private static AppDatabase dbInstance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static AppDatabase getDatabase() {
        return dbInstance;
    }

    public static App getAppInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        Stetho.initializeWithDefaults(this);
        FirebaseApp.initializeApp(getApplicationContext());
        dbInstance = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "quiz-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}
