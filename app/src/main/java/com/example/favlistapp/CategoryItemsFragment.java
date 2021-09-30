package com.example.favlistapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CategoryItemsFragment extends Fragment {
    private RecyclerView itemsRV;
    Category category;

    private static final String MY_CATEGORY_ARGS = "categoryargs"; // this key is used to recover category for this fragment.
    public CategoryItemsFragment() {

    }

    public static CategoryItemsFragment newInstance(Category myCategory) {
        CategoryItemsFragment myCIF = new CategoryItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MY_CATEGORY_ARGS,myCategory);
        myCIF.setArguments(bundle);
        return myCIF;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //2: find category by getArguments() method:
        if(getArguments() != null)
            category = (Category) getArguments().getSerializable(MY_CATEGORY_ARGS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);
        if(view != null){
            //1: Create ItemsRV:
            itemsRV = view.findViewById(R.id.items_RV);
            itemsRV.setAdapter(new ItemsRecyclerAdapter(category));
            itemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }
    public void addItemToCategory(String item){
        category.getItems().add(item);
        ItemsRecyclerAdapter itemsRecyclerAdapter = (ItemsRecyclerAdapter) itemsRV.getAdapter();
        itemsRecyclerAdapter.setCategory(category);
        itemsRecyclerAdapter.notifyDataSetChanged();


    }
}