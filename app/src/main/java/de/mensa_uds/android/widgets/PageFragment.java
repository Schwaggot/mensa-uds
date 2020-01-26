package de.mensa_uds.android.widgets;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.mensa_uds.android.MainActivity;
import redfox.android.mensa.R;

public class PageFragment extends Fragment {

    public static Fragment newInstance(int num) {
        PageFragment f = new PageFragment();

        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pager, container, false);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.instance));

        int mNum = getArguments() != null ? getArguments().getInt("num") : 1;

        RecyclerAdapter adapter = new RecyclerAdapter(mNum, getActivity());
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }
}