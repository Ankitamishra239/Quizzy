package ashiana.com.quizzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_bookmarks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);



       List<QuestionsModel> list = new ArrayList<>();
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));
       list.add(new QuestionsModel("What is your name?", "", "", "", "", "John Doe",0));

        BoolmarkAdaptor adaptor = new BoolmarkAdaptor(list);
        recyclerView.setAdapter(adaptor);

        MobileAds.initialize(this);

        loadAds();

    }

   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
