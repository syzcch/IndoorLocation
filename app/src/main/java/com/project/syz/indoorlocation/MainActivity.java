package com.project.syz.indoorlocation;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.indoorlocation.bean.Wifi;
import com.project.syz.indoorlocation.bean.WifiItem;
import com.project.syz.indoorlocation.database.DBHelper;
import com.project.syz.indoorlocation.mainfunc.RecordActivity;
import com.project.syz.indoorlocation.mainfunc.SearchActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView lv;
    private MyListAdapter adapter;
    private List<ScanResult> sclist = new ArrayList<ScanResult>();
    private List<WifiItem> wifi = new ArrayList<WifiItem>();
    private Context mContext;
    private Wifi wifiSingle;
    public  final static String RECORD_KEY = "RECORDWIFI";

    private static File dir,file;

    private static final String DATABASE_NAME = "wifi.db3";
    private static DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDatabase();
        setGridView();
    }

    private void setGridView() {
        mContext = getApplicationContext();
        lv = (ListView)findViewById(R.id.lvwifi);

        initWifiData();
        adapter = new MyListAdapter(wifi);
        lv.setAdapter(adapter);
    }

    //Finding more info about wifi   https://developer.android.com/reference/android/net/wifi/ScanResult.html
    private void initWifiData(){
        WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
        if(!wifi_service.isWifiEnabled()){
            Toast.makeText(this, "Please enable your WIFI for indoor location", Toast.LENGTH_LONG).show();
            finish();
            System.exit(0);
 //           wifi_service.setWifiEnabled(true);
        }
        sclist = wifi_service.getScanResults();
        Iterator it = sclist.iterator();
        while(it.hasNext()){
            ScanResult tmpsr = (ScanResult)it.next();
            WifiItem wi = new WifiItem(tmpsr.SSID,tmpsr.level);
            wifi.add(wi);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            /*
            if (!file.exists()) {
                dbhelper.CreateDB();
                dbhelper.CreateTable();
            }
            */
        } catch (Exception e) {
            Log.e("Database", "Creating database error!");
            e.printStackTrace();
        }
    }

    public void use(View view){

        wifiSingle = Wifi.getInstance();
        for(int i=0;i<adapter.mChecked.size();i++){
            if(adapter.mChecked.get(i)){
                wifiSingle.add(wifi.get(i));
            }
        }
        if(wifiSingle.get().size() == 0){
            Toast.makeText(this, "Please select some Wifi for indoor location", Toast.LENGTH_LONG).show();
            return;
        }
//        wifiSingle.setWifiList(wifi);
    }

    public void recordwifi(View view){

        if(wifiSingle.get().size() == 0){
            Toast.makeText(this, "Please select some Wifi for indoor location", Toast.LENGTH_LONG).show();
            return;
        }
        Intent mIntent = new Intent(this,RecordActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(RECORD_KEY,wifiSingle);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    public void search(View view){

       Intent mIntent = new Intent(this,SearchActivity.class);
//        Bundle mBundle = new Bundle();
 //       mBundle.putSerializable(RECORD_KEY,wifiSingle);
//        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    class MyListAdapter extends BaseAdapter {
        List<Boolean> mChecked;
        List<WifiItem> listwifi;
        HashMap<Integer,View> map = new HashMap<Integer,View>();

        public MyListAdapter(List<WifiItem> list){
            listwifi = new ArrayList<WifiItem>();
            listwifi = list;

            mChecked = new ArrayList<Boolean>();
            for(int i=0;i<list.size();i++){
                mChecked.add(false);
            }
        }

        @Override
        public int getCount() {
            return listwifi.size();
        }

        @Override
        public Object getItem(int position) {
            return listwifi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;

            if (map.get(position) == null) {
                Log.e("MainActivity","position1 = "+position);

                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.listitem, null);
//                view = lv;
                holder = new ViewHolder();
                holder.selected = (CheckBox)view.findViewById(R.id.list_select);
                holder.name = (TextView)view.findViewById(R.id.list_name);
                holder.level = (TextView)view.findViewById(R.id.list_level);
                final int p = position;
                map.put(position, view);
                holder.selected.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox)v;
                        mChecked.set(p, cb.isChecked());
                    }
                });
                view.setTag(holder);
            }else{
                Log.e("MainActivity", "position2 = " + position);
                view = map.get(position);
                holder = (ViewHolder)view.getTag();
            }

            holder.selected.setChecked(mChecked.get(position));
            holder.name.setText(listwifi.get(position).getSSID());
            holder.level.setText(String.valueOf(listwifi.get(position).getLevel()));

            return view;
        }

    }

    static class ViewHolder{
        CheckBox selected;
        TextView name;
        TextView level;
    }
}
