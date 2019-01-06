package malknaor.android.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreTableActivity extends Activity {
    private static final String TAG = "ScoreTableActivity";
    public static final String SCROLL_TABLE_LIST = "SCROLL_TABLE_LIST";

    private ArrayList<TableEntry> tableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_table_activity);

        tableListAdapter = getScoresTableList(SCROLL_TABLE_LIST);

        if (tableListAdapter == null) {
            tableListAdapter = new ArrayList<TableEntry>();
        }

        String user_name = getIntent().getStringExtra("NAME");
        String user_difficulty = getIntent().getStringExtra("DIFFICULTY");
        String time = getIntent().getStringExtra("TIME");

        if (user_name != null && user_difficulty != null && time != null) {
            tableListAdapter.add(new TableEntry(user_name, user_difficulty, time));
        }

        saveScoresTableList(tableListAdapter, SCROLL_TABLE_LIST);
        TableAdapter tableAdapter = new TableAdapter(ScoreTableActivity.this, R.layout.table_entry_layout, tableListAdapter);

        final ListView listView = findViewById(R.id.score_table_list_view);
        listView.setAdapter(tableAdapter);

        //fonts
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/AmaticSC-Regular.ttf");
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "fonts/SuezOne-Regular.ttf");

        ((TextView) findViewById(R.id.score_table)).setTypeface(myFont2);
        ((TextView) findViewById(R.id.name_text_view)).setTypeface(myFont2);
        ((TextView) findViewById(R.id.difficulty_text_view)).setTypeface(myFont2);
        ((TextView) findViewById(R.id.time_text_view)).setTypeface(myFont2);

        Button exitScoreTable = findViewById(R.id.exit_scores_btn);
        exitScoreTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScoreTableActivity.super.onBackPressed();
            }
        });
    }

    private void saveScoresTableList(ArrayList<TableEntry> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    private ArrayList<TableEntry> getScoresTableList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<TableEntry>>() {
        }.getType();

        return gson.fromJson(json, type);
    }
}

