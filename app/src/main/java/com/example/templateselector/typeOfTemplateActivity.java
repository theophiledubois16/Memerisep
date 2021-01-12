package com.example.templateselector;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static androidx.core.content.FileProvider.getUriForFile;

public class typeOfTemplateActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private Button nextButton;
    private Uri selectedImage, photoURI;
    private String picturePath;
    private Uri pictureShot;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    private ArrayList<String> idTemplatesList = new ArrayList<>();
    private ArrayList<String> nameTemplatesList = new ArrayList<>();
    private ArrayList<String> urlTemplatesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_template);
        nextButton = findViewById(R.id.nextButton);

    }

    public void selectTemplateClicked (View view){

        String url = "https://api.imgflip.com/get_memes";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray memes = data.getJSONArray("memes");
                            for (int i = 0; i < memes.length(); i++){
                                JSONObject n = memes.getJSONObject(i);
                                String id = n.getString("id");

                                idTemplatesList.add(id);
                                String name = n.getString("name");
                                nameTemplatesList.add(name);
                                String url = n.getString("url");
                                urlTemplatesList.add(url);
                                System.out.println("URL  LIST  " + urlTemplatesList);



                            }
                            System.out.println("URL TAMPLATE LIST COMPLETE " + urlTemplatesList);
                            Intent intentImgflip = new Intent(typeOfTemplateActivity.this, TemplateSelector.class);
                            intentImgflip.putExtra("intentImgflip",urlTemplatesList);
                            intentImgflip.putExtra("intentNameImgflip",nameTemplatesList);
                            startActivity(intentImgflip);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "An error occurred.");
            }
        });

        queue.add(request);

    }


    public void importImageClicked (View view){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }

    public void takePicture (View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = getUriForFile(this,
                        "com.example.imgfliptest.fileprovider",
                        photoFile);



                System.out.println("URI de l'image : " + photoURI);
                Log.d("URI de l'image : ", "" + photoURI);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
            Uri selectedFile = photoURI;
            System.out.println("URI de l'image prise: " + selectedFile);
            ImageView importedImageView = (ImageView) findViewById(R.id.importedImageView);
            importedImageView.setImageURI(selectedFile);
            pictureShot = selectedFile;
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