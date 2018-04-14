package com.md.numacts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.md.numacts.R;
import com.md.numacts.data.FactResponse;
import com.md.numacts.data.HistoryData;

import java.util.List;

/**
 * Created by Danish Ansari on 13-Oct-17.
 */

public class SeriesFactsAdapter extends RecyclerView.Adapter<SeriesFactsAdapter.MyViewHolder>
{
    private List<FactResponse> list;
    private Context context;

    public SeriesFactsAdapter(List<FactResponse> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_quest, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        FactResponse data = list.get(position);
        holder.tvNumber.setText((int) data.getNumber()+"");
        holder.tvFacts.setText(data.getText());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvNumber, tvFacts;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvFacts = itemView.findViewById(R.id.tvFact);
        }
    }
}
