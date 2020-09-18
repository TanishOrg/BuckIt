package com.example.bucketlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import com.example.bucketlist.model.BucketItems;

import java.util.List;

public class RecyclerAdapterDream extends RecyclerView.Adapter<RecyclerAdapterDream.ViewHolder>{


    Context context;
    List<BucketItems> itemsList;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;

    public RecyclerAdapterDream(){}

    public RecyclerAdapterDream(Context context,List<BucketItems> items) {
        this.context = context;
        this.itemsList = items;
    }


    private static final String TAG = "RECYCLERadapter";
    int count = 0;
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

//        Log.i(TAG,"oncreateviewholder" + count++);
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.row_item_dream,parent,false);
//        final ViewHolder viewHolder = new ViewHolder(view);
//
//        myDialog = new Dialog(parent.getContext(),android.R.style.Theme_Translucent_NoTitleBar);
//        myDialog.setContentView(R.layout.popup_show_window);
//
//        viewHolder.card_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(parent.getContext(), "test click" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                myDialog.show();
//            }
//        });
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_dream,parent,false);



        return new ViewHolder(view);
//        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        BucketItems items = itemsList.get(position);
        holder.id = items.getId();
        holder.cardTitle.setText(items.getTitle());
        holder.cardTargetDate.setText(items.getDeadline());


        TextView titleOfCard = holder.myDialog.findViewById(R.id.cardTitle);
        TextView infoOfCard = holder.myDialog.findViewById(R.id.cardDescription);
        TextView targetOfCard = holder.myDialog.findViewById(R.id.card_target_date);

        titleOfCard.setText(items.getTitle());
        if (items.getInfo() != null) {
            infoOfCard.setText(items.getInfo());
        }
        targetOfCard.setText(items.getDeadline());
        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "test click" + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                holder.myDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @NonNull
        Dialog myDialog;
        RelativeLayout card_item;
        ImageView imageView;
        TextView cardTitle , cardTargetDate;
        int id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cardCategoryIcon);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardTargetDate = itemView.findViewById(R.id.cardTargetDate);
            card_item = itemView.findViewById(R.id.card_item);

            //inflating
            myDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
            myDialog.setContentView(R.layout.popup_show_window);



        }
    }
}
