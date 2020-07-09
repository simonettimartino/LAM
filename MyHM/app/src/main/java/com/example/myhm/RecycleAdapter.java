package com.example.myhm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ExampleViewHolder> {

    private static final String TAG = "RecycleAdapter";
    private List<Reports> mExampleList;
    private OnNoteListener mOnNoteListener;

    public RecycleAdapter(List<Reports> exampleList, OnNoteListener mOnNoteListener) {
        this.mExampleList = exampleList;
        this.mOnNoteListener = mOnNoteListener;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_2recycle, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mOnNoteListener);
        return evh;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView mTextView3;
            public TextView mTextView1;
            public TextView mTextView2;
            public TextView mTextView4;
            OnNoteListener mOnNoteListener;

        public ExampleViewHolder(View itemView, OnNoteListener onNoteListener) {
                super(itemView);

                mTextView1 = itemView.findViewById(R.id.textView2);
                mTextView2 = itemView.findViewById(R.id.textView4);
                mTextView3 = itemView.findViewById(R.id.textView6);
                mTextView4 = itemView.findViewById(R.id.textView8);
                mOnNoteListener = onNoteListener;

            }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onItemClick(getAdapterPosition());
        }
    }
       /* public void RecycleAdapter(List<Reports> exampleList) {
            mExampleList = exampleList;
        }
*/



    @Override
        public void onBindViewHolder(ExampleViewHolder holder, int position) {
            try{
                Reports currentItem = mExampleList.get(position);
                holder.mTextView1.setText(Double.toString(currentItem.getPeso().getValore()));
                holder.mTextView2.setText(Double.toString(currentItem.getTemperatura().getValore()));
                holder.mTextView3.setText(Double.toString(currentItem.getGlicemia().getValore()));
                holder.mTextView4.setText(currentItem.getNote());
            }catch (NullPointerException e){
                Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
            }
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        public interface OnNoteListener{
            void onItemClick(int position);

        }
    }

