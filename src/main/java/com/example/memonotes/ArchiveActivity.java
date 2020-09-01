package com.example.memonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ArchiveActivity extends AppCompatActivity {
    ArrayList<String>Anotes;
    ArrayList<String>ATitle;
    ArrayList<String>ADate;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView2;
    ArchiveAdapt customAdapter2;

    public void recycle2(){
        recyclerView2=findViewById(R.id.recycle2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        customAdapter2=new ArchiveAdapt(Anotes,getApplicationContext(),ADate,ATitle);
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView2.addItemDecoration(dividerItemDecoration);
        recyclerView2.setAdapter(customAdapter2);
        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView2);

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
                case ItemTouchHelper.RIGHT:
                    deletedNote = Anotes.get(pos);
                    deletedTitle=ATitle.get(pos);
                    deletedDate=ADate.get(pos);
                    Anotes.remove(pos);
                    ADate.remove(pos);
                    ATitle.remove(pos);
                    customAdapter2.notifyItemRemoved(pos);
                    sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
                    try {
                        sharedPreferences.edit().putString("Anote",ObjectSerializer.serialize(Anotes)).apply();
                        sharedPreferences.edit().putString("Adate",ObjectSerializer.serialize(ADate)).apply();
                        sharedPreferences.edit().putString("Atitle",ObjectSerializer.serialize(ADate)).apply();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String finalDeletedNote = deletedNote;
                    Snackbar.make(recyclerView2, deletedNote, Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Anotes.add(pos, finalDeletedNote);
                                    ATitle.add(pos,deletedTitle);
                                    ADate.add(pos,deletedDate);

                                    customAdapter2.notifyItemInserted(pos);
                                    sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
                                    try {
                                        sharedPreferences.edit().putString("Anote",ObjectSerializer.serialize(Anotes)).apply();
                                        sharedPreferences.edit().putString("Adate",ObjectSerializer.serialize(ADate)).apply();
                                        sharedPreferences.edit().putString("Atitle",ObjectSerializer.serialize(ADate)).apply();

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
                    .addSwipeRightBackgroundColor(Color.parseColor("#FF0000"))
                    .addSwipeRightActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        Anotes=new ArrayList<>();
        ATitle= new ArrayList<>();
        ADate= new ArrayList<>();
        sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);

        try{

            Anotes= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Anote",ObjectSerializer.serialize(new ArrayList<>())));
            ADate= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Adate",ObjectSerializer.serialize(new ArrayList<>())));
            ATitle= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Atitle",ObjectSerializer.serialize(new ArrayList<>())));
            System.out.println(Anotes);
            System.out.println(ATitle);
            System.out.println(ADate);


            recycle2();
            customAdapter2.notifyDataSetChanged();
        }catch(Exception e){
            e.printStackTrace();
        }









    }
}
