package com.example.favlistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryitemsActivity extends AppCompatActivity {
    private RecyclerView itemsRV;
    private FloatingActionButton addItemsFloatingActionButton;
    //1: Recover the category we got from mainActivity
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryitems);
        itemsRV = findViewById(R.id.items_RV);

        addItemsFloatingActionButton = findViewById(R.id.add_item_button);
        //1: Recover the category we got from mainActivity
        category = (Category) getIntent().getSerializableExtra(MainActivity.CATEGORY_OBJECT_KEY);

        //2: we got category so we can work with it:
        setTitle(category.getName());
        //2.1: set adapter and pass category to it:
        itemsRV.setAdapter(new ItemsRecyclerAdapter(category));
        itemsRV.setLayoutManager(new LinearLayoutManager(this));
        addItemsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayItemCreationDialog();
            }
        });
    }
    private void displayItemCreationDialog(){
        final EditText itemEditText = new EditText(CategoryitemsActivity.this); //or just "this"
        itemEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle(R.string.item_dialog_title)
                .setView(itemEditText)
                .setPositiveButton(R.string.positive_button_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemName = itemEditText.getText().toString();
                        category.getItems().add(itemName);
                        //let's refresh adapter and RV.
                        ItemsRecyclerAdapter itemsRA = (ItemsRecyclerAdapter) itemsRV.getAdapter();
                        itemsRA.notifyItemInserted(category.getItems().size() - 1);
                        dialogInterface.dismiss();
                    }
                }).create().show();


    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        //passing back category to MainActivity:
        bundle.putSerializable(MainActivity.CATEGORY_OBJECT_KEY,category);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK,intent); //first int is saying to MainActivity: everything went Ok.

        //super should be after the codes.
        super.onBackPressed();

    }
}