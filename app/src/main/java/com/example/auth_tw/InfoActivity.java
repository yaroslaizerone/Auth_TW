package com.example.auth_tw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView telegram, github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        telegram = findViewById(R.id.Telegram);
        github = findViewById(R.id.GitHub);

        telegram.setClickable(true);
        telegram.setMovementMethod(LinkMovementMethod.getInstance());
        github.setClickable(true);
        github.setMovementMethod(LinkMovementMethod.getInstance());

        String tel = "<a href='https://t.me/Fxrge_f1F'> Telegram </a>";
        String git = "<a href='https://github.com/yaroslaizerone'> GitHub </a>";

        telegram.setText(Html.fromHtml(tel));
        github.setText(Html.fromHtml(git));
    }
}