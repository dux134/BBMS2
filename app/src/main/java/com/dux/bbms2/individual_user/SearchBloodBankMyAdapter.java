package com.dux.bbms2.individual_user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dux.bbms2.R;
import com.dux.bbms2.bank_user.BankUserDataModel;

import java.util.ArrayList;

import static com.dux.bbms2.individual_user.SearchBloodBank.spinner;

public class SearchBloodBankMyAdapter extends RecyclerView.Adapter<SearchBloodBankMyAdapter.SearchBloodBankViewHolder> {
    private ArrayList<SearchBloodBankDataModel> list;
    private SearchBloodBankMyAdapter.RecyclerItemListener listener;
    private Activity context;

    public SearchBloodBankMyAdapter(ArrayList<SearchBloodBankDataModel> list, RecyclerItemListener listener, Activity context) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchBloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search_blood_bank,parent,false);
        SearchBloodBankViewHolder holder = new SearchBloodBankViewHolder(view,listener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBloodBankViewHolder holder, final int position) {
//        holder.unitsAvailable.setText(list.get(position).);
        holder.unitsAvailable.setText(list.get(position).getBloodShown() + " Units Available");
        holder.address.setText(list.get(position).getAddress());
        holder.bloodBankName.setText(list.get(position).getFullname());

        holder.mobile.setText("Ph no. : +91-"+list.get(position).getMobile());

//        if(Integer.valueOf(list.get(position).getBloodShown()) <10 )
//            holder.unitsAvailable.setTextColor(Color.parseColor("#D81B60"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerItemListener {
        void onClick(View view, int adapterPosition);
    }

    public class SearchBloodBankViewHolder extends RecyclerView.ViewHolder {
        private TextView bloodBankName,address,unitsAvailable,mobile;
        private ImageView call;
        private RecyclerItemListener listener2;

        public SearchBloodBankViewHolder(View itemView, final RecyclerItemListener listener) {
            super(itemView);

            bloodBankName = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.description);
            unitsAvailable = itemView.findViewById(R.id.units_available);
            mobile = itemView.findViewById(R.id.search_bank_mobile);
            call = itemView.findViewById(R.id.search_bank_call_image);
            listener2 = listener;
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener2.onClick(view,getAdapterPosition());
                }
            });
        }
    }
}
