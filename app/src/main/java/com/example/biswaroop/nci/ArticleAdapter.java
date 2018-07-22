package com.example.biswaroop.nci;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

//    /* Get the class name*/
//    final String TAG = "TAG: "+ this.getClass().getSimpleName().toString();
//    /* Get the class name*/

    private List<Article> articleList;
    private Context ctx;

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, sourceName, author, publishedAt;
        ImageView imgStory;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            /* Get the method name*/
//            class Local{};
//            String strMethod = Local.class.getEnclosingMethod().getName();
//            Log.e(TAG,strMethod);
            /* Get the method name*/

            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            sourceName = itemView.findViewById(R.id.tvSourceName);
            author = itemView.findViewById(R.id.tvAuthor);
            publishedAt = itemView.findViewById(R.id.tvPublishedAt);
            imgStory = itemView.findViewById(R.id.ivNewsImage);
        }
    }

    public ArticleAdapter(Context context, List<Article> articleList) {
//        /* Get the method name*/
//        class Local{};
//        String strMethod = "ArticleAdapter";
////        strMethod = Local.class.getEnclosingMethod().getName();
//        Log.e(TAG,strMethod);
//        /* Get the method name*/

        this.articleList = articleList;
        this.ctx = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

//        /* Get the method name*/
//        class Local{};
//        String strMethod = Local.class.getEnclosingMethod().getName();
//        Log.e(TAG,strMethod);
//        /* Get the method name*/

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View inflatedView = layoutInflater.inflate(R.layout.recyclerview_singlerow,
                viewGroup,false);
        return new ArticleViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder articleViewHolder, int i) {

        Article currentArticle = articleList.get(i);

//        Source bindSource = currentArticle.getSource();
        String imgURL = "";
        try {
            imgURL = currentArticle.getUrlToImage();
        } catch (Exception e) {

            Log.e("TAG","Error: " + e.getMessage());
        }

        final String urlStory = currentArticle.getUrl();
        String strAuthor = "",strDesc="",strSourceName="";

        articleViewHolder.title.setText(currentArticle.getTitle());

        strDesc = currentArticle.getDescription();
        if (strDesc == null || strDesc.equals("null")){
            strDesc = " ";
        }
        articleViewHolder.description.setText(strDesc);

//        strSourceName = currentArticle.getSource().getName();
        strSourceName = currentArticle.source.getName;
        if (strSourceName == null || strSourceName.equals("null")){
            strSourceName = " ";
        }
        articleViewHolder.sourceName.setText(strSourceName);

        strAuthor = currentArticle.getAuthor();
        if (strAuthor == null || strAuthor.equals("null")){
            strAuthor = " ";
        }
        articleViewHolder.author.setText(strAuthor);
        articleViewHolder.publishedAt.setText(currentArticle.getPublishedAt());

        try{
            articleViewHolder.imgStory.setImageURI(Uri.parse(imgURL));

            Picasso.get()
                    .load(imgURL)
                    .into(articleViewHolder.imgStory);

        } catch (Exception e){
            Log.e("TAG","Error: " + e.getMessage());
        }

        articleViewHolder.imgStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = articleViewHolder.itemView.getContext();
                Toast.makeText(ctx, "Opening news website!",Toast.LENGTH_SHORT).show();
                Intent intentOpenUrl = new Intent();
                intentOpenUrl.setAction(Intent.ACTION_VIEW);
                intentOpenUrl.setData(Uri.parse(urlStory));
                Bundle bundle = new Bundle();
                startActivity(view.getContext(),intentOpenUrl,bundle);
            }
        });

        articleViewHolder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = articleViewHolder.itemView.getContext();
                Toast.makeText(ctx, "Opening news website!",Toast.LENGTH_SHORT).show();
                Intent intentOpenUrl = new Intent();
                intentOpenUrl.setAction(Intent.ACTION_VIEW);
                intentOpenUrl.setData(Uri.parse(urlStory));
                Bundle bundle = new Bundle();
                startActivity(view.getContext(),intentOpenUrl,bundle);
            }
        });

        articleViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = articleViewHolder.itemView.getContext();
                Toast.makeText(ctx, "Opening news website!",Toast.LENGTH_SHORT).show();
                Intent intentOpenUrl = new Intent();
                intentOpenUrl.setAction(Intent.ACTION_VIEW);
                intentOpenUrl.setData(Uri.parse(urlStory));
                Bundle bundle = new Bundle();
                startActivity(view.getContext(),intentOpenUrl,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {

//        /* Get the method name*/
//        class Local{};
//        String strMethod = Local.class.getEnclosingMethod().getName();
//        Log.e(TAG,strMethod);
//        /* Get the method name*/

        if(articleList != null) {
            return articleList.size();
        }else
        return 0;
    }

    void setArticleList(List<Article> articleList) {

//        /* Get the method name*/
//        class Local{};
//        String strMethod = Local.class.getEnclosingMethod().getName();
//        Log.e(TAG,strMethod);
//        /* Get the method name*/

        this.articleList = articleList;
        notifyDataSetChanged();
    }

}
