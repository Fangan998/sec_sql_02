package com.example.sec_sql_02;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lv01;
    EditText ed_num;
    Button btn_sec,btn_sec_all;
    Cursor cursor;
    private SQLiteDatabase  db= null;
    private final static String CREATE_TABLE = "CREATE TABLE table01(_id INTEGER PRIMARY KEY,pname TEXT,price INTERGER)";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_num = findViewById(R.id.ed_num_text);
        lv01 = findViewById(R.id.list_a01);
        btn_sec = findViewById(R.id.btn_sec);
        btn_sec_all = findViewById(R.id.sec_all_btn);

        db=openOrCreateDatabase("db1.db", MODE_PRIVATE, null);


        try {

            db.execSQL(CREATE_TABLE);
            db.execSQL("INSERT INTO table01 (name,price) values ('香蕉',30)"); // 新增資料
            db.execSQL("INSERT INTO table01 (name,price) values ('非洲柳丁',87)");
            db.execSQL("INSERT INTO table01 (name,price) values ('西瓜',120)");
            db.execSQL("INSERT INTO table01 (name,price) values ('梨子',250)");
            db.execSQL("INSERT INTO table01 (name,price) values ('水蜜桃',280)");}
        catch (Exception e){

        }
        cursor=getAll();
        UpdateAdapter(cursor);

        btn_sec.setOnClickListener(clickListener);
        btn_sec_all.setOnClickListener(clickListener);
        lv01.setOnItemClickListener (listviewlistener);
    }

    private ListView.OnItemClickListener listviewlistener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            Cursor c=get(id);
            String s= "id=" + id + "\r\n" + "pname=" + c.getString(1) + "\r\n" + "price=" + c.getInt(2);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }
    };

    /*
    private void UpdateAdapter(Cursor cursor) {
        if (cursor != null && cursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2, // 包含兩個資料項
                    cursor, // 資料庫的 Cursors 物件
                    new String[] {"pname", "price" }, // pname、price 欄位
                    new int[] { android.R.id.text1, android.R.id.text2 }, // 與 pname、price對應的元件
                    0); // adapter 行為最佳化
            lv01.setAdapter(adapter); // 將adapter增加到listview01中
        }
    }*/

    private void UpdateAdapter(Cursor cursor) {
        if (cursor != null && cursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.mylayout, // 包含兩個資料項
                    cursor, // 資料庫的 Cursors 物件
                    new String[] {"_id","name", "price" }, // pname、price 欄位
                    new int[] { R.id.id_txtshow,R.id.id_name_txtshow, R.id.id_price_txtshow}, // 與 pname、price對應的元件
                    0); // adapter 行為最佳化
            lv01.setAdapter(adapter); // 將adapter增加到listview01中
        }
    }

    private Button.OnClickListener clickListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            try{
                switch (v.getId()){
                    case R.id.btn_sec:{     // 查詢單筆
                        long id = Integer.parseInt(ed_num.getText().toString());
                        cursor=get(id);
                        UpdateAdapter(cursor); // 載入資料表至 ListView 中
                        break;
                    }case R.id.sec_all_btn:{    // 查詢全部
                        cursor=getAll();       // 查詢所有資料
                        UpdateAdapter(cursor); // 載入資料表至 ListView 中
                        break;
                    }
                }
            }catch (Exception err){
                Toast.makeText(getApplicationContext(), "查無此資料!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /*
    private Cursor get(long rowid) throws SQLException
    {
        Cursor cursor= db.rawQuery("SELECT _id, _id||'.'||name pname, price FROM table01 WHERE _id="+rowid,null);
        if (cursor.getCount()>0)
            cursor.moveToFirst();
        else
            Toast.makeText(getApplicationContext(), "查無此筆資料!", Toast.LENGTH_SHORT).show();
        return cursor; // 傳回_id、pname、price欄位

    }*/

    private Cursor get(long rowid) throws SQLException
    {
        Cursor cursor= db.rawQuery("SELECT _id, name , price FROM table01 WHERE _id="+rowid,null);
        if (cursor.getCount()>0)
            cursor.moveToFirst();
        else
            Toast.makeText(getApplicationContext(), "查無此筆資料!", Toast.LENGTH_SHORT).show();
        return cursor; // 傳回_id、name、price欄位

    }


    protected void onDestroy(){
        super.onDestroy();
        db.close(); // 關閉資料庫
    }

    /*
    private Cursor getAll() {
        Cursor cursor= db.rawQuery("SELECT _id, _id||'.'||name pname, price FROM table01",null);
        return cursor; // 傳回_id、pname、price欄位
    }
    */

    private Cursor getAll() {
        Cursor cursor= db.rawQuery(
                "SELECT _id, name, price FROM table01",null);
        return cursor; // 傳回_id、name、price欄位
    }
}