package com.example.oi_projekt.adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oi_projekt.R;
import com.example.oi_projekt.activities.LoginSignupPageActivity;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<String> cardList;
    private String exc;
    private String well;
    private String try7;
    private String btr;

    public CardAdapter(ArrayList<String> cardList, String lang) {
        this.cardList = cardList;
        switch (lang) {
            case "en":
                exc = "Excellent!!!";
                try7 = "Try to get 7 Lolipops!";
                well = "Well Done!";
                btr = "Better Luck Next Time!";
                break;
            case "hr":
                exc = "Odlično!!!";
                try7 = "Pokušaj Skupiti 7 Lizalica!";
                well = "Dobro Odrađeno!";
                btr = "Više Sreće Drugi Put!";
                break;
            default:
                exc = "Excellent!!!";
                try7 = "Try to get 7 Lolipops!";
                well = "Well Done!";
                btr = "Better Luck Next Time!";
                break;
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_results, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.textView.setText(cardList.get(position));
        if(cardList.get(position)!=null && cardList.get(position)!=""){
            switch (Integer.parseInt(cardList.get(position))) {
                case 0:
                    holder.progressBar.setProgress(0);
                    holder.textView2.setText(btr);
                    break;
                case 1:
                    holder.textView2.setText(btr);
                    holder.progressBar.setProgress(15);
                    break;
                case 2:
                    holder.progressBar.setProgress(30);
                    holder.textView2.setText(try7);
                    break;
                case 3:
                    holder.progressBar.setProgress(40);
                    holder.textView2.setText(try7);
                    break;
                case 4:
                    holder.progressBar.setProgress(60);
                    holder.textView2.setText(try7);
                    break;
                case 5:
                    holder.progressBar.setProgress(70);
                    holder.textView2.setText(try7);
                    break;
                case 6:
                    holder.progressBar.setProgress(85);
                    holder.textView2.setText(try7);
                    break;
                case 7:
                    holder.progressBar.setProgress(100);
                    holder.textView2.setText(well);
                    break;
                default:
                    holder.progressBar.setProgress(100);
                    holder.textView2.setText(exc);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView textView2;
        public TextView textView;
        public CardView cardView;
        public ProgressBar progressBar;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            cardView = itemView.findViewById(R.id.cardView);
            progressBar = itemView.findViewById(R.id.ProgressBar);
        }
    }
}
