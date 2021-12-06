package com.theappfactory.ChattrBox.SimpleNewsChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.KEY_SELECTED_ARTICLE_ID;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.KEY_SELECTED_ARTICLE_IMAGE_URL;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.KEY_SELECTED_ARTICLE_TITLE;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.KEY_SELECTED_ARTICLE_URL;

//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.ui.PlayerView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;
    private Context ctx;
//    LiveData <String> strArticleId;
    String strArticleId = "hello";
    NCIRoomDatabase nciRoomDatabase = null;
    ArticleDAO articleDAO = null;
    TextToSpeech tts1 = null;

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView articleId, title, description, sourceName, author, publishedAt, commentCount;
        TextView tvLeftSeparator, tvRightSeparator;
        EditText etComment;
        FloatingActionButton fabGotoArticleComment, fabSendComment;
        ImageView imgStory;
//        PlayerView playerViewNewsVideo;
        ProgressBar progressBarStoryImage;
        TextView tvLoading;
        Button btnShare, btnLike;


        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            sourceName = itemView.findViewById(R.id.tvSourceName);
            author = itemView.findViewById(R.id.tvAuthor);
            publishedAt = itemView.findViewById(R.id.tvPublishedAt);
            imgStory = itemView.findViewById(R.id.ivNewsImage);
//            playerViewNewsVideo = itemView.findViewById(R.id.playerViewNewsVideo);
            etComment = itemView.findViewById(R.id.etChatComment);
            fabSendComment = itemView.findViewById(R.id.fabChatSendComment);
            articleId = itemView.findViewById(R.id.tvArticleId);
            fabGotoArticleComment = itemView.findViewById(R.id.fabGotoArticleComment);
            commentCount = itemView.findViewById(R.id.tvCommentCount);
            progressBarStoryImage = itemView.findViewById(R.id.progressBarStoryImage);
            tvLoading = itemView.findViewById(R.id.tvLoading);
            tvLeftSeparator = itemView.findViewById(R.id.tvLeftSeparator);
            tvRightSeparator = itemView.findViewById(R.id.tvRightSeparator);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }

    public ArticleAdapter(Context context, List<Article> articleList) {
        this.articleList = articleList;
        this.ctx = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        try {
            View inflatedView = layoutInflater.inflate(R.layout.recyclerview_singlerow,
                    viewGroup, false);
            return new ArticleViewHolder(inflatedView);
        } catch ( Exception e) {
            Log.e("TAG", "onCreateViewHolder: Error inflating rv row:" + e.getMessage() );
            return null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder articleViewHolder, int i) {

        nciRoomDatabase = NCIRoomDatabase.getDatabase(ctx);
        articleDAO = nciRoomDatabase.getArticleDAO();
        StateVariables stateVariables = StateVariables.getINSTANCE();

        final Article currentArticle = articleList.get(i);
        Log.e("TAG", "currentArticle: " + i);
        //Insert speaker here TODO //

        tts1 = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int ttsStatus) {
                if (ttsStatus == TextToSpeech.SUCCESS) {
                    int ttsResult = tts1.setLanguage(Locale.US);
                    if (ttsResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This language is not supported");
                    } else {
                        //speak news
                        Log.d("ArticleAdapter","SpeakText = " + stateVariables.getSpeakText());
                        if (stateVariables.getSpeakText() == 1){
                            Log.d("ArticleAdapter", "getSpeakText = " + stateVariables.getSpeakText());
                            tts1.speak("Reading Text", TextToSpeech.QUEUE_FLUSH, null, "my Text");
//                        tts1.speak("Article number: "+ i, TextToSpeech.QUEUE_FLUSH, null, "article number");
                            tts1.speak(currentArticle.getTitle(),TextToSpeech.QUEUE_FLUSH,null,"articleTitle");
//                        ConvertTextToSpeech();
                        } else{
                            Log.d("ArticleAdapter", "getSpeakText = " + stateVariables.getSpeakText());
                        }
                    }
                } else {
                    Log.e("error", "Initialization Failed!");
                }
            }
        });



