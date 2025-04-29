package com.mine.lostandfoundapp2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private DBHelper db;
    private Item     item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize DB and pull the item ID from the Intent
        db = new DBHelper(this);
        int id = getIntent().getIntExtra("item_id", -1);
        item = db.getItemById(id);

        // Wire up views
        TextView tvName     = findViewById(R.id.tv_name);
        TextView tvDesc     = findViewById(R.id.tv_description);
        TextView tvDate     = findViewById(R.id.tv_date);
        TextView tvLoc      = findViewById(R.id.tv_location);
        TextView tvPhone    = findViewById(R.id.tv_phone);
        TextView tvStatus   = findViewById(R.id.tv_status);
        Button   btnRemove  = findViewById(R.id.btn_remove);

        // Populate fields if we found the item
        if (item != null) {
            tvName.setText(item.getName());
            tvDesc.setText(item.getDescription());
            tvDate.setText("Date: "     + item.getDateReported());
            tvLoc.setText("Location: "   + item.getLocation());
            tvPhone.setText("Phone: "    + item.getPhone());
            tvStatus.setText("Status: "  + item.getStatus());
        }

        // Remove and finish on button tap
        btnRemove.setOnClickListener(v -> {
            boolean ok = db.removeItem(id);
            Toast.makeText(
                this,
                ok ? "Item removed" : "Remove failed",
                Toast.LENGTH_SHORT
            ).show();
            if (ok) finish();
        });

    }
}
