package com.dhananjay.exposyslabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhananjay.exposyslabs.adapter.RecycleViewAdapter;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {
    TextView marqueetxt;
    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;

    private DataContent dataContent;
    private List<DataContent> dataContentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        //https://stackoverflow.com/questions/44135857/working-with-navigationview-items-within-a-fragment , if we use view.findviewbyid then it cant find nav_view coz fragments are inside the activity
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

        marqueetxt= view.findViewById(R.id.marqueeText);
        marqueetxt.setSelected(true);
        marqueetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new InternshipFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                navigationView.getMenu().getItem(2).setChecked(true);
                getActivity().setTitle(navigationView.getMenu().getItem(2).getTitle());
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataContentList = new ArrayList<>();

        dataContentList.add(new DataContent(R.drawable.energy,"Energy","Solar energy and Wind energy together will create BRICK energy and it is free that’s what we are creating so you can save a lot on energy bills in coming years. “No need to buy Electricity anymore”"));
        dataContentList.add(new DataContent(R.drawable.defense,"Defense","47SDT drone surveillance device with shooting capabilities that will monitor streets and borders of your country with solar technology. No need of Army anymore"));
        dataContentList.add(new DataContent(R.drawable.robo,"Robots","A 5ft robot who will do your entire domestic work not limited to ordering a pizza/grocery or teaching your children just in $1500 with 2 years of replacement warranty. No need of Helpers anymore"));


        recycleViewAdapter = new RecycleViewAdapter(getContext(),dataContentList);
        recyclerView.setAdapter(recycleViewAdapter);

        WebView webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("file:///android_asset/abcd.html");

        return view;
    }
}
