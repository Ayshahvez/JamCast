package net.JamCast.app.weatherapp.data;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import net.JamCast.app.weatherapp.R;

import java.util.regex.Pattern;

import static net.JamCast.app.weatherapp.R.*;

public class databaseActivity extends AppCompatActivity {

 //   Time today = new Time(Time.getCurrentTimezone());
    DBAdapter myDb;
  //  EditText etTasks;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.content_database);


        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       //   setSupportActionBar(toolbar);


          ///////////////
          openDB();
        //  myDb.insertRow("John", "Cherry Garden", "Kingston", "Jamaica","4326447","manager","ve.z");
          populateListView();
          listViewItemClick();
       //   ListViewItemLongClick();

     //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          }



     /* //Delete items
              public void deleteButtonClicked(View view){
           String inputText = buckysInput.getText().toString();


//                  }
                  try{
                      Long id = Long.parseLong(inputText);
                      myDb.deleteRow(id);
                      populateListView();
                  } catch (NumberFormatException e) {
                      // not an integer!
                  }


          }*/

    //Delete items
    public void ClearAll(View view){

         myDb.deleteAll();
        populateListView();
    }


/*
      //Print the database
              public void printDatabase(){
            buckysInput.setText("");
          }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }
/*
    public void addButtonClicked(View v){
        today.setToNow();
        String timestamp = today.format("%Y-%m-%d %H:%M:%S");
        if(!TextUtils.isEmpty(etTasks.getText().toString())){
            myDb.insertRow(etTasks.getText().toString(),timestamp);

        }
        populateListView();
    }
*/
    public void populateListView()
    {
        Cursor cursor = myDb.getAllRows();
       // String[] fromFileNames = new String[] {DBAdapter.KEY_ROWID,DBAdapter.KEY_TASK};
        String[] fromFileNames = new String[] {DBAdapter.KEY_ROWID,DBAdapter.KEY_NAME, DBAdapter.KEY_CITY};
        int[] toViewIDs = new int[] {R.id.Id,R.id.name,R.id.city};
        SimpleCursorAdapter simpleCursorAdapter;
        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.logs_item,cursor,fromFileNames,toViewIDs,0);
        ListView myList = (ListView) findViewById(R.id.listView);
        myList.setAdapter(simpleCursorAdapter);
    }
/*
    private void updateTask(long id){
        Cursor cursor = myDb.getRow(id);
        if (cursor.moveToFirst()) {
            String task = etTasks.getText().toString();
            if (!task.isEmpty() && task != null) {
                today.setToNow();
                String date = today.format("%Y-%m-%d %H:%M:%S");
                myDb.updateRow(id, task, date);
            }
        }
        cursor.close();
    }*/

    private void listViewItemClick(){
        ListView myList = (ListView)findViewById(id.listView);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   updateTask(id);
                //  populateListView();
                displayToast(id);
            }
        });
    }

 /*   private void ListViewItemLongClick(){
        ListView myList = (ListView) findViewById(id.listView);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                updateTask(id);
                populateListView();
                displayToast(id);
                return false;
            }
        });
    }*/

    private void displayToast(long id){
        Cursor cursor =  myDb.getRow(id);
        if(cursor.moveToFirst()){
            long idDB = cursor.getLong(DBAdapter.COL_ROWID);
            String name = cursor.getString(DBAdapter.COL_NAME);
            String address = cursor.getString(DBAdapter.COL_ADDRESS);
            String city = cursor.getString(DBAdapter.COL_CITY);
            String country = cursor.getString(DBAdapter.COL_COUNTRY);
            String tel = cursor.getString(DBAdapter.COL_TEL);
            String role = cursor.getString(DBAdapter.COL_ROLE);
            String email = cursor.getString(DBAdapter.COL_EMAIL);

            String message = "ID: " +idDB+ "\n"+ "Name: "+name+ "\n"+"Address: " +address+"\n"+"City: "+city
                    +"\n"+"Country: "+country+"\n"+"Tel: "+tel+"\n"+"Role: "+role+"\n"+"Email: "+email+"\n";
            Toast.makeText(databaseActivity.this,message,Toast.LENGTH_LONG).show();
        }
    cursor.close();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        closeDB();
    }

    private void closeDB()
    {
        myDb.close();
    }



}
