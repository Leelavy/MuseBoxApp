package com.lilo.museboxapp.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.lilo.museboxapp.MuseBoxApplication;
import java.util.List;

public class Model {

    public static final Model instance = new Model();

    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private Model(){
    }

    @SuppressLint("StaticFieldLeak")
    public void addPost(final Post post, Listener<Boolean> listener) {
        ModelFirebase.addPost(post,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.postDao().insertAllPosts(post);
                return "";
            }
        }.execute();
    }

//    @SuppressLint("StaticFieldLeak")
//    public void updatePost(final Post post, Listener<Boolean> listener) {
//        ModelFirebase.updatePost(post, listener);
//        new AsyncTask<String,String,String>(){
//            @Override
//            protected String doInBackground(String... strings) {
//                AppLocalDb.db.postDao().insertAllPosts(post);
//                return "";
//            }
//        }.execute();
//    }

    @SuppressLint("StaticFieldLeak")
    public void deletePost(final Post post, Listener<Boolean> listener){
        ModelFirebase.deletePost(post,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.postDao().deletePost(post);
                return "";
            }
        }.execute();
    }

    public void refreshPostsList(final CompListener listener){
        long lastUpdated = MuseBoxApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate",0);
        ModelFirebase.getAllPostsSince(lastUpdated,new Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Post p: data){
                            AppLocalDb.db.postDao().insertAllPosts(p);
                            if (p.lastUpdated > lastUpdated)
                                lastUpdated = p.lastUpdated;
                        }
                        SharedPreferences.Editor edit = MuseBoxApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                        edit.putLong("PostsLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)
                            listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    public LiveData<List<Post>> getAllPosts(){
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getAllPosts();
        refreshPostsList(null);
        return liveData;
    }


    public void update(Post post){

    }

    public void updateUserProfile(String username, String info, String profileImgUrl, Listener<Boolean> listener) {
        ModelFirebase.updateUserProfile(username, info, profileImgUrl, listener);
    }

    public void setUserAppData(String email){
        ModelFirebase.setUserAppData(email);
    }


}
