package com.example.to_do;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Tabbed extends AppCompatActivity
{
    String catname;
    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseHelper mydb;
    TabItem tabtask, tabdelayed, tabdone, tabfavourite;
    ImageView btnsearch,btnadd;
    EditText input;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    boolean check=true;
    FloatingActionButton filter;
    Spinner spinner;
    String [] arraySpinner = {" As Creation Time","As Ascending","As Decencing", "Clear Selection"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        // setupWindowAnimations();

        tabLayout = findViewById(R.id.tabs);
        tabtask = findViewById(R.id.tabItemtask);
        tabdelayed = findViewById(R.id.tabItem2delayed);
        tabdone = findViewById(R.id.tabItem3done);
        tabfavourite = findViewById(R.id.tabItem4fav);
        viewPager = findViewById(R.id.container);
        btnsearch=(ImageView)findViewById(R.id.iconsearch);
        btnadd=(ImageView)findViewById(R.id.addicon);
        filter=(FloatingActionButton)findViewById(R.id.filter);
        spinner=(Spinner)findViewById(R.id.spinner);

//        btnaddpop_up=(TextView)findViewById(R.id.btnaddpop_up);
//        btncanpop_up=(TextView)findViewById(R.id.btncancelpop_up);

        mydb = new DatabaseHelper(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        input=(EditText)findViewById(R.id.txtgroup_name);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
*/
       /* filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                 spinner.getItemAtPosition(position);
                 if (spinner.equals("As Creation Time")){
                     Toast.makeText(Tabbed.this, "Hello Toast",Toast.LENGTH_SHORT).show();

                 }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });*/

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Tabbed.this);
                dialog.setContentView(R.layout.add_group_layout);		//create xml file

                // set the custom dialog components - text, image and button
                final EditText input = (EditText) dialog.findViewById(R.id.txtgroup_name);

                TextView add = (TextView) dialog.findViewById(R.id.btnaddpop_up);
                final TextView can = (TextView) dialog.findViewById(R.id.btncancelpop_up);
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(input.getText().toString().length()> 30)
                        {
                            check =false;
                            //Toast.makeText(Tabbed.this, "Please enter only 30 characters!!!", Toast.LENGTH_SHORT).show();
                            input.setError("30 characters ONLY !");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if(check==true) {
                            catname = input.getText().toString();
                            if (catname == null || catname.isEmpty()) {
                                catname = "New Group";
                            }

                            boolean isInserted = mydb.insertData(catname);
                            if (isInserted == true) {
                                Toast.makeText(Tabbed.this, "Task Group Successfully added.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Tabbed.this, Tabbed.class);
                                finish();
                                startActivity(intent);

                                /*Fragment fragment = new frag_group_category();
                                Bundle bundle=new Bundle();
                                String str="refresh";
                                bundle.putString("refresh",str);
                                fragment.setArguments(bundle);*/

                                dialog.cancel();
                            } else {
                                input.setError("Group Name Empty!");
                                input.requestFocus();
                                Toast.makeText(Tabbed.this, "Category is not added.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });

                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Log.i("asd","ok");
                dialog.show();
            }

        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Tabbed.this,search.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabbed, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment()
        { }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView=null;
            return rootView;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    frag_group_category tab1 = new frag_group_category();
                    return tab1;
                case 1:
                    frag_delayed  tab2 = new frag_delayed();
                    return tab2;
                case 2:
                    frag_done tab3 = new frag_done();
                    return tab3;
                case 3:
                    frag_fav tab4 = new frag_fav();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    }
}
