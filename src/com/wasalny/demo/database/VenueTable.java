package com.wasalny.demo.database;
 
import java.util.ArrayList;
import java.util.List;

import com.wasalny.demo.model.VenueModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class VenueTable extends AbstractTable{
private static final String TABLE_NAME = "venues";
	
	private static VenueTable instance;
	private String[] projection = {Fields._ID,Fields.VENUEID,
		Fields.NAME,Fields.LAT,Fields.LNG,Fields.IMG_URL,Fields.CHECKIN_COUNT
		,Fields.USERS_COUNT,Fields.TIP_COUNT
	};
	final static class Fields implements BaseColumns {
		public static final String NAME = "name";
		public static final String VENUEID = "venue_ID";
		public static final String LAT = "lat";
		public static final String LNG = "lng";
		public static final String IMG_URL = "image_url";
		public static final String CHECKIN_COUNT = "checkin_count";
		public static final String USERS_COUNT = "users_count";
		public static final String TIP_COUNT = "tip_count";
	}
	
	
	private VenueTable()
	{
		DatabaseManager.getInstance().addTable(this);
	}
	
	public static VenueTable getInstance() {
		if (instance == null)
			instance = new VenueTable();
		return instance;
	}
	@Override
	protected void create(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String createSql = "CREATE TABLE " + getTableName() +
				" (" + Fields._ID + NUMBER_TYPE +ORDER_ASCENDING+ COMMA_SEP +
				Fields.NAME + TEXT_TYPE + COMMA_SEP +
				Fields.LAT + TEXT_TYPE + COMMA_SEP +
				Fields.CHECKIN_COUNT + NUMBER_TYPE + COMMA_SEP +
				Fields.USERS_COUNT + NUMBER_TYPE +COMMA_SEP +
				Fields.LNG + TEXT_TYPE+ COMMA_SEP +
				Fields.IMG_URL + TEXT_TYPE+ COMMA_SEP +
				Fields.VENUEID + TEXT_TYPE+ COMMA_SEP +
				Fields.TIP_COUNT + NUMBER_TYPE +")";
		db.execSQL(createSql);
	}

	@Override
	protected void upgrade(SQLiteDatabase db, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	private ContentValues getContentValues(VenueModel item) {
		ContentValues values = new ContentValues();
		values.put(Fields.VENUEID, item.getId());
		values.put(Fields.NAME, item.getName());
		values.put(Fields.LAT, item.getLat());
		values.put(Fields.LNG, item.getLng());
		values.put(Fields.CHECKIN_COUNT, item.getCheckIn_count() );
		values.put(Fields.IMG_URL, item.getImg_url());
		values.put(Fields.TIP_COUNT, item.getTipCount());
		values.put(Fields.USERS_COUNT, item.getUsersCounts ());
	 
		
		return values;
	}
	
	public List<VenueModel> GetVenueList()
	{
		Cursor cursor=DatabaseManager.getInstance().getWritableDatabase().query(getTableName(), getProjection(), null, null, null, null, null);
		List<VenueModel>venueList=null;
		if (cursor.moveToFirst()) {
			venueList = new ArrayList<VenueModel>(cursor.getCount());
			do {
				venueList.add(new VenueModel(cursor.getString(cursor.getColumnIndex(Fields.VENUEID)), cursor.getString(cursor.getColumnIndex(Fields.NAME)),
						cursor.getString(cursor.getColumnIndex(Fields.LAT)), cursor.getString(cursor.getColumnIndex(Fields.LNG)), 
						cursor.getString(cursor.getColumnIndex(Fields.IMG_URL)), cursor.getInt(cursor.getColumnIndex(Fields.CHECKIN_COUNT)),
						cursor.getInt(cursor.getColumnIndex(Fields.USERS_COUNT)),
						cursor.getInt(cursor.getColumnIndex(Fields.CHECKIN_COUNT))  
						   
					 ));
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		
		return venueList;
	}
	
	public VenueModel GetvenueByID(String venue_id)
	{
		 VenueModel nModel=null;
			Cursor cursor=DatabaseManager.getInstance().getWritableDatabase().query(getTableName(), getProjection(), "venue_ID="+"'"+venue_id+"'", null, null, null, null);
        if(cursor.moveToFirst())
        {
     	   nModel=new VenueModel(cursor.getString(cursor.getColumnIndex(Fields.VENUEID)), cursor.getString(cursor.getColumnIndex(Fields.NAME)),
					cursor.getString(cursor.getColumnIndex(Fields.LAT)), cursor.getString(cursor.getColumnIndex(Fields.LNG)), 
					cursor.getString(cursor.getColumnIndex(Fields.IMG_URL)), cursor.getInt(cursor.getColumnIndex(Fields.CHECKIN_COUNT)),
					cursor.getInt(cursor.getColumnIndex(Fields.USERS_COUNT)),
					cursor.getInt(cursor.getColumnIndex(Fields.CHECKIN_COUNT)));
        }
		 return nModel;
	}
	
	public void insertVenue(VenueModel item) {
		DatabaseManager.getInstance().getWritableDatabase()
			.insert(getTableName(), null, getContentValues(item));
		//MemberNewsTable.getInstance().insertNewsItem( item);
	}
	
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	protected String[] getProjection() {
		// TODO Auto-generated method stub
		return projection;
	}

}
