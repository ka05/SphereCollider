package com.example.clay.spherecollider.view.level;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clay.spherecollider.view.game.SphereCollider;
import com.example.clay.spherecollider.R;
import com.example.clay.spherecollider.database.DatabaseConnector;
import com.example.clay.spherecollider.view.game.management.GameMediator;

/**
 * Created by Clay on 4/11/2015.
 */
public class LevelView extends Activity {

    // key-value pair passed values between activities
    public static final String ROW_ID = "row_id";
    public static long levelRow;
    public static boolean levelSelected;

    public static GameMediator gameMediator;

    // adapter for populating the ListView
    private CursorAdapter levelAdapter;
    private ImageView lvlImageView;
    private ListView levelListView;
    private TextView txtViewLevelName;
    private Button btnStartLevel;
    private LinearLayout levelLegendContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        gameMediator = GameMediator.getInstance();
        gameMediator.setContext(getApplicationContext());

        levelSelected = false;

        levelLegendContainerView = (LinearLayout)findViewById(R.id.levelLegendContainerView);
        txtViewLevelName = (TextView)findViewById(R.id.txtViewLevelName);
        levelListView = (ListView)findViewById(R.id.listViewLevels);
        levelListView.setOnItemClickListener(viewLevelListener);

        lvlImageView = (ImageView)findViewById(R.id.levelImage);

        btnStartLevel = (Button)findViewById(R.id.btnStartLevel);
        btnStartLevel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (levelSelected == true) {
                    // create and Intent to launch the ViewContact Activity
                    Intent startLevelView = new Intent(LevelView.this, SphereCollider.class);

                    //pass the selected contacts row ID as extra with the Intent
                    startLevelView.putExtra(ROW_ID, levelRow);
                    startActivity(startLevelView);
                } else {
                    Toast.makeText(LevelView.this, "Please select a level first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((Button)findViewById(R.id.btnHome)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent home = new Intent(LevelView.this, com.example.clay.spherecollider.SphereCollider.class);
                startActivity(home);
            }
        });


        // map each contacts name to a TextView in the ListView layout
        String[] from = new String[]{"levelName", "levelCompleted"};
        int[] to = new int[]{R.id.txtLevelName, R.id.txtLevelCompleted};
        levelAdapter = new SimpleCursorAdapter(
                LevelView.this, R.layout.level_table_row, null, from, to, 0
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.txtLevelName);
                if( ((TextView)view.findViewById(R.id.txtLevelCompleted)).getText().equals("false") ) {

                    if(((TextView)view.findViewById(R.id.txtLevelName)).getText().equals("Level 1")){
                        text.setTextColor(Color.WHITE);
                        view.setEnabled(true);
                    }else{
                        text.setTextColor(Color.GRAY);
                        view.setEnabled(false);
                    }
                }else{
                    text.setTextColor(Color.WHITE);
                    view.setEnabled(true);
                }


                return view;
            }
        }; // 0 = start at first row

        // set adapter for listView
        levelListView.setAdapter(levelAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //create a new GetContactTask and execute
        new GetLevelsTask().execute((Object[])null);
        levelSelected = false;
        // need to call draw levelView Legend - set all to white as default
        setLevelLegendView("#FFFFFF", "#FFFFFF", "#FFFFFF");
//        ((ImageView)findViewById(R.id.levelImage)).setImageResource(R.drawable.no_level); // old way using image
    }

    @Override
    protected void onStop(){
        Cursor cursor = levelAdapter.getCursor(); // get cursor

        if(cursor != null){
            //we have a cursor
            //so lets close - deactivate it
            cursor.close();
        }
        levelAdapter.changeCursor(null); // adapter has no cursor
        super.onStop(); //needs to be last because other code must execute first
    }


    // pass in as "0xFFFFFF" format
    // http://www.nthelp.com/colorcodes.htm
    private void setLevelLegendView(String ballColor, String deflaterColor, String inflaterColor){
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;

        Drawable ballDrawable = levelLegendContainerView.findViewById(R.id.ball).getBackground();
        Drawable deflaterDrawable = levelLegendContainerView.findViewById(R.id.deflater).getBackground();
        Drawable inflaterDrawable = levelLegendContainerView.findViewById(R.id.inflater).getBackground();

        ballDrawable.setColorFilter(Color.parseColor(ballColor), mode);
        deflaterDrawable.setColorFilter(Color.parseColor(deflaterColor), mode);
        inflaterDrawable.setColorFilter(Color.parseColor(inflaterColor), mode);
    }


    //perform the database query outside the GUI Thread
    private class GetLevelsTask extends AsyncTask<Object, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(LevelView.this);

        //perform the database access
        @Override // from AsyncTask Class
        protected Cursor doInBackground(Object... params){
            databaseConnector.open();

            //get cursor containing all contacts
            return databaseConnector.getAllLevels();
        }

        // use the cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result){
            levelAdapter.changeCursor(result); //set the adapters cursor
            databaseConnector.close(); // close connection
        }
    }

    // performs database query outside GUI thread
    private class LoadLevelTask extends AsyncTask<Long, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(LevelView.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params)
        {
            databaseConnector.open();

            // get a cursor containing all data on given entry
            return databaseConnector.getOneLevel(params[0]);
        } // end method doInBackground

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result)
        {
            super.onPostExecute(result);

            result.moveToFirst(); // move to the first item

            // get the column index for each data item
            gameMediator.setLevelId(result.getLong(result.getColumnIndex("_id")));
            gameMediator.setLevelName( result.getString( result.getColumnIndex("levelName") ) );
            gameMediator.setLevelBgImgSrc(result.getInt(result.getColumnIndex("levelBG")));
            gameMediator.setLevelCompleted( result.getString( result.getColumnIndex("levelCompleted") ) );

            gameMediator.setBallColor(result.getString(result.getColumnIndex("ballColor")));
            gameMediator.setInflaterColor(result.getString(result.getColumnIndex("inflaterColor")));
            gameMediator.setReducerColor(result.getString(result.getColumnIndex("reducerColor")));
            gameMediator.setNumInflaters(result.getInt(result.getColumnIndex("numInflaters")));
            gameMediator.setNumReducers(result.getInt(result.getColumnIndex("numReducers")));
            gameMediator.setNumPoints(result.getInt(result.getColumnIndex("numPoints")));
            gameMediator.setMaxPoints(result.getInt(result.getColumnIndex("maxPoints")));


            showLevelSelection();
            result.close(); // close the result cursor
            databaseConnector.close(); // close database connection
        } // end method onPostExecute
    }

    OnItemClickListener viewLevelListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // get gameMediatorData
            if(view.isEnabled()){
                new LoadLevelTask().execute(id);
                levelRow = id;
                levelSelected = true;
            } else {
                Toast.makeText(LevelView.this, "Level" + id + " is locked.", Toast.LENGTH_SHORT).show();
            }
        }
    }; //end OnItemClick


    public void showLevelSelection(){
        // parse it aas a Level
//        int resId = this.getResources().getIdentifier("drawable/" + gameMediator.getImgSrc(), "drawable", this.getPackageName());
//        lvlImageView.setImageResource(resId);
//        lvlImageView.setContentDescription(gameMediator.getLevelName());
        //levelLegendContainerView.setBackground(getDrawable(this.getResources().getIdentifier("drawable/" + gameMediator.getLevelBgImgsrc(), "drawable", this.getPackageName() ) ) );
        txtViewLevelName.setText(gameMediator.getLevelName());
        setLevelLegendView(gameMediator.getBallColor(), gameMediator.getInflaterColor(), gameMediator.getReducerColor() );
    }

}
