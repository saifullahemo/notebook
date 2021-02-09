package com.example.notebook;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class addCategory extends AppCompatActivity {
    int finalColor;
    ListView mListView;
    public static ArrayList<Categorie> cat2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        finalColor = Color.parseColor("#262D3B");
        mListView = (ListView) findViewById(R.id.listView);
        cat2 = MainActivity.getCat();
        CatAdapter adapter = new CatAdapter(addCategory.this, cat2);
        ((TextView) findViewById(R.id.nb_cat)).setText("Categories (" + String.valueOf(cat2.size()) + ")");
        mListView.setAdapter(adapter);
    }

    public void setColor(View v, final Dialog dialog) {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                if (color != 0)
                    finalColor = color;
                else
                    finalColor = Color.parseColor("#262D3B");
                //((View) findViewById(R.id.color)).setBackgroundColor(color);
                dialog.findViewById(R.id.color).setBackgroundColor(finalColor);
            }

            @Override
            public void onCancel() {
                //here goes nothing
            }
        });
    }


    public void delete(View v) {
        final int position = mListView.getPositionForView((View) v.getParent());
        if (!MainActivity.cat.get(position).getName().equals("none")) {
            MainActivity.cat.remove(position);
            cat2.remove(position);
            CatAdapter a = (CatAdapter) mListView.getAdapter();
            ((TextView) findViewById(R.id.nb_cat)).setText("Categories (" + String.valueOf(cat2.size()) + ")");
            a.notifyDataSetChanged();
        } else
            Toast.makeText(getApplicationContext(), "Error can't delete \"none\" category", Toast.LENGTH_SHORT).show();
        SwipeLayout s = (SwipeLayout) mListView.getChildAt(position);
        s.close(true);
    }


    public void dialog(View view) {

        // custom dialog
        //final View v = (View) this;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_cat_layou);
        dialog.setTitle("Title...");
        dialog.findViewById(R.id.color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColor(view, dialog);
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ok = true;
                int i =0;
                String name = ((TextView) dialog.findViewById(R.id.catName)).getText().toString();
                while (i < MainActivity.getCat().size())
                {
                    if (name.equals(MainActivity.getCat().get(i).getName()))
                        ok = false;
                    i++;
                }
                if (ok) {
                    MainActivity.cat.add(new Categorie(name, finalColor));
                    cat2.add(new Categorie(name, finalColor));
                    CatAdapter a = (CatAdapter) mListView.getAdapter();
                    ((TextView) findViewById(R.id.nb_cat)).setText("Categories (" + String.valueOf(cat2.size()) + ")");
                    a.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: Category " + name + " already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }


    public void finish(View v)
    {
        finish();
    }
}
