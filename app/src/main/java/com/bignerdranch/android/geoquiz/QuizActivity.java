package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton, mFalseButton, mNextButton, mCheatButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    //private static final String KEY_ISCHEATER = "isCheater_quizActivity";
    private static final String KEY_ISCHEATERSET = "isCheater_quizActivity";
    private static final String KEY_CHEATSLEFT= "cheats_left";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static int mNumberOfCheatsLeft = 4;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    //private static boolean mIsCheater = false;
    private static HashSet<Integer> mIsCheaterSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheaterSet = (HashSet<Integer>)savedInstanceState.getSerializable(KEY_ISCHEATERSET);
            mNumberOfCheatsLeft = savedInstanceState.getInt(KEY_CHEATSLEFT, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mTrueButton.setEnabled(false);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                mFalseButton.setEnabled(false);
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumberOfCheatsLeft > 1) {
                    //Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                    Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                    //startActivity(intent);
                    startActivityForResult(intent, REQUEST_CODE_CHEAT);
                } else {
                    Toast.makeText(QuizActivity.this, R.string.cheats_used, Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        //savedInstanceState.putBoolean(KEY_ISCHEATER, mIsCheater);
        savedInstanceState.putSerializable(KEY_ISCHEATERSET, mIsCheaterSet);
        savedInstanceState.putInt(KEY_CHEATSLEFT, mNumberOfCheatsLeft);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            if (CheatActivity.wasAnswerShown(data)) {
                mIsCheaterSet.add(mCurrentIndex);
                //System.out.println("mCurrentIndex: " +mCurrentIndex);
                mNumberOfCheatsLeft--;
            }
            // this was necessary rather than a call static class
            // variable since the intent lives, not the activity
        }
    }

    private void updateQuestion() {
        int questionId = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionId);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResId = 0;
        if (mIsCheaterSet.contains(mCurrentIndex)) {
            messageResId = R.string.judgment_toast;
        }
        else {
            if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue())
                messageResId = R.string.correct_toast;
            else
                messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
        //toast.setGravity(Gravity.TOP, 0,0);
        //toast.show();
    }
}
