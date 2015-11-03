package com.example.towu.imagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.towu.imagesearch.R;
import com.example.towu.imagesearch.adapters.ImageResultsAdapter;
import com.example.towu.imagesearch.models.ImageOptions;
import com.example.towu.imagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    EditText etSearch;
    GridView gvResults;
    ArrayList<ImageResult> imageResults;
    ImageResultsAdapter aImageResults;
    ImageOptions imageOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        etSearch = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, FullImageActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
        });
        imageResults = new ArrayList<>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                LoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        imageOptions = new ImageOptions();
    }

    int pageIndex = 0;
    private void LoadMoreDataFromApi(int index) {
        fetchImages(pageIndex, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    public void onImageSearch(View view) {
        pageIndex = 0;
        fetchImages(0, true);
    }

    private void fetchImages(int index, final boolean clear) {
        String query = etSearch.getText().toString();
        Toast.makeText(this, "Search String: " + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        StringBuffer url = new StringBuffer("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&start="+index);
        if (!imageOptions.size.isEmpty()){
            url.append("&imgsz="+imageOptions.size);
        }
        if (!imageOptions.color.isEmpty()){
            url.append("&imgcolor="+imageOptions.color);
        }
        if (!imageOptions.type.isEmpty()){
            url.append("&imgtype="+imageOptions.type);
        }
        if (!imageOptions.site.isEmpty()){
            url.append("&as_sitesearch="+imageOptions.size);
        }
        client.get(url.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    if (clear){
                        imageResults.clear();
                    }
                    imageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                    //aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                    aImageResults.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        pageIndex += 8;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageOptions options = (ImageOptions) data.getSerializableExtra("options");
        imageOptions.size = options.size;
        imageOptions.color = options.color;
        imageOptions.type = options.type;
        imageOptions.site = options.site;
        Log.d("DEBUG", imageOptions.toString());

    }

    public void showAdvancedOptions(MenuItem item) {
        Toast.makeText(this, "Advanced options selected!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SearchActivity.this, AdvancedOptionsActivity.class);
        i.putExtra("options", imageOptions);
        startActivityForResult(i, 200);

    }
}
