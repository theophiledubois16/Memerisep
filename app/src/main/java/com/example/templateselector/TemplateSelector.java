package com.example.templateselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TemplateSelector extends AppCompatActivity {

    public ArrayList<String> idTemplatesList = new ArrayList<>();
    public ArrayList<String> nameTemplatesList = new ArrayList<>();
    public ArrayList<String> urlTemplatesList = new ArrayList<>();

    ImageView memeTemplate;
    public int indexTemplate = 0;

    private String urlToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_selector);
        Intent intentImgflip = getIntent();
        urlTemplatesList = intentImgflip.getStringArrayListExtra("intentImgflip");
        nameTemplatesList = intentImgflip.getStringArrayListExtra("intentNameImgflip");
        memeTemplate = findViewById(R.id.memeTemplate);


        initTemplatesRecyclerView();

    }

    private void initTemplatesRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.templatesRecyclerView);
        TemplateSelector.TemplatesRecyclerViewAdapter adapter = new TemplateSelector.TemplatesRecyclerViewAdapter(nameTemplatesList,urlTemplatesList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class TemplatesRecyclerViewAdapter extends RecyclerView.Adapter<TemplateSelector.TemplatesRecyclerViewAdapter.ViewHolder> {


        private final ArrayList<String> mUrlsTemplatesList;
        private final ArrayList<String> mTemplatesNames;
        private Context mContext;


        public TemplatesRecyclerViewAdapter(ArrayList<String> mTemplatesNames, ArrayList<String> mUrlsTemplatesList, Context mContext) {


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
                    Intent intent = new Intent(TemplateSelector.this, MainActivity.class);
                    intent.putExtra("urlToPass", urlToPass);
                    startActivity(intent);
                }
            });



        }

        @Override
        public int getItemCount() {
            return mUrlsTemplatesList.size();
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