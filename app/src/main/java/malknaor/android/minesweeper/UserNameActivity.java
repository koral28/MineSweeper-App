package malknaor.android.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserNameActivity extends Activity {
    private String playerName;
    private String playerDifficulty;
    private String playerTime;

    EditText userName;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_user_name_activity);

        userName = findViewById(R.id.user_name);
        submit = findViewById(R.id.submit);

        playerDifficulty = getIntent().getStringExtra("DIFFICULTY");
        playerTime = getIntent().getStringExtra("TIME");

        //here in click i save name, difficulty, time
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().matches("")) {
                    Toast.makeText(UserNameActivity.this, "Please enter your name", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(UserNameActivity.this, ScoreTableActivity.class);
                    intent.putExtra("DIFFICULTY", playerDifficulty);
                    intent.putExtra("TIME", playerTime);
                    intent.putExtra("NAME", userName.getText().toString());
                    startActivity(intent);
                    UserNameActivity.super.onBackPressed();
                }
            }
        });
    }
}

