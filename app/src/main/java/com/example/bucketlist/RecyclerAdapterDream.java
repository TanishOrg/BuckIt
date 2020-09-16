package com.example.bucketlist;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterDream extends RecyclerView.Adapter<RecyclerAdapterDream.ViewHolder>{

    @NonNull
    Dialog myDialog;

    private static final String TAG = "RECYCLERadapter";
    int count = 0;
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        Log.i(TAG,"oncreateviewholder" + count++);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_dream,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        myDialog = new Dialog(parent.getContext(),android.R.style.Theme_Translucent_NoTitleBar);
        myDialog.setContentView(R.layout.popup_show_window);

        viewHolder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "test click" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                myDialog.show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout card_item;
        ImageView imageView;
        TextView cardTitle , cardTargetDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cardCategoryIcon);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardTargetDate = itemView.findViewById(R.id.cardTargetDate);
            card_item = itemView.findViewById(R.id.card_item);


        }
    }
}
