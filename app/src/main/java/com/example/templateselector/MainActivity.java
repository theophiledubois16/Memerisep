package com.example.templateselector;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {

    private EditText textInput;
    private RelativeLayout relativeLayout;
    private LinearLayout editTextStyle, editText, fontEditor;
    private Button apply, selectedText,font;
    private ImageButton submit, saveMeme;
    private ImageView imageView;
    float dX, dY;
    private ScrollView scrollView;
    private TextView fontExampleView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scrollView);
        textInput = findViewById(R.id.plain_text_input);
        relativeLayout = findViewById(R.id.relativeLayout);
        editTextStyle = findViewById(R.id.textEditorLayout);
        editText = findViewById(R.id.linearLayout);
        submit = findViewById(R.id.submit);
        apply = findViewById(R.id.apply);
        imageView = findViewById(R.id.imageView);
        saveMeme = findViewById(R.id.save);
        fontEditor = findViewById(R.id.fontEditor);
        fontExampleView = findViewById(R.id.fontExempleView);
        font = findViewById(R.id.font);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        apply.setEnabled(false);
       /** String urlTemplate =  getIntent().getStringExtra("urlToPass");

        Glide.with(this)
                .load(urlTemplate)
                .into(imageView);**/
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

                            relativeLayout.requestDisallowInterceptTouchEvent(true);
                            Log.d("TRUE", "onTouch: "+ "relativeLayout.requestDisallowInterceptTouchEvent(true);" );
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

                        case MotionEvent.ACTION_UP:

                            relativeLayout.requestDisallowInterceptTouchEvent(false);

                            Log.d("FALSE", "onTouch: "+ "relativeLayout.requestDisallowInterceptTouchEvent(flase);" );
                    }
                    Typeface style = newTextView.getTypeface();
                    font.setTypeface(style);

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
        CharSequence action = null;

        if (view instanceof ImageButton) {
            ImageButton buttonClicked = (ImageButton) view;
            action = buttonClicked.getContentDescription();
        }

        if (view instanceof Button) {
            Button buttonClicked = (Button) view;
            action = buttonClicked.getContentDescription();
        }

        Log.d("fontSizeManager", "ACTION = "+ action);

        float fontSize = selectedText.getTextSize();
        float spFontSize = fontSize / getResources().getDisplayMetrics().scaledDensity;

        if (!selectedText.getText().toString().isEmpty()){
            if (action.equals("+") && spFontSize < 80){
                selectedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spFontSize + 4 );
            }
            else if (action.equals("-")&& spFontSize > 15){
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
            else if (action.equals("bin")){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedText.setVisibility(View.GONE);
                        applyText(view);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
            else if (action.equals("font")){
                fontEditor.setVisibility(View.VISIBLE);
                apply.setVisibility(View.GONE);
                Typeface style = font.getTypeface();
                fontExampleView.setTypeface(style);

                int viewColor = font.getCurrentTextColor();
                String hexColor = String.format("#%06X", (0xFFFFFF & viewColor));
                fontExampleView.setTextColor(Color.parseColor(hexColor));

            }
        }
    }

    public void changeTemplate (View view){
        this.finish();

    }

    public void saveMemeClicked (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you happy with your work?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToGallery();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Bitmap setViewToBitmapImage(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void saveToGallery () {

        Bitmap bitmap = setViewToBitmapImage(relativeLayout);

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/Pictures/MemerISEP");
        dir.mkdirs();

        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File (dir,filename);

        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fontSetter(View view){
        Button buttonClicked = (Button) view;

        CharSequence action = buttonClicked.getContentDescription();
        if (action.equals("font")){
            Typeface style = buttonClicked.getTypeface();
            fontExampleView.setTypeface(style);
        }
        else if (action.equals("color")){
            ColorDrawable viewColor = (ColorDrawable) buttonClicked.getBackground();
            int colorId = viewColor.getColor();
            String hexColor = String.format("#%06X", (0xFFFFFF & colorId));
            fontExampleView.setTextColor(Color.parseColor(hexColor));
        }
    }

    public void setFont (View view){
        Typeface styleToPass = fontExampleView.getTypeface();
        int viewColor = fontExampleView.getCurrentTextColor();
        String hexColor = String.format("#%06X", (0xFFFFFF & viewColor));

        selectedText.setTextColor(Color.parseColor(hexColor));
        selectedText.setTypeface(styleToPass);

        font.setTypeface(styleToPass);
        font.setTextColor(Color.parseColor(hexColor));

        fontEditor.setVisibility(View.GONE);

        apply.setVisibility(View.VISIBLE);

    }
}