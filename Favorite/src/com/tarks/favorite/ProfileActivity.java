/*
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tarks.favorite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.manuelpeinado.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import com.tarks.favorite.global.Global;

public class ProfileActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FadingActionBarHelper helper = new FadingActionBarHelper()
           .actionBarBackground(R.drawable.ab_background)
            .headerLayout(R.layout.profile)
            .contentLayout(R.layout.activity_listview);
        setContentView(helper.createView(this));
        helper.initActionBar(this);
        
        getSupportActionBar().setTitle( Global.NameMaker(Global.getSetting("name_1", ""), Global.getSetting("name_2", "")));
        
        ImageView profile = (ImageView) findViewById(R.id.image_header);
        profile.setImageDrawable(Drawable.createFromPath( getCacheDir().toString() + "/profile.jpg"));

        ListView listView = (ListView) findViewById(android.R.id.list);
      ArrayList<String> items = new ArrayList<String>();
      
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
      items.add("Once upon a midnight dreary");
      items.add("While I pondered weak and weary");
      items.add("Over many a quaint and curious volume of forgotten lore");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    /**
     * @return A list of Strings read from the specified resource
     */
    private ArrayList<String> loadItems(int rawResourceId) {
        try {
            ArrayList<String> countries = new ArrayList<String>();
            InputStream inputStream = getResources().openRawResource(rawResourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                countries.add(line);
            }
            reader.close();
            return countries;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
   //     getSupportMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
}
