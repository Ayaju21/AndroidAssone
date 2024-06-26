package com.example.biologyquizapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.biologyquizapplication.model.Question;
import com.example.biologyquizapplication.model.UserAnswersModule;
import com.example.biologyquizapplication.model.QuestionDataAccess;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private final int MAX_QUESTIONS = 5;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewPercent;
    private TextView textViewStreak;
    private AppCompatButton buttonAnswer0;
    private AppCompatButton buttonAnswer1;
    private AppCompatButton buttonAnswer2;

    private AppCompatButton buttonNext;

    private QuestionDataAccess questionDataAccess;

    private boolean canAdvance = false;
    private int SCORE = 0;
    private int CURRENT_STREAK = 0;
    private int QUESTIONS_ANSWERED_CORRECTLY = 0;
    private int QUESTIONS_ANSWERED_TOTAL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        questionDataAccess = new QuestionDataAccess();

        buttonNext.setOnClickListener(e -> {
            if (canAdvance)
                setNewQuestion();
        });

        startGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.d("Lifecycle", "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        Log.d("Lifecycle", "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        Log.d("Lifecycle", "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped").
        Log.d("Lifecycle", "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.d("Lifecycle", "onDestroy() called");
    }


    private void findViews() {
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewPercent = findViewById(R.id.textViewPercentage);
        textViewStreak = findViewById(R.id.textViewStreak);
        textViewScore = findViewById(R.id.textViewScore);
        buttonAnswer0 = findViewById(R.id.buttonAnswer1);
        buttonAnswer1 = findViewById(R.id.buttonAnswer2);
        buttonAnswer2 = findViewById(R.id.buttonAnswer3); // Adjusted index due to removal
        buttonNext = findViewById(R.id.buttonNext);
    }

    private void startGame() {
        SCORE = 0;
        CURRENT_STREAK = 0;
        QUESTIONS_ANSWERED_CORRECTLY = 0;
        QUESTIONS_ANSWERED_TOTAL = 0;
        updateTopBar();
        setNewQuestion();
    }

    private void setNewQuestion() {
        if (QUESTIONS_ANSWERED_TOTAL >= MAX_QUESTIONS) {
            endGame();
            return;
        }
        canAdvance = false;

        Question question = questionDataAccess.getRandomQuestion();
        if (question == null) {
            endGame();
            return;
        }
        String questionString = question.getQuestion();
        String[] questionAnswers = question.getAnswers();
        AtomicInteger atomicCorrectAnswer = new AtomicInteger(question.getRightAnswer());

        textViewQuestion.setText(questionString);

        AppCompatButton[] answerButtons = {buttonAnswer0, buttonAnswer1, buttonAnswer2};
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(questionAnswers[i]);
            answerButtons[i].setBackgroundResource(R.drawable.answer_button);
            answerButtons[i].setTextColor(answerButtons[i].getContext().getColor(R.color.my_white));
        }

        for (int i = 0; i < answerButtons.length; i++) {
            int answerIndex = i;
            answerButtons[i].setOnClickListener(e -> {
                int correctAnswer = atomicCorrectAnswer.get();
                if (correctAnswer == answerIndex)
                    answeredCorrect(answerButtons[answerIndex], question);
                else
                    answeredWrong(answerButtons[answerIndex], answerButtons[correctAnswer], question);
                QUESTIONS_ANSWERED_TOTAL++;
                canAdvance = true;
                updateTopBar();
            });
        }
    }

    private void updateTopBar() {
        String scoreText = "Score: " + SCORE;
        textViewScore.setText(scoreText);
        String percentText = QUESTIONS_ANSWERED_CORRECTLY + " / " + MAX_QUESTIONS;
        textViewPercent.setText(percentText);
        String streakText = "Streak: " + CURRENT_STREAK;
        textViewStreak.setText(streakText);
    }

    private void answeredWrong(AppCompatButton thisButton, AppCompatButton correctButton, Question question) {
        UserAnswersModule.wrongQuestions.add(question);
        thisButton.setBackgroundResource(R.drawable.answer_button_wrong);
        thisButton.setTextColor(thisButton.getContext().getColor(R.color.red));
        correctButton.setBackgroundResource(R.drawable.answer_button_correct);
        correctButton.setTextColor(correctButton.getContext().getColor(R.color.aqua));
        CURRENT_STREAK = 0;
    }

    private void answeredCorrect(AppCompatButton thisButton, Question question) {
        UserAnswersModule.correctQuestions.add(question);
        thisButton.setBackgroundResource(R.drawable.answer_button_correct);
        thisButton.setTextColor(thisButton.getContext().getColor(R.color.aqua));
        CURRENT_STREAK++;
        QUESTIONS_ANSWERED_CORRECTLY++;

        SCORE++; // Increment the score by 1 for each correct answer.
    }

    private void endGame() {
        Intent intent = new Intent(MainActivity.this, EndGameActivity.class);
        UserAnswersModule.score = SCORE;
        startGame();
        startActivity(intent);
    }
}
