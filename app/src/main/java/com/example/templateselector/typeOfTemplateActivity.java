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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class typeOfTemplateActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private Button nextButton;
    private Uri selectedImage;
    private String picturePath;
    private Bitmap pictureShot;
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