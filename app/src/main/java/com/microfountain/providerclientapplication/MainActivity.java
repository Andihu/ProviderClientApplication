package com.microfountain.providerclientapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String AUTHORITY = "com.example.MyContentProvider";
    ListView listView;
    private Uri uri;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        uri = new Uri.Builder().scheme("content").authority(AUTHORITY).appendPath("user").build();

        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.layout_itm,
                null,
                new String[]{"_id","name"},
                new int[]{R.id.id,R.id.textView});
        listView.setAdapter(simpleCursorAdapter);

        getContentResolver().registerContentObserver(uri, false, new ContentObserver(new Handler(getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, @Nullable Uri uri, int flags) {
                Cursor query = getContentResolver().query(uri, null, null, null, null);
                if (query.moveToFirst()) {
                    do {
                        String name = query.getString(query.getColumnIndex("name"));
                        Log.d(TAG, "onChange: "+name);
                    }while (query.moveToNext());
                }
                simpleCursorAdapter.swapCursor(query);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void addData(View view) {
        ContentValues contentValues = new ContentValues();
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        contentValues.put("name", sdf.format(date));
        getContentResolver().insert(uri, contentValues);
    }

    public void getDataLimit3(View view) {
        uri = new Uri.Builder().scheme("content").authority(AUTHORITY).appendPath("user").appendPath("limit").appendPath("3").build();
        Cursor query = getContentResolver().query(uri,null, null, null, null);
        if (query.moveToFirst()) {
            do {
                String name = query.getString(query.getColumnIndex("name"));
                Log.d(TAG, "onChange: query"+name);
            }while (query.moveToNext());
        }
        simpleCursorAdapter.swapCursor(query);
    }
}