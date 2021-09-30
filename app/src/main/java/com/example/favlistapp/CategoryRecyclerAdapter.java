package com.example.favlistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    //String [] categories = {"Hobbies","Sports","Games","Electronic Gadgets","Foods","Countries","LifeStyles","x","y"};
    private ArrayList<Category> categories;
    interface CategoryIsClickedInterface{
        void categoryIsClicked(Category category);
    }
    private CategoryIsClickedInterface categoryIsClickedListener;

    public CategoryRecyclerAdapter(ArrayList<Category> categories,CategoryIsClickedInterface categoryIsClickedListener) {

        this.categories = categories;
        this.categoryIsClickedListener=categoryIsClickedListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());  //parent = MainActivity
        View view = layoutInflater.inflate(R.layout.category_viewholder,parent, false); //we have to pass false for recyclerview
        return new CategoryViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {        //baraye har item run mishe.
        holder.getTxtCategoryNumber().setText(Integer.toString(position+1));
        holder.getTxtCategoryName().setText(categories.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryIsClickedListener.categoryIsClicked(categories.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addCategory(Category category){
        categories.add(category);
        notifyItemInserted(categories.size()-1);

    }
}
