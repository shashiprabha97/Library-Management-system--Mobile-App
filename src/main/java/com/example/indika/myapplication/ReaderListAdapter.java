package com.example.indika.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReaderListAdapter extends RecyclerView.Adapter<ReaderListAdapter.ReadViewHolder> {
    private List<ReadingItem> mReadingList;
    private OnItemClickListner mListner;

    public interface OnItemClickListner{
        void OnItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.mListner = listner;
    }

    public ReaderListAdapter(List<ReadingItem> mReadingList){
        this.mReadingList = mReadingList;
    }
    @NonNull
    @Override
    public ReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_item,parent,false);
        ReadViewHolder rvh = new ReadViewHolder(v,mListner);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReadViewHolder holder, int position) {
        ReadingItem readItem = this.mReadingList.get(position);

        holder.tTitle.setText(readItem.getTitle());
        holder.tType.setText(readItem.getType());
        holder.tCategory.setText(readItem.getCategory());
    }

    @Override
    public int getItemCount() {
        return mReadingList.size();
    }

    public static class ReadViewHolder extends RecyclerView.ViewHolder{
        public TextView tTitle;
        public TextView tCategory;
        public TextView tType;

        public ReadViewHolder(View itemView, final OnItemClickListner listner){
            super(itemView);
            tTitle = itemView.findViewById(R.id.titleBox);
            tCategory = itemView.findViewById(R.id.category);
            tType = itemView.findViewById(R.id.type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listner.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
