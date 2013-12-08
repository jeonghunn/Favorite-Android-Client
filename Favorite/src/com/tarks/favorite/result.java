package com.tarks.favorite;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.tarks.favorite.test.phone_number;

public class result extends SherlockActivity implements OnItemLongClickListener {
	private ListView maListViewPerso;
	PackageInfo pi;
	String name;
	String asky = "";
	String result_value;
	//
	String who_favorited_me;
	String check_favorite_me;
	String people;
	String value;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent(); // 인텐트 받아오고
		String info = intent.getStringExtra("info"); // 인텐트로 부터 데이터 가져오고
		String kind = intent.getStringExtra("kind"); // 인텐트로 부터 데이터 가져오고

		// 설정 값 불러오기
		SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
		String name_1 = prefs.getString("name_1", "");
		String name_2 = prefs.getString("name_2", "");

		// Cut

		StringTokenizer st = new StringTokenizer(info, "*DIVIDE*");
		String users_srl = st.nextToken();
		final String people = st.nextToken();

		// Name
		if (getString(R.string.lang).matches("ko")) {
			name = name_1 + name_2;
		} else {
			name = name_2 + " " + name_1;
		}

		// make value des
		Resources res = getResources();
		value = kind + " - " + String.format(res.getString(R.string.test_result), name,
				people);

		// ASKY
		if (people.matches("0")) {
			asky = getString(R.string.asky);
		}

		// Result Value
		try {
			result_value = URLEncoder.encode(value + " " + asky, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// URLEncode
		try {
			who_favorited_me = URLEncoder.encode(
					getString(R.string.who_favorited_me), "UTF-8");
			check_favorite_me = URLEncoder.encode(
					getString(R.string.check_how_many_people_are_favorite_me),
					"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		maListViewPerso = (ListView) findViewById(R.id.listView1);

		// Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

		// On déclare la HashMap qui contiendra les informations pour un item
		HashMap<String, String> map;

		// PAckage manager
		PackageManager pm = getPackageManager();

		// KAKAO story
		try {

			pi = pm.getPackageInfo("com.kakao.story",
					PackageManager.GET_ACTIVITIES);
			// add list
			map = new HashMap<String, String>();
			map.put("title", getString(R.string.check_with_kakaostory));
			map.put("n", "1");
			// map.put("des", "카카오스토리로 결과를 확인합니다.");
			map.put("img", String.valueOf(R.drawable.kakaostory));
			listItem.add(map);
		} catch (PackageManager.NameNotFoundException e) {

		}

		// KAKAO TALK
		try {

			pi = pm.getPackageInfo("com.kakao.talk",
					PackageManager.GET_ACTIVITIES);
			// add list
			map = new HashMap<String, String>();
			map.put("title", getString(R.string.check_with_kakaotalk));
			map.put("n", "2");
			// map.put("des", "카카오톡으로 결과를 확인합니다.");
			map.put("img", String.valueOf(R.drawable.kakaotalk));
			listItem.add(map);
		} catch (PackageManager.NameNotFoundException e) {

		}

		// FACEBOOK
		// add list
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.check_with_facebook));
		map.put("n", "3");
		// map.put("des", "카카오톡으로 결과를 확인합니다.");
		map.put("img", String.valueOf(R.drawable.facebook));
		listItem.add(map);

		// Twitter
		// add list
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.check_with_twitter));
		map.put("n", "4");
		// map.put("des", "카카오톡으로 결과를 확인합니다.");
		map.put("img", String.valueOf(R.drawable.twitter));
		listItem.add(map);

		// View self
		// add list
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.check_result));
		map.put("n", "5");
		// map.put("des", "카카오톡으로 결과를 확인합니다.");
		map.put("img", String.valueOf(R.drawable.check));
		listItem.add(map);

		// View self

		// peek
		// add list
		map = new HashMap<String, String>();
		map.put("title", getString(R.string.peek));
		map.put("n", "6");
		map.put("des", getString(R.string.peek_des));
		map.put("img", String.valueOf(R.drawable.eye));
		listItem.add(map);

		// Création d'un SimpleAdapter qui se chargera de mettre les items
		// présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
				listItem, R.layout.list2,
				new String[] { "img", "title", "des" }, new int[] { R.id.img,
						R.id.titre, R.id.description });

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

				if (map.get("n").matches("1")) {
					Uri uri = Uri
							.parse("storylink://posting?post="
									+ result_value
									+ "\nhttp://favorite.tarks.net"
									+ "&appid=-&appver=1.0&appname=Favorite&urlinfo={\"title\":\""
									+ who_favorited_me
									+ "\",\"desc\":\""
									+ check_favorite_me
									+ "\",\"imageurl\":[\"http://tarks.net/app/favorite/icon_white.png\"],\"type\":\"article\"}&apiver=1.0");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}

				if (map.get("n").matches("2")) {
					Uri uri = Uri
							.parse("kakaolink://sendurl?msg="
									+ result_value
									+ "&url=http://favorite.tarks.net&appid=-&appver=1.0&appname=Favorite");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}

				if (map.get("n").matches("3")) {
					Uri uri = Uri
							.parse("http://www.facebook.com/sharer/sharer.php?m2w&s=100&p%5Btitle%5D=Favorite&p%5Bsummary%5D="
									+ result_value
									+ "&p%5Burl%5D=http://favorite.tarks.net%2F&p%5Bimages%5D%5B0%5D=http://tarks.net/app/favorite/icon_white.png");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);

				}

				if (map.get("n").matches("4")) {
					Uri uri = Uri.parse("https://twitter.com/intent/tweet?text=" + result_value + " http://favorite.tarks.net");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);

				}
				

				if (map.get("n").matches("5")) {
					 Intent intent = new Intent(result.this, result_view.class);
					  intent.putExtra("people", people);
					  intent.putExtra("kind", getString(R.string.phone_number));
					  intent.putExtra("result_value", value + "\nhttp://favorite.tarks.net");
			 	    	 startActivity(intent); 

				}

			}
		});

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		// 리턴값을 무족건 true로 해야함
		return true;
	}

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
