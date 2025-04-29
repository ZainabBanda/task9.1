package com.mine.lostandfoundapp2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_create_advert)
            .setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class)));

        findViewById(R.id.btn_list_items)
            .setOnClickListener(v ->
                startActivity(new Intent(this, ListActivity.class)));

        findViewById(R.id.btn_show_map)
            .setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class)));
    }
}

