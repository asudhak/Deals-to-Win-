package edu.wlan.deals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	  private static final int DATABASE_VERSION = 1; 
	  private static final String DATABASE_NAME = "COUPONS"; 
	
	  private static final String TABLE_COUPON = "places";
	 
	  private static final String KEY_NAME = "name"; 
	   
	  
	  private static final String KEY_PREF = "preference";  
	  private static final String KEY_MILES = "miles";
	  
	  public DatabaseHelper(Context context){
		  super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	  }
	  @Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	      String CREATE_TABLE = "CREATE TABLE " + TABLE_COUPON + "(" + KEY_NAME + " TEXT PRIMARY KEY," + KEY_PREF +" TEXT,"+ KEY_MILES + " DOUBLE)";
	      
	      Log.i("Both table","created");
	      db.execSQL(CREATE_TABLE);
	     
	 }

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	    db.execSQL("DROP TABLE IF EXISTS " +TABLE_COUPON);
	 
	    onCreate(db);
	}
	
	public void insert(String a, String b, double c){
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values= new ContentValues();
		values.put(KEY_NAME,a);
		values.put(KEY_PREF, b);
		values.put(KEY_MILES,c);
		db.insert(TABLE_COUPON , null, values);
		Log.i("DB","INSERTED");
		db.close();
	}
	
	public void delete(){
		  SQLiteDatabase db = this.getWritableDatabase(); 
		  db.delete(TABLE_COUPON, null, null);
	  db.close();
	} 
	public ArrayList getCoupons() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT *" + " FROM " + TABLE_COUPON;
		ArrayList<Map<String, String>> result=new ArrayList<Map<String, String>>();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do {
				HashMap h=new HashMap();
				h.put("Name", cursor.getString(0));
				h.put("Type", cursor.getString(1));
				h.put("Dist", cursor.getString(2));
				result.add(h);
			}while(cursor.moveToNext());
		}
		cursor.close();
        db.close();
		return result;
	}
	public void deleteCoupon(String name,String pref,String mile){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COUPON, KEY_NAME + "=? AND "+ KEY_PREF + "=? AND " + KEY_MILES + "=?", new String[]{name,pref,mile});
	}
	
	
 }