//        Source bindSource = currentArticle.getSource();
        String imgURL = "";
        try {
            imgURL = currentArticle.getUrlToImage();
            if (imgURL == null) {
                imgURL = "";
            }
        } catch (Exception e) {
            Log.e("TAG","Error: " + e.getMessage());
        }

        final String urlStory = currentArticle.getUrl();
        String strAuthor = "",strDesc="",strSourceName="";

//Not used -start
//        int dpiDevice = Resources.getSystem().getDisplayMetrics().densityDpi;
//        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
//        int heightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
//
//        float xdpi = Resources.getSystem().getDisplayMetrics().xdpi;
//        float ydpi = Resources.getSystem().getDisplayMetrics().ydpi;
//
//        float XScreenIN = widthPixels/xdpi;
//        float YScreenIN = heightPixels/ydpi;
//
//        float Area = XScreenIN * YScreenIN;
//
////        Toast.makeText(ctx, dpiDevice+", "+widthPixels+", "+heightPixels, Toast.LENGTH_SHORT).show();
////
////        Toast.makeText(ctx, xdpi+", "+ydpi+", "+Area, Toast.LENGTH_SHORT).show();
////        Toast.makeText(ctx, XScreenIN+", "+YScreenIN+", "+Area, Toast.LENGTH_SHORT).show();
//
////        articleViewHolder.title.setText((int)(articleViewHolder.title.getTextSize() * dpiDevice / 160));
////        articleViewHolder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
////        articleViewHolder.title.setTextSize(24);
////Not used -start

        try{
            articleViewHolder.title.setText(currentArticle.getTitle());
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: " + e.getMessage() );
        }

        try {
            strDesc = currentArticle.getDescription();
            if ((strDesc == null) || strDesc.equals("null") || strDesc.isEmpty()) {
//                strDesc = " ";
                strDesc = articleViewHolder.description.getHint().toString();
                articleViewHolder.description.setText(strDesc);
                articleViewHolder.description.setTextColor(Color.LTGRAY);
            } else {
                articleViewHolder.description.setText(strDesc);
                articleViewHolder.description.setTextColor(ContextCompat.getColor(ctx,R.color.colorText));
            }
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: " + e.getMessage() );
        }

//        if (currentArticle.getDescription().isEmpty()
//                || currentArticle.getDescription().equals("null")
//                || currentArticle.getDescription().equals("")) {
//            strDesc.equals(" ");
//        }
//        if ( strDesc == null || strDesc.equals("null")){
//            strDesc = " ";
//        }
//        articleViewHolder.description.setTextSize((float) (articleViewHolder.description.getTextSize() * 1.0));


