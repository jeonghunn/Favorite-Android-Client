//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite;

import java.lang.reflect.Field;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.ProfileActivity;
import com.tarks.favorite.page.document_write;
import com.tarks.favorite.page.page_create;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.view.KeyEvent;
import android.view.View;
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
	int fragment_position;
	//String[] subtitle;
	int[] icon;
	Fragment fragment1 = new mainfragment();
	Fragment fragment2 = new PagePopularFragment();
	
	Fragment contacts_fragment = new contacts_fragment();
	Fragment no_favorite = new no_favorite_fragment();
	//Fragment fragment3 = new Fragment3();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private Menu optionsMenu;
	
	int NowPosition;
	
	String user_name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml
		setContentView(R.layout.main);
		

		// Get the Title
		mTitle = mDrawerTitle = getTitle();
	user_name = Global.NameMaker(getString(R.string.lang), Global.getSetting("name_1", ""), Global.getSetting("name_2", ""));
		// Generate title
		title = new String[] { user_name ,getString(R.string.favorites), getString(R.string.contacts), getString(R.string.pages),
				 getString(R.string.create_page),  getString(R.string.setting), };

		// Generate subtitle
		//subtitle = new String[] { "Subtitle Fragment 1", "Subtitle Fragment 2",
		//		"Subtitle Fragment 3" };
		// Generate icon
		icon = new int[] {R.drawable.drawer_profile , R.drawable.home ,R.drawable.people_white, R.drawable.ic_list,
				R.drawable.add  ,R.drawable.settings };

	
		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		//Drag margin
		Field mDragger;
		try {
			mDragger = mDrawerLayout.getClass().getDeclaredField(
			        "mLeftDragger");
		
		mDragger.setAccessible(true);
		ViewDragHelper draggerObj = (ViewDragHelper) mDragger
		        .get(mDrawerLayout);

		Field mEdgeSize = draggerObj.getClass().getDeclaredField(
		        "mEdgeSize");
		mEdgeSize.setAccessible(true);
		int edge = mEdgeSize.getInt(draggerObj);

		mEdgeSize.setInt(draggerObj, edge * 2);

		
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//mRightDragger for right obviously
 catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);
		
	

		// Set a custom shadow that overlays the main content when the drawer
		// opens
//		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
//				GravityCompat.START);

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
		 getSupportActionBar().setDisplayShowHomeEnabled(false);
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
			selectItem(1);
		}
		
		
	
	}
	
	public void ChangeUserAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(
				main.this);
		builder.setMessage(getString(R.string.change_user_default_des)).setTitle(
				getString(R.string.change_user));
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					ChangeUserAct();
					}
				});
		builder.setNegativeButton(getString(R.string.no), null);
		builder.show();
	
	}
	
	public void ChangeUserAct(){
		// Setting Editor
					SharedPreferences edit = getSharedPreferences("setting",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = edit.edit();
					editor.putString("default_user", "Y");
					editor.putString("user_srl", Global.getSetting("default_user_srl", "0"));
					editor.putString("user_srl_auth", Global.getSetting("default_user_srl_auth", "null"));

					//Commit
					editor.commit();
					
					MainActivity.INSTANCE.restartApplication();		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.optionsMenu = menu;
		MenuItem item;


		menu.add(0, 1, 0, getString(R.string.create_page)).setIcon(R.drawable.add)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
//		menu.add(0, 2, 0, getString(R.string.search)).setIcon(R.drawable.search)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0, 0, 0, getString(R.string.write)).setIcon(R.drawable.write)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		

		menu.add(0, 100, 0, getString(R.string.change_user))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		
		menu.add(0, 200, 0, getString(R.string.invite))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.findItem(0).setVisible(fragment_position == 1 || fragment_position != 2);
		menu.findItem(1).setVisible(fragment_position == 3);
		
		//User change
		menu.findItem(100).setVisible(Global.getSetting("default_user", "Y").matches("N"));
		
		//Invite
		menu.findItem(200).setVisible(fragment_position == 2);

		// item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
		// item.setIcon(R.drawable.ic_menu_add_bookmark);

		return true;
	}

	public void Drawer(){
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home) {
			Drawer();
		
		}
		
		if (item.getItemId() == 0) {
			Intent intent1 = new Intent(main.this,
					document_write.class);
			intent1.putExtra("page_srl",Global.getSetting("user_srl", "0"));
			intent1.putExtra("page_name",user_name);
			startActivityForResult(intent1, 1);
			return true;
	
		}
		
		if (item.getItemId() == 1) {
			Intent intent1 = new Intent(main.this, page_create.class);
			startActivityForResult(intent1, 1);
			return true;
	
		}
		
		
		if (item.getItemId() == 100) {
			try{
			ChangeUserAlert();
			}catch (Exception e){ System.exit(0);}
		}
		
		if (item.getItemId() == 200) {
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("text/plain");
			share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invite));
			share.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_message));
			startActivity(Intent.createChooser(share,
					getString(R.string.invite)));
			return true;
	
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

	public void selectItem(int position) {
		fragment_position = position;
		invalidateOptionsMenu();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		switch (position) {
		case 0:
			//ft.remove(fragment1);
			ft.replace(R.id.content_frame, fragment1);
			break;
		case 1:
			setTitle(getString(R.string.my_favorites));
			if(!Global.getSetting("favorite", "0").matches("0")){
			ft.replace(R.id.content_frame, fragment1);
			}else{
				ft.replace(R.id.content_frame, no_favorite);
			}
		//((mainfragment) fragment1).refreshAct();
			break;
		case 2:
			setTitle(getString(R.string.contacts));
			ft.replace(R.id.content_frame, contacts_fragment);
			break;
		case 3:
			setTitle(getString(R.string.popularity));
			ft.replace(R.id.content_frame, fragment2);
			break;
		case 4:
			Intent intent = new Intent(main.this, page_create.class);
			startActivity(intent);
			break;
		case 5:
			Intent intent1 = new Intent(main.this, setting.class);
			startActivity(intent1);

			break;
		}
		ft.commit();
		mDrawerList.setItemChecked(position, true);
        NowPosition = position;
		// Get the title followed by the position
		//setTitle(title[position]);
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	/**
	 * Restart the application.
	 */
	public void restartApplication() {
		PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(),
				0, new Intent(getIntent()), getIntent().getFlags());
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
		System.exit(2);
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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(main.this, ProfileActivity.class);
			  intent.putExtra("member_srl", Global.getSetting("user_srl", "0"));
			startActivity(intent);	
		}

	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_MENU == keyCode){
			Drawer();
			//TODO
		}
		return super.onKeyDown(keyCode, event);
		
	}
	
	@Override
	public void onBackPressed() {
		if(NowPosition > 1){
			selectItem(1);
		}else{
			this.moveTaskToBack(true);
		}
		
	}

//	@Override
//	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getSupportActionBar().setTitle(mTitle);
//	}
//	
}
