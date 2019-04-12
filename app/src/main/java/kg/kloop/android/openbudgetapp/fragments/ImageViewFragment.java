package kg.kloop.android.openbudgetapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.ImageViewFragmentAdapter;

public class ImageViewFragment extends Fragment {

    RecyclerView recyclerView;


    public ImageViewFragment() {
    }

    public static ImageViewFragment newInstance() {

        Bundle args = new Bundle();

        ImageViewFragment fragment = new ImageViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_image_view, container, false);

        recyclerView = view.findViewById(R.id.image_view_fragment_recycler_view);
        ArrayList<String> urls = getArguments().getStringArrayList("urls");
        recyclerView.setAdapter(new ImageViewFragmentAdapter(getActivity(), urls));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
