package com.theappfactory.ChattrBox.SimpleNewsChat;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.annotation.NonNull;

@Entity(tableName = "article_table")
public class Article { //implements ValueEventListener {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    int commentCount = -1;
    String countryCode = " ";

    String author, title, description, url, urlToImage, publishedAt;
//    String sourceId, sourceName;

    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded
    Source source;

    public Article(String id, String author, String title, String description, String url,
                   String urlToImage, String publishedAt, int commentCount, String countryCode) {

        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.commentCount = commentCount;
        this.countryCode = countryCode;
    }

//    public Article() {
//    }

//    public Source getSource() {
//        return source;
//    }
//
//    public void setSource(Source source) {
//        this.source = source;
//    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getCommentCount() {
//        return getNCIFirebaseCommentCount(id);
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
//        if (commentCount == -1) {
//            getNCIFirebaseCommentCount(id);
//        } else {
            this.commentCount = commentCount;
//        }
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getId() {
        String id = cleanString(url,"/");
        if (id.equals("indexhtml")) {
            id = id+publishedAt;
        }
        else if (id.equals("storyhtml")) {
            id = id+publishedAt;
        }
        else if (id.equals("posthtml")) {
            id = id+publishedAt;
        }
        else if (id.equals("email")) {
            id = id+publishedAt;
        }
        return id;
    }

    public void setId(String id) {
        this.id = cleanString(id,"/");
    }

    public String cleanString(String strToClean, String splChar) {
        String dirtyString = strToClean;
        String [] arrayOfStrings = dirtyString.split(splChar);
        String lastBlockofString = arrayOfStrings[arrayOfStrings.length - 1];
        String cleanString = lastBlockofString.replaceAll("[^\\w\\s]","");

        return cleanString;
    }

//    public void getNCIFirebaseCommentCount(String articleId) {
//        //Variables
//        FirebaseDatabase nciFirebaseDatabase;
//        DatabaseReference nciFirebaseDatabaseReference;
////        int commentCount;
//
//        //get the comment count
//        nciFirebaseDatabase = FirebaseDatabase.getInstance();
//        nciFirebaseDatabaseReference = nciFirebaseDatabase.getReference(FIREBASE_TABLE_COMMENTS);
//
//        nciFirebaseDatabaseReference.child("nci-test/comments/" + articleId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        int count = (int) dataSnapshot.getChildrenCount();
//                        setCommentCount(count);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//
//        //
//    }

//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        this.commentCount = (int) dataSnapshot.getChildrenCount();
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//    }
}
