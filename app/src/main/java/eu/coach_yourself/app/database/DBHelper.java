package eu.coach_yourself.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.coach_yourself.app.adapter.AdapterRemind;
import eu.coach_yourself.app.model.ModelFile;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DBVOY.db";
    public static final String CONTACTS_TABLE_NAME = "reminder";
    public static final String CONTACTS_COLUMN_ID = "rowid";
    public static final String CONTACTS_COLUMN_NAME = "title";
    public static final String CONTACTS_COLUMN_DAY = "days";
    public static final String CONTACTS_COLUMN_TIME = "time";
    public static final String CONTACTS_COLUMN_HR = "hr";
    public static final String CONTACTS_COLUMN_MINUT = "minut";

    private HashMap hp;
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table reminder " +
                        "( title text,days text,time text,hr text,minut,text)"

        );
//        db.execSQL(
//                "create table favourite " +
//                        "(category_id text,categoryname text,categorytitle text,language text,description text,text_color text,design_color text,category_iconimage text,card_image text,subscription text,contentname text,contentId text,mp3file text,mp3subscribe text)"
//
//        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reminder");
//        db.execSQL("DROP TABLE IF EXISTS favourite");
        onCreate(db);

    }

    public long insertreminder(String title, String days, String time, String hr, String minut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("days", days);
        contentValues.put("time", time);
        contentValues.put("hr", hr);
        contentValues.put("minut", minut);
        long id= db.insert("reminder", null, contentValues);
        return id;
    }

//    public boolean insert_favourite(String category_id, String categoryname, String categorytitle, String language, String description, String text_color, String design_color, String category_iconimage, String subscription, String contentname, String contentId, String mp3file, String mp3subscribe) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("category_id", category_id);
//        contentValues.put("categoryname", categoryname);
//        contentValues.put("categorytitle", categorytitle);
//        contentValues.put("language", language);
//        contentValues.put("description", description);
//        contentValues.put("text_color", text_color);
//        contentValues.put("design_color", design_color);
//        contentValues.put("category_iconimage", category_iconimage);
//        contentValues.put("subscription", subscription);
//        contentValues.put("contentId", contentId);
//        contentValues.put("contentname", contentname);
//        contentValues.put("mp3file", mp3file);
//        contentValues.put("mp3subscribe", mp3subscribe);
//
//
//        db.insert("favourite", null, contentValues);
//        return true;
//    }

    public boolean updateremind(String rowid, String title, String days, String time, String hr, String minut) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("days", days);
        contentValues.put("time", time);
        contentValues.put("hr", hr);
        contentValues.put("minut", minut);

        db.update("reminder", contentValues, CONTACTS_COLUMN_ID + "=?", new String[]{rowid});
        return true;
    }

  /*  public ModelFile getReminderData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from reminder where rowid="+id, null );

        ModelFile modelFile = null;
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID);
            int index2 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME);
            int index3 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_DAY);
            int index4 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_TIME);


            String rowid=cursor.getString(index1);
            String txt_remidtitle = cursor.getString(index2);
            String txt_remindday = cursor.getString(index3);
            String txt_remindtime = cursor.getString(index4);
            modelFile = new ModelFile(rowid, txt_remidtitle, txt_remindday, txt_remindtime);
        }
        return modelFile;
    }*/

    public List<ModelFile> getAllReminderData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select rowid,title,days,time,hr,minut from reminder", null);
        List<ModelFile> list = new ArrayList<>();
        AdapterRemind adapterRemind = new AdapterRemind(context, list);

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID);
            int index2 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME);
            int index3 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_DAY);
            int index4 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_TIME);
            int index5 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_HR);
            int index6 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_MINUT);


            String rowid = cursor.getString(index1);
            String txt_remidtitle = cursor.getString(index2);
            String txt_remindday = cursor.getString(index3);
            String txt_remindtime = cursor.getString(index4);
            String remind_hr = cursor.getString(index5);
            String remind_minut = cursor.getString(index6);
            ModelFile modelFile = new ModelFile(rowid, txt_remidtitle, txt_remindday, txt_remindtime, remind_hr, remind_minut);
            modelFile.setRowid(rowid);
            modelFile.setRemind_time(txt_remindtime);
            modelFile.setRemind_day(txt_remindday);
            modelFile.setRemind_hr(remind_hr);
            modelFile.setRemind_title(txt_remidtitle);
            modelFile.setRemind_minut(remind_minut);
            list.add(modelFile);
        }
        adapterRemind.notifyDataSetChanged();
        db.close();
        return list;
    }

//    public List<ModelFile> getAllFavouriteData() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select rowid,categoryname,category_iconimage,contentname from favourite", null);
//        List<ModelFile> list = new ArrayList<>();
//        AdapterFavourite adapterFavourite = new AdapterFavourite(context, list);
//
//        while (cursor.moveToNext()) {
//            int index1 = cursor.getColumnIndex("rowid");
//            int index2 = cursor.getColumnIndex("categoryname");
//            int index3 = cursor.getColumnIndex("category_iconimage");
//            int index4 = cursor.getColumnIndex("contentname");
//
//
//            String favrowid = cursor.getString(index1);
//            String favcatname = cursor.getString(index2);
//            String favcontantname = cursor.getString(index4);
//            String favimg = cursor.getString(index3);
//
//            ModelFile modelFile = new ModelFile(favrowid, favcatname, favcontantname, favimg);
//            modelFile.setFavrowid(favrowid);
//            modelFile.setFavimg(favimg);
//            modelFile.setFavcatname(favcatname);
//            modelFile.setFavcontantname(favcontantname);
//            list.add(modelFile);
//        }
//        adapterFavourite.notifyDataSetChanged();
//        db.close();
//        return list;
//    }

    public List<ModelFile> getAllReminderData2() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select rowid,title,days,time,hr,minut from reminder", null);
        List<ModelFile> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID);
            int index2 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME);
            int index3 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_DAY);
            int index4 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_TIME);
            int index5 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_HR);
            int index6 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_MINUT);


            String rowid = cursor.getString(index1);
            String txt_remidtitle = cursor.getString(index2);
            String txt_remindday = cursor.getString(index3);
            String txt_remindtime = cursor.getString(index4);
            String remind_hr = cursor.getString(index5);
            String remind_minut = cursor.getString(index6);
            ModelFile modelFile = new ModelFile(rowid, txt_remidtitle, txt_remindday, txt_remindtime, remind_hr, remind_minut);
            modelFile.setRowid(rowid);
            modelFile.setRemind_time(txt_remindtime);
            modelFile.setRemind_day(txt_remindday);
            modelFile.setRemind_hr(remind_hr);
            modelFile.setRemind_title(txt_remidtitle);
            modelFile.setRemind_minut(remind_minut);

            list.add(modelFile);
        }
        db.close();
        return list;
    }

    public Integer deleteReminder(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("reminder",
                "rowid = ? ",
                new String[]{Integer.toString(id)});
    }

//    public Integer delete_favourite(Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("favourite",
//                "contentId = ? ",
//                new String[]{Integer.toString(id)});
//    }
//
//    public Cursor getfavID(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery("select * from favourite where rowid=" + id + "", null);
//        return c;
//
//    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        db.close();
        return count;
    }

//    public long getFavouriteCount() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String CONTACTS_TABLE_NAME1 = "favourite";
//        long count = DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME1);
//        db.close();
//        return count;
//    }

}
