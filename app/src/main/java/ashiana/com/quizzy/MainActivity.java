package ashiana.com.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private Button startBtn, bookmarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.start_button);
        bookmarks = findViewById(R.id.bookmarks_button);

        MobileAds.initialize(this);

        loadAds();

        startBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent categoryIntent = new Intent(MainActivity.this,CategoriesActivity.class);
                startActivity(categoryIntent);
            }
        });

        bookmarks.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent bookmaeksIntent = new Intent(MainActivity.this,BookmarkActivity.class);
                startActivity(bookmaeksIntent);
            }
        });
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
