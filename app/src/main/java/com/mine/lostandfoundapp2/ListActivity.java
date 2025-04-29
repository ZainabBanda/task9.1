package com.mine.lostandfoundapp2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private DBHelper     db;
    private ItemAdapter  adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);  // create this XML

        db = new DBHelper(this);

        RecyclerView rv = findViewById(R.id.rv_items);
        List<Item> items = db.getAllItems();
        adapter = new ItemAdapter(this, items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
}
