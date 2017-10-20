package com.bignerdranch.android.geoquiz;

/**
 * Created by kwheelerj on 8/12/2017.
 */

public class Question {

    private boolean mAnswerTrue;
    private int mTextResId;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }


}
