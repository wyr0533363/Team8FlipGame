package iss.workshop.team8flipgame.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.team8flipgame.model.Score;

public class DBService extends SQLiteOpenHelper {
    //declare required values
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FlipGamesDB";
    private static final String TABLE_NAME = "ScoresTable";

    //declare table column name
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
    private static final String KEY_DIFFICULTY = "difficulty";

    public DBService(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createDb = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_SCORE + " TEXT," +
                KEY_DIFFICULTY + " TEXT"
                + " )";
        sqLiteDatabase.execSQL(createDb);

        //Seed data
        addScore(new Score("Daryl", 123, "Hard"));
        addScore(new Score("Daryl1", 222, "Normal"));
    }

    //upgrade db if older version exists
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i >= i1){
            return;
        }
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //insert operation
    public long addScore(Score score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_NAME, score.getName());
        v.put(KEY_SCORE, score.getScore());
        v.put(KEY_DIFFICULTY, score.getDifficulty());
        //insert to db
        Long ID = db.insert(TABLE_NAME, null, v);
        return ID;
    }

    public List<Score> getAllScore(){
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_SCORE +" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                Score score = new Score();
                score.setId(Long.parseLong(cursor.getString(0)));
                score.setName(cursor.getString(1));
                score.setScore(Integer.parseInt(cursor.getString(2)));
                score.setDifficulty(cursor.getString(3));
                scores.add(score);
            }while(cursor.moveToNext());
        }
        return scores;
    }

    public List<Score> getAllScore(String difficulty){
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_DIFFICULTY + " = '" + difficulty + "' ORDER BY " + KEY_SCORE +" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                Score score = new Score();
                score.setId(Long.parseLong(cursor.getString(0)));
                score.setName(cursor.getString(1));
                score.setScore(Integer.parseInt(cursor.getString(2)));
                score.setDifficulty(cursor.getString(3));
                scores.add(score);
            }while(cursor.moveToNext());
        }
        return scores;
    }
}
