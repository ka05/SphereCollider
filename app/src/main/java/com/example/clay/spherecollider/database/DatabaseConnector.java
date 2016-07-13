// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.example.clay.spherecollider.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.clay.spherecollider.R;
import com.example.clay.spherecollider.view.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseConnector 
{
   // database name
   private static final String DATABASE_NAME = "SphereColliderLevels";
   private SQLiteDatabase database; // database object
   private DatabaseOpenHelper databaseOpenHelper; // database helper

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context)
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   } // end DatabaseConnector constructor

   // open the database connection
   public void open() throws SQLException
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   } // end method open

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } // end method close

   // inserts a new Level in the database
   public void insertLevel(String levelName, int levelBgImgsrc)
   {
      ContentValues newLevel = new ContentValues();
      newLevel.put("levelName", levelName);
      newLevel.put("levelBG", levelBgImgsrc);

      open(); // open the database
      database.insert("levels", null, newLevel);
      close(); // close the database
   } // end method insertLevel


   public void updateLevelCompleted(long levelId) {
       ContentValues args = new ContentValues();
       args.put("levelCompleted", "true");
       open();
       database.update("levels", args, "_id=?", new String[]{String.valueOf(levelId)});
       close();
   }

    //levelUnlocked
    public void updateLevelUnlocked(long levelId) {
        ContentValues args = new ContentValues();
        args.put("levelUnlocked", "true");
        open();
        database.update("levels", args, "_id=?", new String[]{ String.valueOf(levelId) } );
        close();
    }

   // return a Cursor with all contact information in the database
   public Cursor getAllLevels()
   {
      return database.query("levels", new String[] {"_id", "levelName", "levelUnlocked"},
         null, null, null, null, "levelName");
   } // end method getAllContacts


   // get a Cursor containing all information about the contact specified
   // by the given id
   public Cursor getOneLevel(long id)
   {
      return database.query(
         "levels", null, "_id=" + id, null, null, null, null);
   } // end method getOneLevel

   // delete the contact specified by the given String name
   public void deleteLevel(long id)
   {
      open(); // open the database
      database.delete("levels", "_id=" + id, null);
      close(); // close the database
   } // end method deleteContact

   public void insertMultiple(ArrayList<HashMap<String, Integer>> levelList){

       for(HashMap<String, Integer> levelData : levelList){
           String levelName = "";
           int levelBgImgsrc = 0;

           Iterator it = levelData.entrySet().iterator();
           while (it.hasNext()) {
               Map.Entry pair = (Map.Entry)it.next();
               System.out.println(pair.getKey() + " = " + pair.getValue());
               if(pair.getKey() == "levelName"){
                   levelName = pair.getValue().toString();
               }
               if(pair.getKey() == "levelBG"){
                   levelBgImgsrc = (int)pair.getValue();
               }
               it.remove(); // avoids a ConcurrentModificationException
           }
           insertLevel(levelName, levelBgImgsrc);
       }
   }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = databaseOpenHelper.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

   private class DatabaseOpenHelper extends SQLiteOpenHelper
   {
      // public constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version)
      {
         super(context, name, factory, version);
      } // end DatabaseOpenHelper constructor

      // creates the contacts table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db)
      {
        // query to create a new table named contacts
        String createLevelTableQuery = "CREATE TABLE levels(" +
            "_id integer primary key autoincrement, " +
            "levelName TEXT, " +
            "levelBG integer, " +
            "levelCompleted TEXT, " +
            "levelUnlocked TEXT, " +
            "ballColor TEXT, " +
            "inflaterColor TEXT, " +
            "reducerColor TEXT, " +
            "numReducers integer, " +
            "numInflaters integer, " +
            "numPoints integer, " +
            "maxPoints integer " +
        ");";

        db.execSQL(createLevelTableQuery); // execute the query
        insertLevels(db);

      } // end method onCreate

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion,
          int newVersion) 
      {
      } // end method onUpgrade

       public void insertLevels(SQLiteDatabase db){
           // ideally these would not be hardcoded
           ArrayList<Level> levels = new ArrayList<>();

           levels.add(new Level(
                   "Level 1", // name
                   "false", // completed
                   "true", // unlocked
                   R.drawable.level1, // background
                   "#ff7935", // ballColor
                   "#dd2ea8", // inflater
                   "#2dd2d7", // reducer
                   8, // num reducers
                   4, // numInflaters
                   20, // num starting points
                   100 // max points
           ));
           levels.add(new Level(
                   "Level 2", // name
                   "false", // completed
                   "false", // unlocked
                   R.drawable.level2, // background
                   "#ff7137", // ballColor
                   "#2ed7c5", // inflater
                   "#d0f034", // reducer
                   7, // num reducers
                   5, // numInflaters
                   20, // num starting points
                   200 // max points
           ));
           levels.add(new Level(
                   "Level 3", // name
                   "false", // completed
                   "false", // unlocked
                   R.drawable.level3, // background
                   "#50dc2f", // ballColor
                   "#f43445", // inflater
                   "#2ed7c5", // reducer
                   6, // num reducers
                   6, // numInflaters
                   20, // num starting points
                   300 // max points
           ));
           levels.add(new Level(
                   "Level 4", // name
                   "false", // completed
                   "false", // unlocked
                   R.drawable.level4, // background
                   "#964cd7", // ballColor
                   "#33d7c6", // inflater
                   "#fe753c", // reducer
                   5, // num reducers
                   7, // numInflaters
                   20, // num starting points
                   400 // max points
           ));
           levels.add(new Level(
                   "Level 5", // name
                   "false", // completed
                   "false", // unlocked
                   R.drawable.level5, // background
                   "#53dc34", // ballColor
                   "#db58bc", // inflater
                   "#57d8ca", // reducer
                   4, // num reducers
                   8, // numInflaters
                   20, // num starting points
                   500 // max points
           ));



           for(Level level : levels) {
               String createQuery = "INSERT INTO levels" +
                       "(levelName, levelBG, levelCompleted, levelUnlocked, ballColor, inflaterColor, reducerColor," +
                       "numReducers, numInflaters, numPoints, maxPoints) VALUES" +
                       " ('" +
                       level.getLevelName() + "', '" +
                       level.getLevelBG() + "', '" +
                       level.getLevelCompleted() + "', '" +
                       level.getLevelUnlocked() + "', '" +
                       level.getBallColor() + "', '" +
                       level.getInflaterColor() + "', '" +
                       level.getReducerColor() + "', '" +
                       level.getNumReducers() + "', '" +
                       level.getNumInflaters() + "', '" +
                       level.getNumPoints() + "', '" +
                       level.getMaxPoints() +
                       "');";

               db.execSQL(createQuery); // execute the query
           }
       }
   } // end class DatabaseOpenHelper
} // end class DatabaseConnector

