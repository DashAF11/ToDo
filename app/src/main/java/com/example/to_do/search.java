package com.example.to_do;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    ImageView btnsearch, btnback;
    String str;
    EditText txtsearch_task_cat;
    DatabaseHelper mydb;
    ArrayList<String> list_name;
    ArrayList<String> list_id;
    ArrayList<String> list_date;
    ArrayList<String> list_time;
    ArrayList<String> list_type;
    ArrayList<String> list_status;
    ArrayList<String> list_count;
    ArrayList<String> list_pcount;

    search_Adapter search_adapter;
    RecyclerView listview;
    RelativeLayout r1, r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        list_id = new ArrayList<>();
        list_name=new ArrayList<>();
        list_date = new ArrayList<>();
        list_time = new ArrayList<>();
        list_type = new ArrayList<>();
        list_status = new ArrayList<>();
        list_pcount =new ArrayList<>();
        list_count = new ArrayList<>();
        mydb = new DatabaseHelper(this);

        btnsearch = (ImageView) findViewById(R.id.btn_search_cattask);
        btnback = (ImageView) findViewById(R.id.back_icon);
        txtsearch_task_cat = (EditText) findViewById(R.id.txtsearch);
        listview = (RecyclerView) findViewById(R.id.search_recycler);
        r1 = (RelativeLayout) findViewById(R.id.r1);
        r2 = (RelativeLayout) findViewById(R.id.r2);

        search_adapter = new search_Adapter(search.this, list_id, list_name, list_date, list_time, list_type, list_status, list_count, list_pcount);

        txtsearch_task_cat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txtsearch_task_cat.getText().toString() == null) {
                    clear();
                    //search_adapter(notifyDataSetChanged);
                } else {
                    str = txtsearch_task_cat.getText().toString();
                    list_name.clear();
                    list_date.clear();
                    list_time.clear();
                    list_id.clear();
                    list_type.clear();
                    list_count.clear();
                    list_pcount.clear();
                    list_status.clear();
                    //search_adapter(notifyDataSetChanged);

                    if(str.length() > 0) {
                        search();
                    } else {
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(search.this,Tabbed.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void clear() {
        list_name.clear();
        list_date.clear();
        list_time.clear();
        list_id.clear();
        list_type.clear();
        list_count.clear();
        list_pcount.clear();
        list_status.clear();
    }

    public void search()
    {
        //for categorynaoe
        Cursor res = mydb.search_categoriesname(str);
        Log.i("ccccccccccccc",str+"\t"+txtsearch_task_cat.getText().toString());
        if (res.getCount() == 0) {
            Log.i("Nothing found", "empty");
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
        } else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }
        while (res.moveToNext()) {
            list_id.add(res.getString(0));
            list_name.add(res.getString(1));
            list_date.add(" ");
            list_time.add(" ");
            list_type.add("Group");
            list_status.add(" ");
        }
        res.close();

        //for task id
        ArrayList<String> refid = list_id;
        for (int i = 0; i < refid.size(); i++)
        {
            Cursor c = mydb.getcatCount(String.valueOf(refid.get(i)));

            while (c.moveToNext())
            {
                if (c.getString(0) == null) {
                    list_count.add("0");
                } else {
                    list_count.add(c.getString(0));
                }
                c.close();
                //for count of pendings
                Cursor c1 = mydb.getcatCountpending(String.valueOf(refid.get(i)));

                while(c1.moveToNext())
                {
                    if(c1.getString(0) == null)
                    {
                        list_pcount.add("0");
                    } else {
                        list_pcount.add(c1.getString(0));
                    }
                }
                c1.close();
            }

            //task name
            Cursor res1=mydb.search_taskname(str);
            if (res.getCount() == 0)
            {
                Log.i(" Nothing found", "empty");
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
            }
            else{
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
            }

            while (res1.moveToNext()) {
                list_id.add(res1.getString(0));
                list_name.add(res1.getString(1));
                list_date.add(res1.getString(2));
                list_time.add(res1.getString(3));
                list_status.add(res1.getString(5));
                list_type.add("Task");
                list_count.add(" ");
                list_pcount.add(" ");
            }
            res1.close();

            search_adapter = new search_Adapter(search.this, list_id, list_name, list_date, list_time, list_type, list_status, list_count, list_pcount);
            RecyclerView.LayoutManager mLayoutManeger = new LinearLayoutManager(search.this);
            listview.setLayoutManager(mLayoutManeger);
            listview.setItemAnimator(new DefaultItemAnimator());
            listview.setAdapter(search_adapter);
        }
    }
        @Override
        public void onBackPressed()
        {
            super.onBackPressed();
            Intent intent = new Intent(search.this, Tabbed.class);
            finish();
            startActivity(intent);
        }
    }


