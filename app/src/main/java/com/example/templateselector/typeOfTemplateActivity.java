package com.example.templateselector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class typeOfTemplateActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private Button nextButton;
    private Uri selectedImage;
    private String picturePath;
    private Bitmap pictureShot;
    static final int REQUEST_IMAGE_CAPTURE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_template);
        nextButton = findViewById(R.id.nextButton);

    }

    public void selectTemplateClicked (View view){
        Intent intentImgflip = new Intent(typeOfTemplateActivity.this, TemplateSelector.class);
        startActivity(intentImgflip);
    }


    public void importImageClicked (View view){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }

    public void takePicture (View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);

            cursor.close();

            System.out.println("URI de l'image : " + selectedImage);

/*            Intent imageUri = new Intent(typeOfTemplateActivity.this, MainActivity.class);
            imageUri.putExtra("imageUri",imageUri);
            startActivity(imageUri);*/


            ImageView importedImageView = (ImageView) findViewById(R.id.importedImageView);
            importedImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            pictureShot = null;

        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView importedImageView = (ImageView) findViewById(R.id.importedImageView);
            importedImageView.setImageBitmap(imageBitmap);
            pictureShot = imageBitmap;
            picturePath = null;
        }
    }


    public void nextClicked (View view)
    {
        if (picturePath != null && pictureShot == null){
            Intent intentImported = new Intent(typeOfTemplateActivity.this,MainActivity.class);
            System.out.println("Image path : " + picturePath);
            intentImported.putExtra("picturePath",picturePath);

            startActivity(intentImported);
        }
        else if (picturePath == null && pictureShot != null){
            Intent intentShot = new Intent(typeOfTemplateActivity.this,MainActivity.class);
            System.out.println("picture shot : " + pictureShot);
            intentShot.putExtra("pictureShot", pictureShot);

            startActivity(intentShot);
        }


    }







}