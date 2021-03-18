package com.hog2020.ex42database;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etname;
    EditText etage;
    EditText etemail;

    //SQLiteDatabase 참조변수
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etname=findViewById(R.id.et_name);
        etage=findViewById(R.id.et_age);
        etemail=findViewById(R.id.et_email);

        //test.db라는 이름으로 데이터베이스 파일 열기 또는 없으면 생성
        //액티비티클래스에 이 기능이 메소드로 존재함
        //이 메소드를 실행하면 test.db를 제어할 수 있는
        //능력을 가진 객체(SQLiteDatabase)를 리턴해줌
        db= openOrCreateDatabase("test.db",MODE_PRIVATE,null);

        //만들어진 DB 파일에 테이블(표)를 생성 하는 작업 수행
        //SQL 언어 를 이용해서 원하는 명령을 Database 에 실행
        db.execSQL("CREATE TABLE IF NOT EXISTS member(num INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20),age INTEGER,email TEXT)");

    }

    public void clickinsert(View view) {
        String name= etname.getText().toString();
        int age= Integer.parseInt((etage.getText().toString()));
        String email= etemail.getText().toString();

        db.execSQL("INSERT INTO member(name,age,email) VALUES('"+name+"','"+age+"','"+email+"')");

        etname.setText("");
        etage.setText("");
        etemail.setText("");
    }

    public void clickselectall(View view) {

       Cursor cursor =db.rawQuery("SELECT * FROM member",null);
       //리턴객체 Cursor : select 요청의 결과표(ResultSet) 를 가리키는 객체

        if(cursor == null)return;

        StringBuffer stringBuffer=new StringBuffer();

        int cnt=cursor.getCount();//총 레코드 수(데이터의 행(row)를 알 수 있음)

        cursor.moveToFirst();//첫 레코드로 이동
        for(int i=0;i<cnt;i++){
            int num=cursor.getInt(0);
            String name= cursor.getString(1);
            int age=cursor.getInt(2);
            String email= cursor.getString(3);

            stringBuffer.append(num+" "+name+" "+age+" "+email+"\n");

            cursor.moveToNext();//다음 레코드로 이동
        }

        Toast.makeText(this, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
    }

    public void clickselectname(View view) {
        //이름으로 검색
        String name =etname.getText().toString();
        Cursor cursor=db.rawQuery("SELECT name,age FROM member WHERE name=?",new String[]{name});
        if(cursor == null) return;

        StringBuffer buffer = new StringBuffer();

        while(cursor.moveToNext()){
            String n=cursor.getString(0);
            int age=cursor.getInt(1);

            buffer.append(n+":"+age+"\n");
        }

        Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();
    }

    public void clickupdatebyname(View view) {
        //헤당 이름을 가진 데이터의 age 를 30으로 변경
        String name=etname.getText().toString();

        db.execSQL("UPDATE member SET age = 30,email ='vv@vv' WHERE name=? ", new String[]{name});
    }

    public void clickdeletebyname(View view) {
        String name = etname.getText().toString();

        db.execSQL("DELETE FROM member WHERE name=?",new String[]{name});
    }
}