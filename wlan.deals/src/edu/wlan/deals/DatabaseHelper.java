package edu.wlan.deals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	  private static final int DATABASE_VERSION = 1; 
	  private static final String DATABASE_NAME = "COUPONS_DEALS"; 
	
	  private static final String TABLE_COUPON = "Places";
	 
	  private static final String KEY_NAME = "name"; 
	   
	  
	  private static final String KEY_PREF = "preference";  
	  private static final String KEY_MILES = "miles";
	  private static final String KEY_DESC = "desc";
	  private static final String KEY_IMG = "image";
	  
	  public DatabaseHelper(Context context){
		  super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	  }
	  @Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	      String CREATE_TABLE = "CREATE TABLE " + TABLE_COUPON + "(" + KEY_NAME + " TEXT PRIMARY KEY," + KEY_PREF +" TEXT,"+ KEY_MILES + " DOUBLE," +KEY_DESC + " TEXT,"+ KEY_IMG + " BLOB)";
	      
	      Log.i("Both table","created");
	      db.execSQL(CREATE_TABLE);
	     
	 }

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	    db.execSQL("DROP TABLE IF EXISTS " +TABLE_COUPON);
	 
	    onCreate(db);
	}
	
	public void insert(String a, String b, double c,String desc, Bitmap img){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, out);
        
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values= new ContentValues();
		values.put(KEY_NAME,a);
		values.put(KEY_PREF, b);
		values.put(KEY_MILES,c);
		values.put(KEY_DESC,desc);
		values.put(KEY_IMG,out.toByteArray());
		
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
				h.put("Desc", cursor.getString(3));
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
		db.close();
	}
	public byte[] getImage(String name,String pref,String mile){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_COUPON, new String[]{KEY_IMG}, KEY_NAME + "=? AND "+ KEY_PREF + "=? AND " + KEY_MILES + "=?", new String[]{name,pref,mile},null, null, null);
		
			cursor.moveToFirst();
			byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_IMG));
			cursor.close();
		
		db.close();
		return blob;
	}
	
 }

