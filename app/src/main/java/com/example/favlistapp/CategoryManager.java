package com.example.favlistapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
        //in kelas rooye gooshi Category haro bayad save kone.
public class CategoryManager {

      private Context mContext;
      public CategoryManager(Context context){
          this.mContext=context;
      }

      public void saveCategory(Category category){  //save Strings on Phone
          SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mContext);
          SharedPreferences.Editor editor = SP.edit();
          HashSet itemsHashSet = new HashSet(category.getItems());

          editor.putStringSet(category.getName(),itemsHashSet);
          editor.apply();
      }

      public ArrayList<Category> retrieveCategories(){
          SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mContext);
          Map<String,?> allData = SP.getAll();  // ? yani harchi bood bede. vali key hatman String bashe.
          ArrayList<Category> categories = new ArrayList<>();
          for(Map.Entry<String,?> entry : allData.entrySet()){

              Category category = new Category(entry.getKey(), new ArrayList<String>((HashSet) entry.getValue()) );
              categories.add(category);
          }

        return categories;
      }
}
