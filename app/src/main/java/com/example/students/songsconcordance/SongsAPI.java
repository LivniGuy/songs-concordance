package com.example.students.songsconcordance;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Guy on 13/09/2015.
 */
public interface SongsAPI {
    @GET("/feeds/songs.php")
    void getSongFeed(@QueryMap Map<String, String> params, Callback<List<Song>> response);

    @GET("/feeds/songlyrics.php")
    void getSongLyricsFeed(@Query("songid") String songID, Callback<String> response);

    @GET("/feeds/songlyricsindex.php")
    void getSongLyricsIndexFeed(@Query("songid") String songID,
                                @Query("USER_ID") String userID,
                                @Query("WORD_GROUP_ID") String chosenWordGroupID, Callback<String> response);

    @GET("/feeds/wordbylocation.php")
    void getWordByLocationFeed(@Query("songid") String songid,
                               @Query("wordline") String wordline,
                               @Query("wordinline") String wordinline,
                               @Query("stanza") String stanza, Callback<String> response);

    @GET("/feeds/wordgroups.php")
    void getWordGroupsFeed(@Query("USER_ID") String userID, Callback<List<JsonObject>> response);

    @GET("/feeds/wordgroupwords.php")
    void getWordGroupWordsFeed(@Query("USER_ID") String userID,
                               @Query("WORD_GROUP_NAME") String wordGroupName,
                               Callback<List<JsonObject>> response);

    @GET("/feeds/lingexps.php")
    void getLingExpsFeed(@Query("USER_ID") String userID, Callback<List<JsonObject>> response);

    @GET("/feeds/wordslist.php")
    void getWordsList(@Query("songid") String songid, Callback<List<JsonObject>> response);

    @GET("/manage/login.php")
    void getLogin(@QueryMap Map<String, String> params, Callback<User> response);

    @FormUrlEncoded
    @POST("/manage/definewordgroup.php")
    void postDefineWordGroup(@FieldMap Map<String, String> params, Callback<String> response);

    @FormUrlEncoded
    @POST("/manage/definelingexp.php")
    void postDefineLingExp(@FieldMap Map<String, String> params, Callback<String> response);

    @FormUrlEncoded
    @POST("/manage/deleteobject.php")
    void postDeleteObject(@Field("USER_ID") String userID,
                          @Field("OBJECT_TYPE") String objectType,
                          @Field("OBJECT_NAME") String objectName,
                          Callback<String> response);

}
