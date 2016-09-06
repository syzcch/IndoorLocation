package com.project.syz.indoorlocation.mainfunc;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.indoorlocation.R;
import com.project.syz.indoorlocation.bean.Location;
import com.project.syz.indoorlocation.database.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends ActionBarActivity {

    private volatile static boolean isStop = false;
    private static  DBHelper dbhelper;
    private static  Cursor cursor_loc;
    private static  String sql;
    private static List<Location> locList;
    private static float showX;
    private static float showY;
    private static int showFinger;
    private static TextView finger;
    private static TextView locX;
    private static TextView locY;

    private static final int MSG_SUCCESS = 0;//succeed
    private static final int MSG_FAILURE = 1;//failure

    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case MSG_SUCCESS:
                    Location xy = (Location) msg.obj;
                    finger.setText("fingerprint: "+String.valueOf(xy.getFinger()));
                    locX.setText("locX: "+String.valueOf(xy.getLocX()));
                    locY.setText("locY: "+String.valueOf(xy.getLocY()));
//                    Toast.makeText(getApplication(), getApplication().getString(R.string.get_pic_success), Toast.LENGTH_LONG).show();
                    break;

                case MSG_FAILURE:
                    Toast.makeText(getApplication(), "Async thread task is fail!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        finger = (TextView)findViewById(R.id.finger);
        locX = (TextView)findViewById(R.id.locX);
        locY = (TextView)findViewById(R.id.locY);

        dbhelper = DBHelper.getInstance();
//        sql = "select finger,locX,locY from location;" ;
        sql = "select * from location" ;
        cursor_loc = dbhelper.QueryBySql(sql);

        locList = new ArrayList<Location>();
        while(cursor_loc.moveToNext()) {
            Location loc = new Location();
            loc.setFinger(cursor_loc.getInt(0));
            loc.setLocX(cursor_loc.getFloat(1));
            loc.setLocY(cursor_loc.getFloat(2));
            locList.add(loc);
        }

        startExecutor();
    }

    private void startExecutor(){
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
 //       while(!isStop) {
            singleThreadExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        while(!isStop){
                            // computing fingerprint
                            showFinger = computeFinger();
                            // compute showX,showY
                            computeLocXY();

                            Location xy = new Location();
                            xy.setLocX(showX);
                            xy.setLocY(showY);
                            mHandler.obtainMessage(MSG_SUCCESS,xy).sendToTarget();

                            Thread.sleep(2000);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block

                        mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
                        e.printStackTrace();
                        return;
                    }
                }
            });
  //      }
    }

    private int computeFinger(){
        // you should return finger instead of 0
        return 0;
    }

    private void computeLocXY(){


        // finally set showX and show Y
        showX = (float)1.1;
        showY = (float)1.2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
