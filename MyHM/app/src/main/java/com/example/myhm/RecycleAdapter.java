package com.example.myhm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ExampleViewHolder> {


    private List<Reports> mExampleList;

    public RecycleAdapter(List<Reports> exampleList) {
        mExampleList = exampleList;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView3;
            public TextView mTextView1;
            public TextView mTextView2;
            public ExampleViewHolder(View itemView) {
                super(itemView);

                mTextView1 = itemView.findViewById(R.id.textView2);
                mTextView2 = itemView.findViewById(R.id.textView4);
                mTextView3 = itemView.findViewById(R.id.textView6);

            }
        }
        public void RecycleAdapter(List<Reports> exampleList) {
            mExampleList = exampleList;
        }
        @Override
        public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_2recycle, parent, false);
            ExampleViewHolder evh = new ExampleViewHolder(v);
            return evh;
        }



    @Override
        public void onBindViewHolder(ExampleViewHolder holder, int position) {
            Reports currentItem = mExampleList.get(position);
            holder.mTextView1.setText(Double.toString(currentItem.getPeso().getValore()));
            holder.mTextView2.setText(Double.toString(currentItem.getTemperatura().getValore()));
            holder.mTextView3.setText(Double.toString(currentItem.getGlicemia().getValore()));
        }
        @Override
        public int getItemCount() {
            return mExampleList.size();
        }
    }

