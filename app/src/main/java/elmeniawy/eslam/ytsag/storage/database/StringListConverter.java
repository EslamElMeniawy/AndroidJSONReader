package elmeniawy.eslam.ytsag.storage.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * StringListConverter
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class StringListConverter {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String listToString(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
