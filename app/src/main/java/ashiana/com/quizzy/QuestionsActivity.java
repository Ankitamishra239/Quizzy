package ashiana.com.quizzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private TextView question, number_indicator;
    private FloatingActionButton bookmark;
    private LinearLayout options;
    private Button share_button, next_button;
    private int count = 0;
    private List<QuestionsModel> list;
    private int position = 0;
    private int score = 0;
    private String category;
    private int setNo;
    private Dialog loadingdialg;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        question = findViewById(R.id.question);
        number_indicator = findViewById(R.id.number_indicator);
        bookmark = findViewById(R.id.bookmark);
        options = findViewById(R.id.options);
        share_button = findViewById(R.id.share_button);
        next_button = findViewById(R.id.next_button);

        MobileAds.initialize(this);

        loadAds();

        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo", 1);

        loadingdialg = new Dialog(this);
        loadingdialg.setContentView(R.layout.loading);
        loadingdialg.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialg.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialg.setCancelable(false);

        list = new ArrayList<>();

        loadingdialg.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(QuestionsModel.class));
                }
                if (list.size() > 0) {

                    for (int i = 0; i < 4; i++) {
                        options.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }
                    playAnim(question, 0, list.get(position).getQuestion());
                    next_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            next_button.setEnabled(false);
                            next_button.setAlpha(0.5f);
                            enableOption(true);
                            position++;
                            if (position == list.size()) {
                                /////score activity
                                Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                scoreIntent.putExtra("score", score);
                                scoreIntent.putExtra("total", list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }
                            count = 0;
                            playAnim(question, 0, list.get(position).getQuestion());
                        }
                    });
                } else {
                    finish();
                    Toast.makeText(QuestionsActivity.this, "no questions", Toast.LENGTH_SHORT).show();
                }
                loadingdialg.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingdialg.dismiss();
                finish();
            }
        });



    }

    private void playAnim(final View view, final int value, final String data){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4 ){
                    String option ="";
                    if (count == 0){
                        option = list.get(position).getOptionsA();
                    } else if(count == 1){
                        option = list.get(position).getOptionB();
                    }else if(count == 2){
                        option = list.get(position).getOptionC();
                    }else if(count == 3){
                        option = list.get(position).getOptionD();
                    }
                   playAnim(options.getChildAt(count), 0, option );
                   count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // data change

               if (value == 0){

                   try{
                       ((TextView)view).setText(data);
                       number_indicator.setText(position+1+"/"+list.size());
                   }catch (ClassCastException ex){
                       ((Button)view).setText(data);
                   }
                   view.setTag(data);
                   playAnim(view,1,data);
               }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button SelectedOption){
        enableOption(false);
        next_button.setEnabled(true);
        next_button.setAlpha(1);
        if (SelectedOption.getText().toString().equals(list.get(position).getCorrectAns())){
            //correct
            score++;
            SelectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }else {
            //incorrect
            SelectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption = (Button)options.findViewWithTag(list.get(position).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

    }
    private void enableOption(boolean enable){
        for (int i = 0; i < 4; i++){
            options.getChildAt(i).setEnabled(enable);
            if (enable){
                options.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD300")));
            }
        }
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
