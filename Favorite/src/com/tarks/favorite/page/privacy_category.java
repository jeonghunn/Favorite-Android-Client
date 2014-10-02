//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.page;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.R;
import com.tarks.favorite.global.Global;

public class privacy_category extends Activity {

	// ListView
	ListView listView;
	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;

	int selected_kind;
	int selected_tag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		// 액션바백버튼가져오기
	//	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setDisplayShowHomeEnabled(false);

		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		String status = intent.getStringExtra("status");
		String title = intent.getStringExtra("title");
		String kind = intent.getStringExtra("kind");

		if (title != null)
			setTitle(title);
		selected_kind = 1;
		
		try{
		selected_tag = Integer.parseInt(status);
		}catch (Exception e){
			
		}
		if (kind == null) {
			setList(getString(R.string.privacy_public),
					getString(R.string.privacy_public_des), 1, 0);
			// setList(getString(R.string.privacy_users),
			// getString(R.string.privacy_users_des), 1, 1 );
			// setList(getString(R.string.privacy_relation),
			// getString(R.string.privacy_relation_des), 1, 2 );
			setList(getString(R.string.privacy_favorites),
					getString(R.string.privacy_favorites_des), 1, 3);
			setList(getString(R.string.privacy_justme),
					getString(R.string.privacy_justme_des), 1, 4);
		}

		if(kind != null){
		if (kind.matches("write_permission")) {
			setList(getString(R.string.wr_permission_public),
					getString(R.string.wr_permission_public_des), 1, 0);
			 setList(getString(R.string.wr_permission_users),
			 getString(R.string.wr_permission_users_des), 1, 1 );
			 setList(getString(R.string.wr_permission_relation),
			getString(R.string.wr_permission_relation_des), 1, 2 );
			setList(getString(R.string.wr_permission_favorites),
					getString(R.string.wr_permission_favorites_des), 1, 3);
			setList(getString(R.string.wr_permission_justme),
					getString(R.string.wr_permission_justme_des), 1, 4);
		}
		
		}

		// setList(getString(R.string.add_with_anonymous),
		// getString(R.string.add_with_anonymous_des), 0);
		setListAdapter();
	}

	public void setList(String title, String des, int Kind, int Tag) {
		List p1 = new List(title, des, Kind, Tag);
		m_orders.add(p1);

	}

	public void setListAdapter() {
		listView = (ListView) findViewById(R.id.listView1);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				ListAdapter ca = (ListAdapter) arg0.getAdapter();
				List ls = (List) ca.getItem(arg2);
				Intent intent = new Intent();
				intent.putExtra("status", String.valueOf(ls.getTag()));
				privacy_category.this.setResult(RESULT_OK, intent);
				finish();

			}
		});

		// listView.setOnScrollListener(this);
		m_adapter = new ListAdapter(this, R.layout.list2, m_orders);

		listView.setAdapter(m_adapter);

	}

	private class ListAdapter extends ArrayAdapter<List> {

		private ArrayList<List> items;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<List> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			final List p = items.get(position);
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list2, null);

				if (selected_kind == p.getKind() && selected_tag == p.getTag())
					v.setBackgroundColor(Color.parseColor("#50ABABAB"));
			}

			if (p != null) {

				// LinearLayout layout = (LinearLayout)
				// v.findViewById(R.id.layoutback);
				TextView tt = (TextView) v.findViewById(R.id.title);
				TextView bt = (TextView) v.findViewById(R.id.description);
				ImageView image = (ImageView) v.findViewById(R.id.img);

				if (tt != null) {
					tt.setText(p.getTitle());
				}
				if (bt != null) {
					bt.setText(Global.getValue(p.getDes()));
				}

			}
			return v;
		}
	}

	class List {

		private String Title;
		private String Description;
		private int Tag;
		private int Kind;

		public List(String _Title, String _Description, int _Kind, int _Tag) {

			this.Title = _Title;
			this.Description = _Description;
			this.Kind = _Kind;
			this.Tag = _Tag;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		public int getKind() {
			return Kind;
		}

		public int getTag() {
			return Tag;
		}

	}

//	// 빽백키 상단액션바
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			onBackPressed();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//
//	}

}
