package com.example.templateselector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TemplateSelector extends AppCompatActivity {

    public ArrayList<String> idTemplatesList = new ArrayList<>();
    public ArrayList<String> nameTemplatesList = new ArrayList<>();
    public ArrayList<String> urlTemplatesList = new ArrayList<>();


    private ArrayList<String> mTemplatesNames = new ArrayList<String>();
    private ArrayList<String> mUrlsTemplatesList = new ArrayList<>();
    ImageView memeTemplate;
    public int indexTemplate = 0;

    private String urlToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_selector);

        memeTemplate = findViewById(R.id.memeTemplate);



        initTemplatesRecyclerView();

    }

    private void initTemplatesRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.templatesRecyclerView);
        TemplateSelector.TemplatesRecyclerViewAdapter adapter = new TemplateSelector.TemplatesRecyclerViewAdapter(mTemplatesNames,mUrlsTemplatesList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class TemplatesRecyclerViewAdapter extends RecyclerView.Adapter<TemplateSelector.TemplatesRecyclerViewAdapter.ViewHolder> {


        private final ArrayList<String> mUrlsTemplatesList;
        private ArrayList<String> mTemplatesNames = new ArrayList<>();
        private Context mContext;


        public TemplatesRecyclerViewAdapter(ArrayList<String> mTemplatesNames, ArrayList<String> mUrlsTemplatesList, Context mContext) {

            String url = "https://api.imgflip.com/get_memes";
            RequestQueue queue = Volley.newRequestQueue(mContext);

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

                                }

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


            this.mTemplatesNames = nameTemplatesList;
            this.mUrlsTemplatesList = urlTemplatesList;
            this.mContext = mContext;
        }


        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_templates,parent,false);
            TemplateSelector.TemplatesRecyclerViewAdapter.ViewHolder holder = new TemplateSelector.TemplatesRecyclerViewAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TemplateSelector.TemplatesRecyclerViewAdapter.ViewHolder holder, final int position) {


            Glide.with(mContext)
                    .load(mUrlsTemplatesList.get(position))
                    .into(holder.memeTemplate);
            holder.templatesNames.setText(mTemplatesNames.get(position));


            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urlToPass = mUrlsTemplatesList.get(position);
                    System.out.println("LKUFOTIGT " + urlToPass );
                    Intent intent = new Intent(TemplateSelector.this, MainActivity.class);
                    intent.putExtra("urlToPass", urlToPass);
                    startActivity(intent);
                }
            });



        }

        @Override
        public int getItemCount() {
            return mTemplatesNames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView templatesNames;
            RelativeLayout parentLayout;
            ImageView memeTemplate;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                memeTemplate = itemView.findViewById(R.id.memeTemplate);
                templatesNames =  itemView.findViewById(R.id.templatesNames);
                parentLayout = itemView.findViewById(R.id.parentLayoutTemplates);
            }
        }
    }

/*    public void previousOnClick (View view){
        if (indexTemplate>0) {
            Glide.with(this).load(urlTemplatesList.get(indexTemplate)).into(memeTemplate);
            indexTemplate--;
        }

    }

    public void nextOnClick (View view){
        if (indexTemplate<100) {
            Glide.with(this).load(urlTemplatesList.get(indexTemplate)).into(memeTemplate);
            indexTemplate++;
        }
    }*/
}