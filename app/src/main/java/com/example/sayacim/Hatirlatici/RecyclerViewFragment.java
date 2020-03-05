package com.example.sayacim.Hatirlatici;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sayacim.Adapters.DataAdapter;
import com.example.sayacim.R;


public class RecyclerViewFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private DataAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyler_view,container,false);
        Log.d(TAG,"onCreateView:starting.");
        recyclerView = view.findViewById(R.id.fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = ((HatirlaticiMainActivity)getActivity()).getDataAdapter();
        recyclerView.setAdapter(adapter);
        return  view;
    }

}
