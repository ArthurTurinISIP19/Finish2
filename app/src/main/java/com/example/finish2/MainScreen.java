package com.example.finish2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    String cat = "Foods";
    Toolbar toolbar;
    Animation animation;
    ListView listSearchView;
    EditText searchRequest;
    RadioGroup radioGroupCategories;
    RadioButton currentRadioButton;
    ArrayList<String> listSearch;
    ArrayAdapter<String> list_adapter;
    MenuItemAdapter item_adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        toolbar = (Toolbar) findViewById(R.id.search_bar);
        listSearchView = (ListView) findViewById(R.id.search_list);
        searchRequest = (EditText) findViewById(R.id.search_bar_edit_text);
        radioGroupCategories = (RadioGroup) findViewById(R.id.categories_button_view);
        recyclerView = findViewById(R.id.list);

        getDishesData();

        searchRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories);
                searchInMenuItemAndUpdateSearchList(menuItems, searchRequest.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        listSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                searchRequest.setText(listSearchView.getItemAtPosition(position).toString());

            }
        });
    }

    private void getDishesData() {
        String baseUrl = "https://food.madskill.ru/dishes?version=1.01";

        JSONArray json = new JSONArray();
        try {
            json.put(new JSONObject().put("version", 1.01));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(MainScreen.this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, baseUrl, null,
                response ->
                {
                    addDishesData(response);
                    createRecyclerView();
                },
                error -> Toast.makeText(this, "Не удалось получить данные с сервера.", Toast.LENGTH_LONG).show());
        requestQueue.add(request);
    }

    private void addDishesData(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();

        String nameDish = "";
        String iconResource = "";
        int price = 0;
        String category = "";

        String baseImageUrl = "https://food.madskill.ru/up/images/";

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                nameDish = jsonObject.getString("nameDish");
                iconResource = jsonObject.getString("icon");
                price = Integer.parseInt(jsonObject.getString("price"));
                category = jsonObject.getString("category");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            menuItems.add(new MenuItem(nameDish, price, baseImageUrl.concat(iconResource), category));
        }
        int i =0;
        while (i < menuItems.size())
        {
            if (!menuItems.get(i).getCategory().equals(cat))
            {
                menuItems.remove(i);
            }
            else
            {
                i++;
            }
        }
    }

    private void createRecyclerView()
    {
        MenuItemAdapter adapter = new MenuItemAdapter(this, menuItems);
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.swapAdapter(adapter, false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter.notifyDataSetChanged();
    }
    private void updateSearchListOnCategoriesInMenuItem(ArrayList<MenuItem> menuItems, RadioGroup radioGroupCategories)
    {
        currentRadioButton = (RadioButton) findViewById(radioGroupCategories.getCheckedRadioButtonId());
        listSearch = new ArrayList<>();
        for (int j = 0; j < menuItems.size(); j++) {
            listSearch.add(menuItems.get(j).getName());
        }
        updateListView();
    }
    private void searchInMenuItemAndUpdateSearchList(ArrayList<MenuItem> menuItems, String searchRequest) {
        int j = 0;
        for (int k = 0; k < menuItems.size(); k++) {

            if (!menuItems.get(k).getName().toLowerCase().contains(searchRequest.toLowerCase())) {
                listSearch.remove(j);
            } else {
                j++;
            }
        }
        updateMenuView();
    }


    private void updateMenuView() {
        item_adapter = new MenuItemAdapter(this, menuItems);
        recyclerView.setAdapter(item_adapter);
    }
    private void updateListView() {
        list_adapter = new ArrayAdapter<String>(MainScreen.this, android.R.layout.simple_list_item_1, listSearch);
        listSearchView.setAdapter(list_adapter);
    }


    public void search(View view) {
        int i = 0;
        while (i < menuItems.size()) {
            if (!menuItems.get(i).getName().toLowerCase().contains(searchRequest.getText().toString().toLowerCase())) {
                menuItems.remove(i);
            } else {
                i++;
            }
        }
        createRecyclerView();
        closeSearchBar(view);
    }

    public void openSearchBar(View view) {
        toolbar.setVisibility(View.VISIBLE);
        searchAnimationOpen();
    }

    private void searchAnimationOpen() {

        ConstraintLayout search_bar = (ConstraintLayout) findViewById(R.id.searchConstraintLayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.search_bar_animation);
        search_bar.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(this, R.anim.search_list_animation);
        listSearchView.startAnimation(animation);

    }

    private void searchAnimationClose() {
        ConstraintLayout search_bar = (ConstraintLayout) findViewById(R.id.searchConstraintLayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.search_bar_animation_reverve);
        search_bar.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(this, R.anim.search_list_animation_reverce);
        listSearchView.startAnimation(animation);
    }

    public void closeSearchBar(View view) {
        searchAnimationClose();
        animation = AnimationUtils.loadAnimation(this, R.anim.no_anim_duration);
        toolbar.startAnimation(animation);
        toolbar.setVisibility(View.GONE);
    }

    public void foods(View view) {
        cat = "Foods";
        getDishesData();
    }

    public void drinks(View view) {
        cat = "Drinks";
        getDishesData();

    }

    public void snacks(View view) {
        cat = "Snacks";
        getDishesData();

    }

    public void sauce(View view) {
        cat = "Sauce";
        getDishesData();

    }


}