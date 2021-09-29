package com.md.numacts.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.md.numacts.R;
import com.md.numacts.data.HistoryData;

import java.util.List;

/**
 * Created by Danish Ansari on 13-Oct-17.
 */

public class QuestHistoryAdapter extends RecyclerView.Adapter<QuestHistoryAdapter.MyViewHolder>
{
    private List<HistoryData> list;
    private Context context;

    public QuestHistoryAdapter(List<HistoryData> list, Context context)
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
        HistoryData data = list.get(position);
        holder.tvFacts.setText(data.getFact());
        holder.tvNumber.setText(data.getNumber());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void removeItem(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(HistoryData data, int position)
    {
        list.add(position, data);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvNumber, tvFacts;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvFacts = itemView.findViewById(R.id.tvFact);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }
    }
}
