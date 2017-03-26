package com.google.android.exoplayer.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StreamListActivity extends Activity {

    String[] streams = {
            "http://demo.unified-streaming.com/video/smurfs/smurfs.ism/smurfs.mpd",
            "http://dash.edgesuite.net/envivio/dashpr/clear/Manifest.mpd"
    };

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_list);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, streams);

        // fill list with sample streams
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String)listView.getItemAtPosition(position);
                // pass URL to Player
                startActivity(buildIntent(getApplicationContext(), itemValue));
            }
        });
    }

    public Intent buildIntent(Context context, String uri) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("uri", uri);
        return intent;
    }
}
