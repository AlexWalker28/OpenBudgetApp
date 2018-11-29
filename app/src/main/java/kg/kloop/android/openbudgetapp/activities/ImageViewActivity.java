package kg.kloop.android.openbudgetapp.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.ImageAdapter;

public class ImageViewActivity extends AppCompatActivity {

    private static final String TAG = ImageViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ViewPager viewPager = findViewById(R.id.image_activity_view_pager);

        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");
        Log.i(TAG, "onCreate: urls size - " + urls.size());
        viewPager.setAdapter(new ImageAdapter(this, urls));
    }

}
