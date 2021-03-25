package com.sid.newsar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setSubtitle("History");

        List<String> historyList = decache();
        if (historyList.size() == 0) Toast.makeText(this, "No history yet!", Toast.LENGTH_SHORT).show();
        else {
            initListView(historyList);
        }
    }

    private void initListView(List<String> historyList) {
        HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, historyList);
        ((ListView) findViewById(R.id.historyListView)).setAdapter(adapter);
    }

    private List<String> decache() {
        List<String> historyList = new ArrayList<>();
        try {
            SharedPreferences preferences = getSharedPreferences("news_ar", Context.MODE_PRIVATE);
            JSONArray historyArray = new JSONArray(preferences.getString("history", "[]"));
            for (int i = 0; i < historyArray.length(); i++)
                historyList.add(historyArray.get(i).toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return historyList;
    }

    private void uncache(int index) {
        List<String> historyList = new ArrayList<>();
        try {
            SharedPreferences preferences = getSharedPreferences("news_ar", Context.MODE_PRIVATE);
            JSONArray historyArray = new JSONArray(preferences.getString("history", "[]"));
            historyArray.remove(index);
            preferences.edit().putString("history", historyArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class HistoryAdapter extends ArrayAdapter<String> {

        List<String> historyList;

        public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            historyList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View mView = super.getView(position, convertView, parent);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helpers.openWebPage(getApplicationContext(), historyList.get(position));
                }
            });
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(HistoryActivity.this)
                            .setTitle("Delete URL")
                            .setMessage("Sure you want to delete this URL from history?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    historyList.remove(position);
                                    initListView(historyList);
                                    uncache(position);
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
            });
            return mView;
        }
    }
}