//        strSourceName = currentArticle.getSource().getName();
        try {
            strSourceName = currentArticle.source.getName;
            if ((strSourceName == null) || strSourceName.isEmpty() || strSourceName.equals("null")) {
                strSourceName = " ";
            }
            articleViewHolder.sourceName.setText(strSourceName);
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: " + e.getMessage() );
        }

        try {
            strAuthor = currentArticle.getAuthor();
            if ((strAuthor == null) || strAuthor.isEmpty() || strAuthor.equals("null") || strAuthor.equals(" ")) {
                strAuthor = " ";
                articleViewHolder.author.setText(strAuthor);
                articleViewHolder.tvLeftSeparator.setVisibility(View.INVISIBLE);
                articleViewHolder.tvRightSeparator.setVisibility(View.INVISIBLE);
            } else {
                articleViewHolder.author.setText(strAuthor);
                articleViewHolder.tvLeftSeparator.setVisibility(View.VISIBLE);
                articleViewHolder.tvRightSeparator.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: " + e.getMessage() );
        }

        String strCommentCount = "0";
        if (currentArticle.getCommentCount() == 0) {
            articleViewHolder.commentCount.setText(strCommentCount);
        } else {
            strCommentCount = currentArticle.getCommentCount()+"";
            articleViewHolder.commentCount.setText(strCommentCount);
        }

        try {
            String strDateFromServer = currentArticle.getPublishedAt();
            SimpleDateFormat publishedAtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            publishedAtFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            long time = publishedAtFormat.parse(strDateFromServer).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            Log.e("TAG", "onBindViewHolder: "+ago.toString() );
//            return publishedAt+"("+ago.toString()+")";
//            articleViewHolder.publishedAt.setText(strDateFromServer +"("+ ago.toString()+")");
            articleViewHolder.publishedAt.setText(ago.toString());
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: "+ e.getMessage());
            articleViewHolder.publishedAt.setText(" ");
        }


//        articleViewHolder.playerViewNewsVideo.setPlayer (leExoPlayer simpleExoPlayer);

        try{
            articleViewHolder.imgStory.setImageURI(Uri.parse(imgURL));

//            Picasso.get()
//                    .load(imgURL)
//                    .into(articleViewHolder.imgStory);
            articleViewHolder.progressBarStoryImage.setVisibility(View.VISIBLE);
            articleViewHolder.tvLoading.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(imgURL)
                    .into(articleViewHolder.imgStory, new Callback() {
                        @Override
                        public void onSuccess() {
                            articleViewHolder.progressBarStoryImage.setVisibility(View.GONE);
                            articleViewHolder.tvLoading.setVisibility(View.GONE);
//                            Toast.makeText(ctx, "image loaded~!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            articleViewHolder.progressBarStoryImage.setVisibility(View.VISIBLE);
                            articleViewHolder.tvLoading.setVisibility(View.VISIBLE);
                            Log.e("TAG", "onError: " + e.getMessage() );
                        }
                    });

        } catch (Exception e){
            Log.e("TAG","Error loading imgStory: " + e.getMessage());
        }

        //**Find which is the focussed article**//

//        strArticleId = currentArticle.getId();
//        articleViewHolder.articleId.setText(strArticleId);

        int itemId = articleViewHolder.getAdapterPosition();
        Log.e("TAG", "itemID: " +itemId);
//        final StateVariables stateVariables = StateVariables.getINSTANCE();
//        stateVariables.setStrSelectedAriticleId(currentArticle.getId());
        articleViewHolder.articleId.setText(currentArticle.getId());

//        articleViewHolder.imgStory.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("TAG", "onTouch: " + currentArticle.getId());
////                StateVariables stateVariables = StateVariables.getINSTANCE();
////                stateVariables.setStrSelectedAriticleId(strArticleId);
//                return false;
//            }
//        });
//
////        articleViewHolder.title.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("TAG", "onTouch: " + strArticleId);
//                StateVariables stateVariables = StateVariables.getINSTANCE();
//                stateVariables.setStrSelectedAriticleId(strArticleId);
//                return false;
//            }
//        });
//
//        articleViewHolder.description.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("TAG", "onTouch: " + strArticleId);
//                StateVariables stateVariables = StateVariables.getINSTANCE();
//                stateVariables.setStrSelectedAriticleId(strArticleId);
//                return false;
//            }
//        });
//
//        articleViewHolder.author.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("TAG", "onTouch: " + strArticleId);
//                StateVariables stateVariables = StateVariables.getINSTANCE();
//                stateVariables.setStrSelectedAriticleId(strArticleId);
//                return false;
//            }
//        });
//
//        articleViewHolder.publishedAt.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("TAG", "onTouch: " + strArticleId);
//                StateVariables stateVariables = StateVariables.getINSTANCE();
//                stateVariables.setStrSelectedAriticleId(strArticleId);
//                return false;
//            }
//        });
//
//        articleViewHolder.sourceName.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("TAG", "onTouch: " + strArticleId);
//                StateVariables stateVariables = StateVariables.getINSTANCE();
//                stateVariables.setStrSelectedAriticleId(strArticleId);
//                return false;
//            }
//        });
//        //****************************************//


        articleViewHolder.imgStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = articleViewHolder.itemView.getContext();
                Intent intentStartWebView = new Intent(ctx,DetailedActivity.class);
                intentStartWebView.putExtra(KEY_SELECTED_ARTICLE_URL, currentArticle.getUrl());
                stateVariables.setStrSelectedAriticleId(currentArticle.getId());
                ctx.startActivity(intentStartWebView);

//                Toast.makeText(ctx, "Opening news website!", Toast.LENGTH_SHORT).show();
//                Intent intentOpenUrl = new Intent();
//                intentOpenUrl.setAction(Intent.ACTION_VIEW);
//                intentOpenUrl.setData(Uri.parse(urlStory));
//                Bundle bundle = new Bundle();
//                startActivity(view.getContext(),intentOpenUrl,bundle);
            }
        });

