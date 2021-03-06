package ashiana.com.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ScoreActivity extends AppCompatActivity {

    private TextView scored, total;
    private Button doneButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scored = findViewById(R.id.scored);
        total = findViewById(R.id.total);
        doneButton = findViewById(R.id.doneButton);

        scored.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        total.setText("Out of "+String.valueOf(getIntent().getIntExtra("total",0)));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MobileAds.initialize(this);

        loadAds();
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
