package com.example.memonotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.memonotes.MainActivity.date;
import static com.example.memonotes.MainActivity.notes;
import static com.example.memonotes.MainActivity.title;

public class ArchiveAdapt extends RecyclerView.Adapter<ArchiveAdapt.viewHolder>  {
    public ArrayList<String> Notes;
    Context context;
    int i=1;

    ArrayList<String>Date;
    public ArrayList<String>Title;

    int Index;
    SharedPreferences sharedPreferences;
    ArrayList<String>STitle;
    ArrayList<String>SNotes;
    ArrayList<String>SDates;




    public ArchiveAdapt(ArrayList<String> notes, Context context,ArrayList<String> dates,ArrayList<String>title) {
        Notes = notes;
        this.context = context;
        Date=dates;
        Title=title;


    }



    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_adapter_22, parent, false);
        Log.i("reached","customAdapter and title is "+Title.get(0));
        Log.i("reached",Notes.size()+","+Date.size()+"," +Title.size());
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        Log.i("reached","the holder popsition is"+position);

        holder.notesText.setText(Notes.get(position));
        holder.dateText.setText(Date.get(position));
        if(Title.get(position)==""){
            holder.titleText.setText("Title here...");
        }else {
            holder.titleText.setText(Title.get(position));
        }
        holder.expandableLayout.setVisibility(View.GONE);
        final ViewGroup.LayoutParams params=holder.titleText.getLayoutParams();
        params.width=1150;

        holder.titleText.setLayoutParams(params);
        holder.titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==1){
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                    holder.titleText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));

                    i=2;
                }
                else{
                    holder.expandableLayout.setVisibility(View.GONE);
                    params.width=1150;
                    holder.titleText.setLayoutParams(params);
                    i=1;
                }
            }
        });





        holder.expandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableLayout.setVisibility(View.VISIBLE);
                holder.titleText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));


                Random random =new Random();
                int color=Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256));
                Intent intent = new Intent(context, NotesActivity.class);
                intent.putExtra("notesIndex", holder.getAdapterPosition());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                String newDate;
                newDate=sdf.format(new Date());
                date.remove(holder.getAdapterPosition());
                date.add(holder.getAdapterPosition(),newDate);
                notifyDataSetChanged();
                sharedPreferences=context.getSharedPreferences("com.example.memonotes", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("mDate",ObjectSerializer.serialize(date)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                context.startActivity(intent);

            }
        });

        Index=holder.getAdapterPosition();

    }


    @Override
    public int getItemCount() {
        return Date.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notesText;
        TextView dateText;
        TextView text;
        TextView titleText;
        ConstraintLayout expandableLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            notesText = itemView.findViewById(R.id.notesText);
            dateText=itemView.findViewById(R.id.dateText);
            text=itemView.findViewById(R.id.text);
            titleText=itemView.findViewById(R.id.titleText);
            expandableLayout=itemView.findViewById(R.id.expandableLayout);


        }


        @Override
        public void onClick(View v) {
        }
    }


}