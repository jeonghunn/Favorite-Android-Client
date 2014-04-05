package com.tarks.favorite;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.ProfileActivity;
import com.tarks.favorite.page.document_write;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.view.GravityCompat;

public class main extends SherlockFragmentActivity {

	// Declare Variables
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String[] title;
	//String[] subtitle;
	int[] icon;
	Fragment fragment1 = new mainfragment();
	Fragment fragment2 = new Fragment2();
	Fragment fragment3 = new Fragment3();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml
		setContentView(R.layout.main);
		
		

		// Get the Title
		mTitle = mDrawerTitle = getTitle();

		// Generate title
		title = new String[] { Global.NameMaker(getString(R.string.lang), Global.getSetting("name_1", ""), Global.getSetting("name_2", "")),getString(R.string.home), "Title Fragment 2",
				getString(R.string.setting) };

		// Generate subtitle
		//subtitle = new String[] { "Subtitle Fragment 1", "Subtitle Fragment 2",
		//		"Subtitle Fragment 3" };
		// Generate icon
		icon = new int[] {R.drawable.drawer_profile , R.drawable.home , R.drawable.settings,
				R.drawable.settings };

	
		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);
		
	

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass string arrays to MenuListAdapter
		mMenuAdapter = new MenuListAdapter(main.this, title, icon);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);
		//Background Color
			//	mDrawerLayout.setBackgroundResource(Color.parseColor("#dc7727"));

		// Capture listview menu item click
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				//getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}
	



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position == 0){
				Intent intent = new Intent(main.this, ProfileActivity.class);
				  intent.putExtra("member_srl", Global.getSetting("user_srl", "0"));
				startActivity(intent);	
			}else selectItem(position);
			
			
		}
	}

	private void selectItem(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, fragment1);
			break;
		case 1:
			ft.replace(R.id.content_frame, fragment1);
			break;
		case 2:
//			Intent intent = new Intent(main.this, ProfileActivity.class);
//			startActivity(intent);
			break;
		case 3:
			Intent intent = new Intent(main.this, setting.class);
			startActivity(intent);

			break;
		}
		ft.commit();
		mDrawerList.setItemChecked(position, true);

		// Get the title followed by the position
		//setTitle(title[position]);
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

//	@Override
//	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getSupportActionBar().setTitle(mTitle);
//	}
//	
}
