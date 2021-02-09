package com.example.notebook;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int id;
    public static ListView mListView, checkListView;
    public static List<Item> items = new ArrayList<>();
    public static List<Item> tmp = new ArrayList<>();
    public static ArrayList<Categorie> cat = new ArrayList<>();


    TextView nb_tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        checkListView = (ListView) findViewById(R.id.checkCat);
        nb_tasks = (TextView) findViewById(R.id.nb_tasks);

        id = 0;

        getData();
        getCatData();
        if (cat.size() == 0)
            cat.add(new Categorie("none", Color.parseColor("#262D3B")));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentMain = new Intent(MainActivity.this, EditItem.class);
                Item item = (Item) mListView.getAdapter().getItem(position);
                String title = item.getTitle();
                String time = item.getTime();
                String txt = item.getText();
                String date = item.getDate();
                String categorie = item.getCategorie();
                intentMain.putExtra("position", String.valueOf(position));
                intentMain.putExtra("title", title);
                intentMain.putExtra("txt", txt);
                intentMain.putExtra("date", date);
                intentMain.putExtra("time", time);
                intentMain.putExtra("categorie", categorie);
                startActivityForResult(intentMain, 1);
            }
        });

        ItemAdapter adapter = new ItemAdapter(MainActivity.this, items);
        checkAdapter adapter1 = new checkAdapter(MainActivity.this, cat);
        checkListView.setAdapter(adapter1);
        mListView.setAdapter(adapter);
        checkDate();
    }


    public void getData() {
        List<Item> list = new ArrayList<>();
        Item tmp;
        SQLiteDatabase mydatabase = openOrCreateDatabase("notebook", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(Titre VARCHAR, Date VARCHAR, Status VARCHAR, Txt VARCHAR, Cat VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from tasks", null);
        resultSet.moveToFirst();
        int count = 0;
        while (count < resultSet.getCount())
        {
            String title = resultSet.getString(resultSet.getColumnIndex("Titre"));
            String date = resultSet.getString(resultSet.getColumnIndex("Date"));
            String status = resultSet.getString(resultSet.getColumnIndex("Status"));
            String txt = resultSet.getString(resultSet.getColumnIndex("Txt"));
            String cat = resultSet.getString(resultSet.getColumnIndex("Cat"));
            Date d = new Date();
            SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyyHH:mm");
            try {
                d = newDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tmp = new Item(title, txt, d);
            if (status.equals(Item.Status.DONE.toString()))
                tmp.setStatus(Item.Status.DONE);
            else
                tmp.setStatus(Item.Status.TODO);
            tmp.setCategorie(cat);
            list.add(tmp);
            count++;
            resultSet.moveToNext();
        }
        items = list;
    }


    public void getCatData() {
        ArrayList<Categorie> list = new ArrayList<>();
        Categorie tmp;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cats(Name VARCHAR, Color VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from cats", null);
        resultSet.moveToFirst();
        int count = 0;
        while (count < resultSet.getCount()) {
            String name = resultSet.getString(resultSet.getColumnIndex("Name"));
            String color = resultSet.getString(resultSet.getColumnIndex("Color"));
            tmp = new Categorie(name, Integer.parseInt(color));
            list.add(tmp);
            count++;
            resultSet.moveToNext();
        }
        cat = list;
    }


    public String addToDataBase(int i) {
        Item tmp = items.get(i);
        String query = "'";
        query += tmp.getTitle() + "','";
        query += tmp.getDate() + tmp.getTime() + "','";
        query += tmp.getStatus().toString() + "','";
        query += tmp.getText() + "','";
        query += tmp.getCategorie() + "'";
        return query;
    }

    public void saveData() {
        String query;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE IF EXISTS tasks");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(Titre VARCHAR, Date VARCHAR, Status VARCHAR, Txt VARCHAR, Cat VARCHAR);");

        for (int i = 0; i < items.size(); i++) {
            query = addToDataBase(i);
            mydatabase.execSQL("INSERT INTO tasks VALUES(" + query + ");");
        }
    }

    public void saveCategory() {
        String query;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE IF EXISTS cats");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cats(Name VARCHAR, Color VARCHAR);");
        for (int i = 0; i < cat.size(); i++) {
            query = "'" + cat.get(i).getName() + "','" +  String.valueOf(cat.get(i).getColor()) + "'";
            mydatabase.execSQL("INSERT INTO cats VALUES(" + query + ");");
        }
    }


    public void settings(View V) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void add(View v) {
        Intent intentMain = new Intent(MainActivity.this, AddItem.class);
        startActivityForResult(intentMain, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                String txt = data.getStringExtra("txt");
                String date = data.getStringExtra("date");
                String delete = data.getStringExtra("delete");
                String category = data.getStringExtra("categorie");
                SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyy k:m");
                Date d = null;
                try {
                    d = newDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (data.getStringExtra("edit").equals("true")) {
                    int position = Integer.parseInt(data.getStringExtra("position"));
                    try {
                        modifyItem(position, title, txt, d, delete, category);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Item newItem = new Item(title, txt, d);
                    newItem.setCategorie(category);
                    try {
                        addToList(newItem);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //here goes nothing
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                saveCategory();
                checkCategories();
                affListCorresponding();
                ((checkAdapter) checkListView.getAdapter()).notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                saveCategory();
                checkCategories();
                affListCorresponding();
                ((checkAdapter) checkListView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    public void checkDate() {
        int i = 0;
        Date d;

        d = new Date();
        nb_tasks.setText(String.valueOf(items.size()) + " Tasks");
        while (i < items.size()) {
            if (!(items.get(i).getRealDate().after(d))) {
                //items.get(i).setPassed(true);
                items.get(i).setDateColor("#FF0000");
            } else {
                //items.get(i).setPassed(false);
                items.get(i).setDateColor("#121212");
            }
            i++;
        }
    }

    public void addToList(Item item) throws ParseException {
        items.add(item);
        checkDate();
        saveData();
        Date f = new Date();
        int c = 0;
        int color = Color.BLUE;
        while (c < cat.size()) {
            if (item.getCategorie().equals(cat.get(c).getName())) {
                color = cat.get(c).getColor();
            }
            c++;
        }
        int delay = (int) (item.getRealDate().getTime() - f.getTime());
        if (delay > 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                scheduleNotification(getNotification(item.getTitle(), item.getText(), color), delay);
            }
        affListCorresponding();
    }


    public void modifyItem(int position, String title, String txt, Date d, String delete, String cate) throws ParseException {
        Item item = items.get(position);
        if (delete.equals("false")) {
            item.setTitle(title);
            item.setText(txt);
            item.setDueDate(d);
            item.setCategorie(cate);
        } else
            items.remove(item);
        checkDate();
        saveData();
        Date f = new Date();
        int delay = (int) (d.getTime() - f.getTime());
        int color = Color.BLUE;
        int c = 0;
        while (c < cat.size()) {
            if (item.getCategorie().equals(cat.get(c).getName())) {
                color = cat.get(c).getColor();
            }
            c++;
        }
        if (delay > 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                scheduleNotification(getNotification(title, txt, color), delay);
            }
        affListCorresponding();
    }



    public boolean showCatForItem(Item item) {
        int i = 0;
        while (i < cat.size()) {
            if (cat.get(i).getName().equals(item.getCategorie())) {
                return cat.get(i).getShow();
            }
            i++;
        }
        return false;
    }


    public void affListCorresponding() {
        int nb_items = items.size();

        int i = 0;
        tmp.clear();
        while (i < nb_items) {

            if (showCatForItem(items.get(i)))
                tmp.add(items.get(i));
            i++;
        }
        ItemAdapter adapter = new ItemAdapter(MainActivity.this, tmp);
        mListView.setAdapter(adapter);
        if (tmp.size() > 1)
            ((TextView) findViewById(R.id.nb_tasks)).setText(String.valueOf(tmp.size()) + " Tasks");
        else
            ((TextView) findViewById(R.id.nb_tasks)).setText(String.valueOf(tmp.size()) + " Task");
        adapter.notifyDataSetChanged();
    }


    public void catCheck(View v) {
        final int position = checkListView.getPositionForView((View) v.getParent());
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked())
            cat.get(position).setShow(true);
        else
            cat.get(position).setShow(false);
        affListCorresponding();
    }


    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }


    private Notification getNotification(String Title, String content, int color) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationPublisher.NOTIFICATION_ID);
            builder.setContentTitle(Title);
            builder.setContentText(content);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setAutoCancel(true);
            builder.setVibrate(new long[]{100, 200, 300, 400, 300, 200, 400});

            builder.setColor(color);

        affListCorresponding();
            return builder.build();
    }

    public static ArrayList<Categorie> getCatA() {
        ArrayList<Categorie> tmp = new ArrayList<Categorie>();
        int i = 0;
        while (i < cat.size())
            tmp.add(cat.get(i++));
        return tmp;
    }


    public void closeMenu(View v) {
        DrawerLayout d = ((DrawerLayout) findViewById(R.id.drawer_layout));
        d.closeDrawers();
    }

    public static ArrayList<Categorie> getCat() {
        return (getCatA());
    }


    public void checkCategories() {
        int i = 0;

        while (i < items.size()) {
            int c = 0;
            boolean found = false;
            while (c < cat.size()) {
                if (items.get(i).getCategorie().equals(cat.get(c).getName()))
                    found = true;
                c++;
            }
            if (!found)
                items.get(i).setCategorie("none");
            i++;
        }
        affListCorresponding();
    }

    public void addCategorie(View v) {
        Intent intentMain = new Intent(MainActivity.this, addCategory.class);
        startActivityForResult(intentMain, 2);
        checkCategories();
    }
}
