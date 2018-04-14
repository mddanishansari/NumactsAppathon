package com.md.numacts.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.md.numacts.R;
import com.md.numacts.adapter.QuestHistoryAdapter;
import com.md.numacts.data.HistoryData;
import com.md.numacts.database.DbHelper;
import com.md.numacts.util.QuestRecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestHistoryFragment extends Fragment implements QuestRecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{
    View v;
    RecyclerView recyclerView;
    List<HistoryData> historyDataList = new ArrayList<>();
    QuestHistoryAdapter adapter;
    DbHelper dbHelper;
    SQLiteDatabase db;
    HistoryData data;
    LinearLayout noHistorylayout;
    Cursor cursor;

    public QuestHistoryFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_quest_history, container, false);
        dbHelper = new DbHelper(v.getContext());
        db = dbHelper.getWritableDatabase();
        recyclerView = v.findViewById(R.id.questFactsRV);
        noHistorylayout = v.findViewById(R.id.no_history_layout);
        adapter = new QuestHistoryAdapter(historyDataList, v.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new QuestRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        cursor = db.query(DbHelper.TABLE_NAME_QUEST,
                new String[]{DbHelper.COLUMN_NAME_NUMBER, DbHelper.COLUMN_NAME_FACT},
                null, null, null, null, "id DESC");
        if (cursor.moveToFirst())
        {
            noHistorylayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            do
            {
                data = new HistoryData(cursor.getString(0), cursor.getString(1));
                historyDataList.add(data);
            } while (cursor.moveToNext());
            cursor.close();
            adapter.notifyDataSetChanged();
        }
        return v;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {
        if (viewHolder instanceof QuestHistoryAdapter.MyViewHolder)
        {
            final String number = historyDataList.get(viewHolder.getAdapterPosition()).getNumber();
            final String fact = historyDataList.get(viewHolder.getAdapterPosition()).getFact();
            final HistoryData deletedItem = historyDataList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(v.findViewById(R.id.questHistoryRootLayout), "Record deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.show();
            snackbar.addCallback(new Snackbar.Callback()
            {

                @Override
                public void onDismissed(Snackbar snackbar, int event)
                {
                    //see Snackbar.Callback docs for event details
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                    {
                        db.delete(DbHelper.TABLE_NAME_QUEST, DbHelper.COLUMN_NAME_NUMBER + "=? AND "+DbHelper.COLUMN_NAME_FACT+" =?", new String[]{number,fact});
                        if (historyDataList.size() == 0)
                        {
                            noHistorylayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onShown(Snackbar snackbar)
                {
                }
            });
        }
    }
}
