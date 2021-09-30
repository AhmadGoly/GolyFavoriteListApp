package com.example.favlistapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class CategoryFragment extends Fragment implements CategoryRecyclerAdapter.CategoryIsClickedInterface{
    //First we need an interface so fragment can talk to activity:
    interface OnCategoryInteractionListener {
        void categoryIsTapped(Category category);
    }
    private RecyclerView categoryRecyclerView;
    private CategoryManager mCategoryManager;
    private OnCategoryInteractionListener listenerObject;

    public CategoryManager getCategoryManager() {
        return mCategoryManager;
    }

    @Override
    public void categoryIsClicked(Category category) {
        listenerObject.categoryIsTapped(category);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //if MainActivity implements OnCategoryInteractionListener:
        //making sure MainActivity implemented our method:
        if(context instanceof OnCategoryInteractionListener){
            listenerObject = (OnCategoryInteractionListener) context;
            mCategoryManager = new CategoryManager(context);

        } else throw new RuntimeException("Hey Developer. the context or activity" +
                " must implement the OnCategoryInteractionListener");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listenerObject = null;
    }

    public CategoryFragment() {
        // Required empty public constructor
    }


    public static CategoryFragment newInstance() {
        return new CategoryFragment();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Category> categories = mCategoryManager.retrieveCategories();
        if(getView() != null){
        categoryRecyclerView = getView().findViewById(R.id.category_RV);
        categoryRecyclerView.setAdapter(new CategoryRecyclerAdapter(categories,this));
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));}
        else throw new RuntimeException("Error 2222");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }
    //helpful methods:
    public void giveCategoryToManager(Category category){
        mCategoryManager.saveCategory(category);
        //now we load the adapter from "categoryRecyclerView" to reload it.
        CategoryRecyclerAdapter categoryRecyclerAdapter = (CategoryRecyclerAdapter) categoryRecyclerView.getAdapter();
        categoryRecyclerAdapter.addCategory(category);

    }
    public void saveCategory (Category category_backFromSerializable) {
        mCategoryManager.saveCategory(category_backFromSerializable);
        updateRecyclerView();

    }
    private void updateRecyclerView(){
            ArrayList<Category> categories = mCategoryManager.retrieveCategories();
            categoryRecyclerView.setAdapter(new CategoryRecyclerAdapter(categories, this));
    }


}