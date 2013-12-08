/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import com.tarks.favorite.test.phone_number;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ListFragment extends Fragment implements OnItemLongClickListener {


	private static final String ARG_POSITION = "position";
	
	private ListView maListViewPerso;
	
	// On déclare la HashMap qui contiendra les informations pour un item
	HashMap<String, String> map;

	private int position;
	String make_class;
	// 년,월,일
	int year;
	int month;
	int day;

	public static ListFragment newInstance(int position) {
		ListFragment f = new ListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View fl = inflater.inflate(R.layout.textlistview, container, false);

		//Import ListView
		 
		maListViewPerso = (ListView) fl.findViewById(R.id.listView1);

		// Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();


		String[] weeks = { "a", "b", "c", "d", "e", "f", "g" };
		// 날짜 불러오기
		Calendar m = Calendar.getInstance();

		year = m.get(Calendar.YEAR);
		month = m.get(Calendar.MONTH) + 1;
		day = m.get(Calendar.DATE);
		
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.phone_number));
		map.put("n", "0");
		map.put("des", getString(R.string.phone_number_des));
		map.put("img", String.valueOf(R.drawable.dial_dark));
		listItem.add(map);  
		
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.birthday));
		map.put("n", "1");
		map.put("des", getString(R.string.birthday_des));
		map.put("img", String.valueOf(R.drawable.calendar_dark));
		listItem.add(map);  
        
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.location));
		map.put("n", "2");
		map.put("des", getString(R.string.location_des));
		map.put("img", String.valueOf(R.drawable.map_dark));
		listItem.add(map);  
		
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.school));
		map.put("n", "3");
		map.put("des", getString(R.string.school_des));
		map.put("img", String.valueOf(R.drawable.edit_dark));
		listItem.add(map);  
		
	
    		
			
    		
    		
		
		// Création d'un SimpleAdapter qui se chargera de mettre les items
		// présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(getActivity().getBaseContext(),
				listItem, R.layout.list2, new String[] { "img", "title",
						"des" }, new int[] { R.id.img, R.id.titre,
						R.id.description });

		// On attribut à notre listView l'adapter que l'on vient de créer
		maListViewPerso.setAdapter(mSchedule);

		// 리스트 길게 클릭하면 리스트 삭제하기 위해 롱 클릭 이벤트
		maListViewPerso.setOnItemLongClickListener(this);
		// Enfin on met un écouteur d'évènement sur notre listView
		maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				HashMap<String, String> map = (HashMap<String, String>) maListViewPerso
						.getItemAtPosition(position);

				if (map.get("n").matches("0")){
					//Find by Phone number
					  Intent intent = new Intent(getActivity(), phone_number.class);
			 	    	 startActivity(intent); 
				}
			
				
							}
		});
		
		
		
		return fl;

		}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	


}