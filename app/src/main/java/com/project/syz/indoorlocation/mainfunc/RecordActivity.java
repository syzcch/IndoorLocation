package com.project.syz.indoorlocation.mainfunc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.indoorlocation.MainActivity;
import com.project.syz.indoorlocation.R;
import com.project.syz.indoorlocation.bean.Wifi;
import com.project.syz.indoorlocation.database.DBHelper;

import java.io.File;
import java.sql.Date;

public class RecordActivity extends ActionBarActivity {

    private Wifi wifi;
    private Wifi wifi1;

    private static File dir,file;
//    private static final String DATABASE_NAME = "wifi.db3";
    private static DBHelper dbhelper;

    private EditText locX,locY;
    private TextView showDetail;

    private float xValue,yValue;
    private int finger;

    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        wifi = (Wifi)getIntent().getSerializableExtra(MainActivity.RECORD_KEY);
        wifi1 = Wifi.getInstance();

        locX = (EditText)findViewById(R.id.locX);
        locY = (EditText)findViewById(R.id.locY);
        showDetail = (TextView)findViewById(R.id.showDetail);

//        loadDatabase();
        dbhelper = DBHelper.getInstance();
        setLocXFilter();
        setLocYFilter();
    }

    private void setLocXFilter(){
        locX.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;
            /*
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            // TODO Auto-generated method stub
                        }
            */
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        locX.setText(s);
                        locX.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    locX.setText(s);
                    locX.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        locX.setText(s.subSequence(0, 1));
                        locX.setSelection(1);
                        return;
                    }
                }
            }
        });
    }

    private void setLocYFilter(){
        locY.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;
            /*
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            // TODO Auto-generated method stub
                        }
            */
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        locY.setText(s);
                        locY.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    locY.setText(s);
                    locY.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        locY.setText(s.subSequence(0, 1));
                        locY.setSelection(1);
                        return;
                    }
                }
            }
        });
    }
/*
    private void loadDatabase(){
        //       dbhelper = new DBHelper(this,DATABASE_NAME);
        dbhelper = DBHelper.getInstance();
        dir = new File("data/data/" + getPackageName() + "/databases");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        file = new File(dir, DATABASE_NAME);

        try {
            if (!file.exists()) {
                dbhelper.CreateDB(file);
                dbhelper.CreateTable();
            }
            else{
                dbhelper.CreateDB(file);
            }


        } catch (Exception e) {
            Log.e("Database", "Creating database error!");
            e.printStackTrace();
        }
    }
*/

    private int computeFinger(){

        // you should return finger instead of 0
        return 0;
    }

    public void reset(View view){

        DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(RecordActivity.this, "Do not Delete it!",
                                Toast.LENGTH_LONG).show();
                        break;
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(RecordActivity.this, "Deleting Now!",
                                Toast.LENGTH_LONG).show();
                        dbhelper.ResetTable();
                        break;
                }
            }
        };

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.btn_star)
                .setTitle("ResetTable ?")
                .setMessage("Do you want to delete all data in location table?")
                .setPositiveButton("Yes", onclick)
                .setNegativeButton("No",  onclick).create();
        dialog.show();
    }

    public void submit(View view) {

        // if finger computing is too slow, we will move it into a single thread.
        finger = computeFinger();

        // just for testing
        if(0 == finger){
            finger = 111111;
        }

        if(locX.getText().toString().isEmpty()){
            Toast.makeText(this, "Please input location X to record info", Toast.LENGTH_LONG).show();
            return;
        }

        if(locY.getText().toString().isEmpty()){
            Toast.makeText(this, "Please input location Y to record info", Toast.LENGTH_LONG).show();
            return;
        }

        showDetail.setText("Fingerprint is:" + String.valueOf(finger));

        xValue = Float.parseFloat(locX.getText().toString());
        yValue = Float.parseFloat(locY.getText().toString());

        date = new Date(System.currentTimeMillis());

        dbhelper.ReplaceInsertLoc(finger, xValue, yValue, date);

        locX.setText("");
        locY.setText("");
//        showDetail.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
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