//        //** Swipe R-->L to delete -- start
//        stateVariables.setOldXValueForSwipe(0f);
////        Float oldTouchValueX = 0f;
//        articleViewHolder.imgStory.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                Toast.makeText(ctx, "touched!!", Toast.LENGTH_SHORT).show();
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                    {
////                        Toast.makeText(ctx, "UP", Toast.LENGTH_SHORT).show();
//
//                        float currentX = event.getX();
//
//                        if (stateVariables.getOldXValueForSwipe() < currentX) {
//                            //swiped right
////                            Toast.makeText(ctx, "swiped right", Toast.LENGTH_SHORT).show();
//                            AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(ctx);
//
//                            deleteAlertBuilder.setMessage(R.string.alert_message_delete_single)
//                                    .setPositiveButton(R.string.alert_message_delete_yes, new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            int indexOfDeleted = articleList.indexOf(currentArticle);
//                                            articleDAO.deleteArticle(currentArticle);
////                                            Toast.makeText(ctx, "Deleted articles from DB.", Toast.LENGTH_SHORT).show();
//                                            Boolean isRemoved = articleList.remove(currentArticle);
//                                            if (isRemoved) {
////                                                Toast.makeText(ctx, "Hiding article till next refresh!", Toast.LENGTH_SHORT).show();
//                                            }
//                                            else {
//                                                Toast.makeText(ctx, "Unable to hide article from List!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    })
//                                    .setNegativeButton(R.string.alert_message_delete_no, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // do nothing
//                                            Log.e("TAG", "onClick: Not deleting article from DB.");
//                                        }
//                                    });
//                            AlertDialog deleteAlertDialog = deleteAlertBuilder.create();
//                            deleteAlertDialog.show();
//
//
//
////                            articleDAO.deleteArticle(currentArticle);
//
//                        }
//
//                        if (stateVariables.getOldXValueForSwipe() > currentX) {
//                            //swiped left
////                            Toast.makeText(ctx, "swiped left", Toast.LENGTH_SHORT).show();
//                        }
//
//                        break;
//                    }
//
//                    case MotionEvent.ACTION_DOWN:
//                    {
////                        Toast.makeText(ctx, "DOWN", Toast.LENGTH_SHORT).show();
//                        stateVariables.setOldXValueForSwipe(event.getX());
//                        break;
//                    }
//
//                }
//                return true;
//            }
//        });
//        //** Swipe R-->L to delete -- end

        articleViewHolder.imgStory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(ctx, "Delete this article from db", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(ctx);

                deleteAlertBuilder.setMessage(R.string.alert_message_delete_single)
                        .setPositiveButton(R.string.alert_message_delete_yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int indexOfDeleted = articleList.indexOf(currentArticle);
                                articleDAO.deleteArticle(currentArticle);
//                                            Toast.makeText(ctx, "Deleted articles from DB.", Toast.LENGTH_SHORT).show();
                                Boolean isRemoved = articleList.remove(currentArticle);
                                if (isRemoved) {
//                                                Toast.makeText(ctx, "Hiding article till next refresh!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ctx, "Unable to hide article from List!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.alert_message_delete_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Log.e("TAG", "onClick: Not deleting article from DB.");
                            }
                        });
                AlertDialog deleteAlertDialog = deleteAlertBuilder.create();
                deleteAlertDialog.show();

                return true;
            }
        });

        articleViewHolder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = articleViewHolder.itemView.getContext();
