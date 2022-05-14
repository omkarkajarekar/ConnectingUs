package com.example.connectingus.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "RecentChatList.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table ChatListTable(name TEXT primary key, userId TEXT,lastMessage TEXT,lastMsgTime TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists ChatListTable");
    }

    public  Boolean insertUserData(String name,String userId,String lastMessage,String lastMsgTime)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("userId",userId);
        contentValues.put("lastMessage",lastMessage);
        contentValues.put("lastMsgTime",lastMsgTime);
        long result=DB.insert("ChatListTable",null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Boolean deleteUserData(String userId)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from ChatListTable where userId=?",new String[] {userId});
        if(cursor.getCount()>0)
        {
            long result=DB.delete("ChatListTable","userId=?",new String[] {userId});
            if(result==-1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public Cursor getData()
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from ChatListTable",null);
        return cursor;
    }
}
