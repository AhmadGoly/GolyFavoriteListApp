package com.example.favlistapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.favlistapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryFragment.OnCategoryInteractionListener {
    private ActivityMainBinding binding;

    public static final String CATEGORY_OBJECT_KEY = "CATEGORY_KEY";
    public static final int MAIN_ACTIVITY_REQUEST_CODE = 1234;

    //Fragments:
    private CategoryFragment mCategoryFragment;
    private CategoryItemsFragment mCategoryItemsFragment;
    private FrameLayout categoryItemsFragmentContainer;

    //Tablet Mode:
    private boolean isTablet = false;
    FloatingActionButton fab;   //this is for tablet.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        //Defining Category Fragment:
        mCategoryFragment = (CategoryFragment) getSupportFragmentManager().findFragmentById(R.id.category_fragment);
        categoryItemsFragmentContainer = findViewById(R.id.category_items_fragment_container);
        isTablet = (categoryItemsFragmentContainer != null);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCreateCategoryDialog();
            }
        });
    }

    //Fab Alert Function
    private void displayCreateCategoryDialog() {
        String alertTitle = getString(R.string.create_category);
        String positiveButtonTitle = getString(R.string.positive_button_title);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        EditText categoryEditText = new EditText(this);
        categoryEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        alertBuilder.setTitle(alertTitle);
        alertBuilder.setView(categoryEditText);

        alertBuilder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Category cache = new Category(categoryEditText.getText().toString(), new ArrayList<String>());
                //replaced with:
                //mCategoryManager.saveCategory(cache);
                //now we load the adapter from "categoryRecyclerView" to reload it.
                //CategoryRecyclerAdapter categoryRecyclerAdapter = (CategoryRecyclerAdapter) categoryRecyclerView.getAdapter();
                //categoryRecyclerAdapter.addCategory(cache);
                /*replaced with:*/ mCategoryFragment.giveCategoryToManager(cache);
                dialogInterface.dismiss();
                displayCategoryItems(cache);
            }
        });
        alertBuilder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayCategoryItems(Category category) {
        if (!isTablet) {
            //Start a new Activity for Phone.
            Intent categoryItemsIntent = new Intent(this, CategoryitemsActivity.class);
            categoryItemsIntent.putExtra(CATEGORY_OBJECT_KEY, category); //we cant pass category to another activity. so
            //we added "Implements serializable" to Category Class.
            startActivityForResult(categoryItemsIntent, MAIN_ACTIVITY_REQUEST_CODE);
        } else{
            //Remove the old Fragment and setup a new Fragment.
            if(mCategoryItemsFragment != null){
                getSupportFragmentManager().beginTransaction().remove(mCategoryItemsFragment).commit();
                mCategoryItemsFragment = null;
            }

            setTitle(category.getName());
            mCategoryItemsFragment = CategoryItemsFragment.newInstance(category);
            if(mCategoryItemsFragment != null)getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.category_items_fragment_container,mCategoryItemsFragment)
                    .addToBackStack(null)
                    .commit();

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayCreateCategoryItemDialog();
                }
            });
        }
    }

    private void displayCreateCategoryItemDialog() {
        final EditText itemET = new EditText(this);
        itemET.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this).setTitle(getString(R.string.item_dialog_title))
                .setView(itemET)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = itemET.getText().toString();
                        mCategoryItemsFragment.addItemToCategory(item);
                        dialogInterface.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestcode should be MAIN_ACTIVITY_REQUEST_CODE
        if (requestCode == MAIN_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //if this is true we got category as data.
            if (data != null) {
                //mCategoryManager.saveCategory((Category) data.getSerializableExtra(CATEGORY_OBJECT_KEY));
                //updateCategories();
                //2 lines replaced with:
                mCategoryFragment.saveCategory((Category) data.getSerializableExtra(CATEGORY_OBJECT_KEY));
            }
        }
    }

    //private void updateCategories() {
    //    ArrayList<Category> categories = mCategoryManager.retrieveCategories();
    //    categoryRecyclerView.setAdapter(new CategoryRecyclerAdapter(categories, this));
    //}
    //Moved to Fragment with implementation.
    //@Override
    //public void categoryIsClicked(Category category) {
    //    displayCategoryItems(category);
    //}

    @Override
    public void categoryIsTapped(Category category) {
        displayCategoryItems(category);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(getString(R.string.app_name));
        if(mCategoryItemsFragment.category != null){
            mCategoryFragment.getCategoryManager().saveCategory(mCategoryItemsFragment.category);
        }
        if (mCategoryItemsFragment != null){
            getSupportFragmentManager().beginTransaction().remove(mCategoryItemsFragment).commit();
            mCategoryItemsFragment = null;

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCreateCategoryDialog();
            }
        });
    }
}
