package app.psychic.data;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;
import app.psychic.models.Answer;

public class ConvertData {

    @TypeConverter
    public static ArrayList<Answer> fromStringToAnswers(String value) {
        Type listType = new TypeToken<ArrayList<Answer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromAnswersToString(ArrayList<Answer> invoiceItems) {
        Gson gson = new Gson();
        return gson.toJson(invoiceItems);
    }
}