//                Toast.makeText(ctx, "Opening news website!",Toast.LENGTH_SHORT).show();
                Intent intentStartWebView = new Intent(ctx,DetailedActivity.class);
                intentStartWebView.putExtra(KEY_SELECTED_ARTICLE_URL, currentArticle.getUrl());
                stateVariables.setStrSelectedAriticleId(currentArticle.getId());
//                Toast.makeText(ctx,stateVariables.getStrSelectedAriticleId(), Toast.LENGTH_SHORT).show();
                ctx.startActivity(intentStartWebView);

//                Intent intentOpenUrl = new Intent();
//                intentOpenUrl.setAction(Intent.ACTION_VIEW);
//                intentOpenUrl.setData(Uri.parse(urlStory));
//                Bundle bundle = new Bundle();
//                startActivity(view.getContext(),intentOpenUrl,bundle);
            }
        });

        articleViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = articleViewHolder.itemView.getContext();
                Intent intentStartWebView = new Intent(ctx,DetailedActivity.class);
                intentStartWebView.putExtra(KEY_SELECTED_ARTICLE_URL, currentArticle.getUrl());
                stateVariables.setStrSelectedAriticleId(currentArticle.getId());
                ctx.startActivity(intentStartWebView);
//                Toast.makeText(ctx, "Opening news website!",Toast.LENGTH_SHORT).show();
//                Intent intentOpenUrl = new Intent();
//                intentOpenUrl.setAction(Intent.ACTION_VIEW);
//                intentOpenUrl.setData(Uri.parse(urlStory));
//                Bundle bundle = new Bundle();
//                startActivity(view.getContext(),intentOpenUrl,bundle);
            }
        });

        articleViewHolder.fabGotoArticleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//moving start
//                Toast.makeText(ctx,"adapter: "+currentArticle.getCountryCode(), Toast.LENGTH_SHORT).show();
                Intent intentStartChat = new Intent(ctx,NCIActivity.class);
                intentStartChat.putExtra(KEY_SELECTED_ARTICLE_ID, currentArticle.getId());
                intentStartChat.putExtra(KEY_SELECTED_ARTICLE_IMAGE_URL, currentArticle.getUrlToImage());
                intentStartChat.putExtra(KEY_SELECTED_ARTICLE_TITLE, currentArticle.getTitle());
                stateVariables.setStrSelectedAriticleId(currentArticle.getId());
//                Toast.makeText(ctx,stateVariables.getStrSelectedAriticleId(), Toast.LENGTH_SHORT).show();
                ctx.startActivity(intentStartChat);
//moving end
//                articleViewHolder.fabGotoArticleComment.hide();

//                stateVariables.setCurrentArticleId(currentArticle.getId());
//                stateVariables.setCurrentArticleUrlToImage(currentArticle.getUrlToImage());
//                stateVariables.setCurrentArticleTitle(currentArticle.getTitle());

            }
        });

        articleViewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
////                sharingIntent.setType("text/plain");
//                sharingIntent.setType("image/*");
//
//                String imageUrl = currentArticle.getUrlToImage();
//
//                String shareBody = "https://play.google.com/store/apps/details?id=com.theappfactory.ChattrBox.SimpleNewsChat";
//                String shareSubject = currentArticle.getTitle();
////                String shareBody = currentArticle.getUrl();
//                sharingIntent.setData(Uri.parse(imageUrl));
////                sharingIntent.setPackage("com.whatsapp");
//                sharingIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(imageUrl));
////                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
////                sharingIntent.putExtra(Intent.EXTRA_REFERRER_NAME, shareSubject);
////                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                Intent.createChooser(sharingIntent, "Share via");
//                try {
////                    ctx.startActivity(sharingIntent);
//                } catch (Exception e) {
//                    Log.e("TAG", "btnShare.onClick: " + e.getMessage());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {

        if(articleList != null) {
            return articleList.size();
        }else
        return 0;
    }

    void setArticleList(List<Article> articleList) {

        this.articleList = articleList;
        notifyDataSetChanged();
    }

    public void updateList(List<Article> newList){
        articleList = newList;
        notifyDataSetChanged();
    }
}
