package kg.kloop.android.openbudgetapp.activities;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.ImageAdapter;
import kg.kloop.android.openbudgetapp.utils.ImageViewPager;

public class ImageViewActivity extends AppCompatActivity {

    private static final String TAG = ImageViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageViewPager viewPager = findViewById(R.id.image_activity_view_pager);

        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");
        if (urls != null) {
            Log.i(TAG, "onCreate: urls size - " + urls.size());
            viewPager.setAdapter(new ImageAdapter(this, urls));
        } else {
            ArrayList<Uri> uris = intent.getParcelableArrayListExtra("uris");
            Log.i(TAG, "onCreate: uris size - " + uris.size());
            viewPager.setAdapter(new ImageAdapter(this, uris, true));
        }
    }

}
