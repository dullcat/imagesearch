package com.example.towu.imagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.towu.imagesearch.R;
import com.example.towu.imagesearch.models.ImageOptions;

public class AdvancedOptionsActivity extends AppCompatActivity {
    ImageOptions options;
    EditText etSize;
    EditText etColor;
    EditText etType;
    EditText etSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_options);

        //getActionBar().hide();
        options = (ImageOptions) getIntent().getSerializableExtra("options");
        etSize = (EditText) findViewById(R.id.etImageSize);
        etColor = (EditText) findViewById(R.id.etImageColor);
        etType = (EditText) findViewById(R.id.etImageType);
        etSite = (EditText) findViewById(R.id.etImageSite);

        etSize.setText(options.size);
        etColor.setText(options.color);
        etType.setText(options.type);
        etSite.setText(options.site);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_advanced_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveOptions(View view) {
        options.size = etSize.getText().toString();
        options.color = etColor.getText().toString();
        options.type = etType.getText().toString();
        options.site = etSite.getText().toString();
        Intent result = new Intent();
        result.putExtra("options", options);
        setResult(RESULT_OK, result);
        finish();
    }
}
