package net.JamCast.app.weatherapp.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter extends Activity {

	private static final String TAG = "DBAdapter"; //used for logging database version changes

	// Field Names:
	public static final String KEY_ROWID = "_id";
//	public static final String KEY_TASK = "task";
//	public static final String KEY_DATE = "date";

	//NEW DATA
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS= "address";
	public static final String KEY_CITY = "city";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_TEL = "tel";
	public static final String KEY_ROLE = "role";
	public static final String KEY_EMAIL = "email";

	
	//public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_TASK, KEY_DATE};

	//NEW
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME , KEY_ADDRESS, KEY_CITY, KEY_COUNTRY, KEY_TEL, KEY_ROLE,KEY_EMAIL};
	
	// Column Numbers for each Field Name:
	public static final int COL_ROWID = 0;
	//public static final int COL_TASK = 1;
	//public static final int COL_DATE = 2;

	public static final int COL_NAME = 1;
	public static final int COL_ADDRESS = 2;
	public static final int COL_CITY = 3;
	public static final int COL_COUNTRY = 4;
	public static final int COL_TEL = 5;
	public static final int COL_ROLE= 6;
	public static final int COL_EMAIL=7;

	// DataBase info:
	public static final String DATABASE_NAME = "dbToDo";
	public static final String DATABASE_TABLE = "mainToDo";
	public static final int DATABASE_VERSION = 2; // The version number must be incremented each time a change to DB structure occurs.
		
	//SQL statement to create database
	private static final String DATABASE_CREATE_SQL = 
			"CREATE TABLE " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_NAME + " TEXT, "
			+ KEY_ADDRESS+ " TEXT, "
			+ KEY_CITY + " TEXT, "
					+ KEY_COUNTRY+ " TEXT, "
					+ KEY_TEL + " TEXT, "
					+ KEY_ROLE + " TEXT, "
					+ KEY_EMAIL+ " TEXT "
			+ ");";
	
	private final Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;


	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	public SQLiteDatabase getWritableDatabase() {
		db = myDBHelper.getWritableDatabase();

		return db;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to be inserted into the database.
	public long insertRow(String name, String address, String city, String country, String tel, String role, String email) {
		ContentValues initialValues = new ContentValues();
		//initialValues.put(KEY_TASK, task);
		//initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_ADDRESS, address);
		initialValues.put(KEY_CITY, city);
		initialValues.put(KEY_COUNTRY, country);
		initialValues.put(KEY_TEL, tel);
		initialValues.put(KEY_ROLE, role);
		initialValues.put(KEY_EMAIL, email);

		// Insert the data into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}

	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}


	public String databaseToString() {
		String dbString = "";
		SQLiteDatabase db = getWritableDatabase();
		String query = "SELECT * FROM " + DATABASE_TABLE+ " WHERE 1";
		//Curson point to a location in your results
		Cursor c = db.rawQuery(query, null);
		//Move to first row in results
		c.moveToFirst();
		while (!c.isAfterLast()) {
			if (c.getString(c.getColumnIndex("email")) != null) {
				dbString += c.getString(c.getColumnIndex("email"))+",";
			//	dbString += "\n";
			}
			c.moveToNext();
		}
		db.close();
		return dbString;
	}



	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	/*
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String task, String date) {
		String where = KEY_ROWID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TASK, task);
		newValues.put(KEY_DATE, date);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
*/
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}


}

