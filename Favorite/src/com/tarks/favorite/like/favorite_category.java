package com.tarks.favorite.like;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.tarks.favorite.R;
import com.tarks.favorite.global.Global;

public class favorite_category extends Activity {

	// ListView
		ListView listView;
	// List
		ArrayList<List> m_orders = new ArrayList<List>();
		// Define ListAdapter
		ListAdapter m_adapter;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
      //액션바백버튼가져오기
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
       
    
       // setList(getString(R.string.add), getString(R.string.add_des), 0);
      //  setList(getString(R.string.add_with_anonymous), getString(R.string.add_with_anonymous_des), 0);
      //  setListAdapter();
    }
    
    public void setList(String title, String des, int Tag){
    	List p1 = new List( title, des, Tag);
		m_orders.add(p1);
		
    }
    
	public void setListAdapter() {
		listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg0.getAdapter() instanceof HeaderViewListAdapter) {
					if (((HeaderViewListAdapter) arg0.getAdapter())
							.getWrappedAdapter() instanceof ListAdapter) {
						ListAdapter ca = (ListAdapter) ((HeaderViewListAdapter) arg0
								.getAdapter()).getWrappedAdapter();

//						List ls = (List) ca.getItem(arg2 - 1);
//
//						Intent intent = new Intent(ProfileActivity.this,
//								document_read.class);
//						intent.putExtra("doc_srl",
//								String.valueOf(ls.getDocSrl()));
//						startActivity(intent);

					}
				}

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
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list2, null);
			}
			final List p = items.get(position);
			if (p != null) {
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

		public List(String _Title, String _Description,
				int _Tag) {
			
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
	}

	

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		public int getTag() {
			return Tag;
		}

		

	}

	
}
