package com.example.templateselector;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.nio.file.ClosedDirectoryStreamException;

public class MainActivity extends AppCompatActivity {

    private EditText textInput;
    private RelativeLayout relativeLayout;
    private LinearLayout editTextStyle, editText;
    private Button  apply, selectedText;
    private ImageButton submit;
    private ImageView imageView;
    float dX, dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInput = findViewById(R.id.plain_text_input);
        relativeLayout = findViewById(R.id.relativeLayout);
        editTextStyle = findViewById(R.id.textEditorLayout);
        editText = findViewById(R.id.linearLayout);
        submit = findViewById(R.id.submit);
        apply = findViewById(R.id.apply);
        imageView = findViewById(R.id.imageView);



        apply.setEnabled(false);

        String urlTemplate =  getIntent().getStringExtra("urlToPass");

        Glide.with(this)
                .load(urlTemplate)
                .into(imageView);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void updateText(View view) {
        if (!textInput.getText().toString().isEmpty()) {

            final Button newTextView = new Button(this);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT,(int) RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.CENTER_IN_PARENT);

            newTextView.setTextColor(Color.parseColor("#000000"));
            newTextView.setTextSize(30);
            newTextView.setBackground(null);
            newTextView.setAllCaps(false);
            newTextView.isClickable();
            newTextView.setLayoutParams(params);
            newTextView.setText(String.valueOf(textInput.getText()));

            relativeLayout.addView(newTextView);
            textInput.setText(null);
            submit.setEnabled(false);

            newTextView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    RelativeLayout parentLayout = (RelativeLayout) v.getParent();

                    int w = v.getWidth();
                    int h = v.getHeight();
                    int W = parentLayout.getWidth();
                    int H = parentLayout.getHeight();

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            dX = v.getX() - event.getRawX();
                            dY = v.getY() - event.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            v.animate()
                                    .x(event.getRawX() + dX)
                                    .y(event.getRawY() + dY)
                                    .setDuration(0)
                                    .start();

                            float x = v.getX();
                            float y = v.getY();
                            float X = parentLayout.getX();
                            float Y = parentLayout.getY();


                            if ((Y+H)<(y+h+30) ){

                                v.animate()
                                        .x(event.getRawX() + dX)
                                        .y((Y+H) -(h+30))
                                        .setDuration(0)
                                        .start();
                                Log.d("TAG", "TOO LOW");
                            }

                            if ((X+W)<(x+w+30)){
                                v.animate()
                                        .x((X+W) -(w+30))
                                        .y(event.getRawY() + dY)
                                        .setDuration(0)
                                        .start();
                                Log.d("TAG", "TOO RIGHT");
                            }

                            if ((x)<(X+30)){
                                v.animate()
                                        .x(X + 30)
                                        .y(event.getRawY() + dY)
                                        .setDuration(0)
                                        .start();
                                Log.d("TAG", "TOO LEFT");
                            }

                            if ((y)<(Y+30)){
                                v.animate()
                                        .x(event.getRawX() + dX)
                                        .y(Y + 30)
                                        .setDuration(0)
                                        .start();
                                Log.d("TAG", "TOO HIGH");
                            }

                            if(((x)<(X+30))&&((y)<(Y+30))){ //hl
                                v.animate()
                                        .x(X+30)
                                        .y(Y + 30)
                                        .setDuration(0)
                                        .start();
                            }

                            if(((y)<(Y+30))&&((X+W)<(x+w+30))){
                                v.animate()
                                        .x((X+W) -(w+30))
                                        .y(Y+30)
                                        .setDuration(0)
                                        .start();
                            }

                            if(((Y+H)<(y+h+30))&&((x)<(X+30))){
                                v.animate()
                                        .x(X+30)
                                        .y((Y+H) -(h+30))
                                        .setDuration(0)
                                        .start();
                            }

                            if(((Y+H)<(y+h+30))&&((X+W)<(x+w+30))){
                                v.animate()
                                        .x((X+W) -(w+30))
                                        .y((Y+H) -(h+30))
                                        .setDuration(0)
                                        .start();
                            }
                            break;
                    }
                    selectedText = newTextView;

                    editTextStyle.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                    apply.setVisibility(View.VISIBLE);
                    apply.setEnabled(true);
                    return true;
                }

            });
        }
    }
    public void applyText(View view){
        submit.setEnabled(true);
        apply.setEnabled(false);
        editTextStyle.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);
        apply.setVisibility(View.GONE);
    }

    @SuppressLint("WrongConstant")
    public void textStyleManager(final View view) {

        ImageButton buttonClicked = (ImageButton) view;
        CharSequence action = buttonClicked.getContentDescription();

        Log.d("fontSizeManager", "ACTION = "+ action);

        float fontSize = selectedText.getTextSize();
        float spFontSize = fontSize / getResources().getDisplayMetrics().scaledDensity;

        if (!selectedText.getText().toString().isEmpty()){
            if (action.equals("+") && spFontSize < 80){
                selectedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spFontSize + 4 );
            }
            else if (action.equals("-")&& spFontSize > 18){
                selectedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spFontSize - 4 );
            }
            else if (action.equals("left")){
                selectedText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
            else if (action.equals("center")){
                selectedText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            else if (action.equals("right")){
                selectedText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            else if (action.equals("bold")){
                Log.d("BOLD", "TYPEFACE = "+ selectedText.getTypeface().getStyle());
                if (selectedText.getTypeface().getStyle() == 0) {

                    selectedText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                else if (selectedText.getTypeface().getStyle() == 1) {

                    selectedText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
            else if (action.equals("bin")){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedText.setVisibility(View.GONE);
                        applyText(view);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }
    }

    public void changeTemplate (View view){
        this.finish();

    }
}