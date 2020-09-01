package com.example.memonotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> notes;
    static int pos;
    SharedPreferences sharedPreferences;
    FloatingActionButton addButton;


    static customAdapter customAdapter;
    static RecyclerView recyclerView;
    static ArrayList<String>date=new ArrayList<>();
    static ArrayList<String>title=new ArrayList<>();
     ArrayList<String> ArchiveNote;
     ArrayList<String>ArchiveDate;
     ArrayList<String>ArchiveTitle;




    public void recycle(){
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter=new customAdapter(notes,getApplicationContext(),date,title);
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(customAdapter);
        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        final int pos = viewHolder.getAdapterPosition();
        String deletedNote ;
        final String deletedTitle;
        final String deletedDate;

        switch (direction) {

            case ItemTouchHelper.LEFT:
                deletedNote = notes.get(pos);
                deletedTitle=title.get(pos);
                deletedDate=date.get(pos);
                notes.remove(pos);
                date.remove(pos);
                title.remove(pos);
                customAdapter.notifyItemRemoved(pos);
                sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("mnotes",ObjectSerializer.serialize(notes)).apply();
                    sharedPreferences.edit().putString("mtitle",ObjectSerializer.serialize(title)).apply();
                    sharedPreferences.edit().putString("mDate",ObjectSerializer.serialize(date)).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String finalDeletedNote = deletedNote;
                Snackbar.make(recyclerView, deletedNote, Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notes.add(pos, finalDeletedNote);
                                title.add(pos,deletedTitle);
                                date.add(pos,deletedDate);

                                customAdapter.notifyItemInserted(pos);
                                sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
                                try {
                                    sharedPreferences.edit().putString("mnotes",ObjectSerializer.serialize(notes)).apply();
                                    sharedPreferences.edit().putString("mDate",ObjectSerializer.serialize(date)).apply();
                                    sharedPreferences.edit().putString("mtitle",ObjectSerializer.serialize(title)).apply();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).show();break;
            case ItemTouchHelper.RIGHT:
                ArchiveNote = new ArrayList<>();
                 ArchiveDate=new ArrayList<>();
                 ArchiveTitle=new ArrayList<>();
                                       final String Anote=notes.get(pos);
                                       final String Atitle=title.get(pos);
                                       final String Adate=date.get(pos);
                                       ArchiveNote.add(Anote);
                                       ArchiveDate.add(Adate);
                                       ArchiveTitle.add(Atitle);
                        try{
                sharedPreferences.edit().putString("Anote",ObjectSerializer.serialize(ArchiveNote)).apply();
                sharedPreferences.edit().putString("Adate",ObjectSerializer.serialize(ArchiveDate)).apply();
                sharedPreferences.edit().putString("Atitle",ObjectSerializer.serialize(ArchiveTitle)).apply();
                                       notes.remove(pos);
                                       title.remove(pos);
                                       date.remove(pos);
                                       customAdapter.notifyItemRemoved(pos);
                sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);

                    sharedPreferences.edit().putString("mnotes",ObjectSerializer.serialize(notes)).apply();
                    sharedPreferences.edit().putString("mDate",ObjectSerializer.serialize(date)).apply();
                    sharedPreferences.edit().putString("mtitle",ObjectSerializer.serialize(title)).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                                       Snackbar.make(recyclerView,Anote+"Archived",Snackbar.LENGTH_LONG)
                                               .setAction("Undo", new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       ArchiveNote.remove(ArchiveNote.lastIndexOf(Anote));
                                                       ArchiveDate.remove(ArchiveDate.lastIndexOf(Adate));
                                                       ArchiveTitle.remove(ArchiveTitle.lastIndexOf(Atitle));
                                                       notes.add(pos,Anote);
                                                       title.add(pos,Atitle);
                                                       date.add(pos,Adate);
                                                       customAdapter.notifyItemInserted(pos);
                                                       sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
                                                       try {
                                                           sharedPreferences.edit().putString("mnotes",ObjectSerializer.serialize(notes)).apply();
                                                           sharedPreferences.edit().putString("mDate",ObjectSerializer.serialize(date)).apply();
                                                           sharedPreferences.edit().putString("mtitle",ObjectSerializer.serialize(title)).apply();

                                                       } catch (Exception e) {
                                                           e.printStackTrace();
                                                       }
                                                   }
                                               }).show();break;



        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(Color.parseColor("#FF0000"))
               .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark))
                .addSwipeRightActionIcon(R.drawable.ic_archive_black_24dp)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }
};

    public void nightMode(){
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu1,menu);
        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){


            case R.id.archive:Intent intent= new Intent(getApplicationContext(),ArchiveActivity.class);
                              startActivity(intent);
                              return  true;




        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       addButton=findViewById(R.id.floatingActionButton);
       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent= new Intent(getApplicationContext(),NotesActivity.class);
               notes.add("");
               title.add("");
               Log.i("reached","onOptionitems");
               SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
               date.add(sdf.format(new Date()));
               sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
               try {
                   sharedPreferences.edit().putString("mDate",ObjectSerializer.serialize(date)).apply();
                   sharedPreferences.edit().putString("mnotes",ObjectSerializer.serialize(notes)).apply();
                   sharedPreferences.edit().putString("mtitle",ObjectSerializer.serialize(title)).apply();

               } catch (Exception e) {
                   e.printStackTrace();
               }
               pos=notes.size()-1;
               customAdapter.notifyDataSetChanged();
               intent.putExtra("notesIndex",pos);
               Log.i("reached","just before startactivity and position is"+pos);
               startActivity(intent);

           }
       });
        date=new ArrayList<>();
        notes = new ArrayList<String>();
        title=new ArrayList<>();
        sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);

        Log.i("reached","try catch before");

        try {
            notes= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("mnotes",ObjectSerializer.serialize(new ArrayList<>())));
            date= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("mDate",ObjectSerializer.serialize(new ArrayList<>())));
            title= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("mtitle",ObjectSerializer.serialize(new ArrayList<>())));
            System.out.println(notes);
            System.out.println(title);
            System.out.println(date);

            recycle();
            Log.i("reached","try catch");


        } catch (Exception e) {
            e.printStackTrace();

/*
       arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,notes){
           @NonNull
           @Override
           public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
               View view= super.getView(position, convertView, parent);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.alert_dark_frame)
                        .setTitle("Note Delete karna hai kya ?")
                        .setMessage("Dekhle delete hone ke baad wapas nahi ayega")
                        .setPositiveButton("Haa Teke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                HashSet<String> set =new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();

                            }
                        })
                        .setNegativeButton("Naa wapas",null).show();

                return true;
            }
        });*/


    }
}}
