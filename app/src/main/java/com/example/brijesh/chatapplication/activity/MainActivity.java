package com.example.brijesh.chatapplication.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.brijesh.chatapplication.R;
import com.example.brijesh.chatapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etName.getText().toString().trim().length()>0){
                    Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                    intent.putExtra("name",binding.etName.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });

    }

    private boolean validate() {
        if (binding.etName.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please provide your nick name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
