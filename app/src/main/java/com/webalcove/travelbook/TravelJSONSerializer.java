package com.webalcove.travelbook;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class TravelJSONSerializer {
    private Context mContext;
    private String mFilename;

    public TravelJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Travel> loadTravels() throws IOException, JSONException {
        ArrayList<Travel> travels = new ArrayList<Travel>();
        BufferedReader reader = null;
        try {
            // open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // build the array of crimes from JSONObjects
//            for (int i = 0; i < array.length(); i++) {
//                travels.add(new Travel(array.getJSONObject(i)));
//            }
        } catch (FileNotFoundException e) {
            // we will ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
}
        return travels;
    }

    public void saveTravels(ArrayList<Travel> travels) throws JSONException, IOException {
        // build an array in JSON
//        JSONArray array = new JSONArray();
//        for (Travel t : travels)
//            array.put(t.toJSON());

        // write the file to disk
//        Writer writer = null;
//        try {
//            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
//            writer = new OutputStreamWriter(out);
//            writer.write(array.toString());
//        } finally {
//            if (writer != null)
//                writer.close();
//        }
    }

}
