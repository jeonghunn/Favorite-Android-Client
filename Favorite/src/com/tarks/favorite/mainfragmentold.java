package com.tarks.favorite;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockFragment;
import com.tarks.favorite.cardsui.MyPlayCard;
import com.tarks.favorite.cardsui.like_card;
import com.tarks.favorite.cardsui.profile_card;
import com.tarks.favorite.cardsui.views.CardUI;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.document_write;
import com.tarks.favorite.test.phone_number;
import com.tarks.widget.listviewutil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class mainfragmentold extends SherlockFragment implements
		OnItemLongClickListener {

	private ListView maListViewPerso;
	private ScrollView sv;
	// 카드 유아이 정의한다.
	CardUI mCardView;

	View rootView;

	// On déclare la HashMap qui contiendra les informations pour un item
	HashMap<String, String> map;

	// Like me or favorite
	String like_me_result;
	String favorite_result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.mainfragment, container, false);

		// Set Favorite
		setfavorite();
		// Import ListView

		setCard();

		return rootView;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setfavorite() {
		// 자신의 신분 설정값을 불러옵니다.
		SharedPreferences prefs = getActivity().getSharedPreferences("setting",
				getActivity().MODE_PRIVATE);
		int like_me = Integer.parseInt(prefs.getString("like_me", "0"));
		int favorite = Integer.parseInt(prefs.getString("favorite", "0"));

		// Import textview
		TextView favorite_text = (TextView) rootView
				.findViewById(R.id.textView4);
		TextView like_me_text = (TextView) rootView
				.findViewById(R.id.textView3);

		// number cut
		NumberFormat nf = NumberFormat.getInstance();
		// nf.setMaximumIntegerDigits(5); //최대수 지정
		like_me_result = nf.format(like_me);
		favorite_result = nf.format(favorite);

		if (like_me > 9999)
			like_me_result = "9999+";
		if (favorite > 9999)
			favorite_result = "9999+";
		
		loadFavorite(Global.getSetting("user_srl", "0"));

	}

	public void setCard() { 
		// 카드 인터페이스를 불러온다.
		// init CardView
		mCardView = (CardUI) rootView.findViewById(R.id.cardsview);
		// mCardView.setSwipeable(true);
		
		setheader();
		addList();
		addList();
		addList();
		// MyPlayCard notice_banner = (new MyPlayCard(
		// getString(R.string.notice), "하하 저를 눌러보고 싶지 않아요?d", "#33b6ea" ));
		//
		// mCardView.addCard(notice_banner);
		//

		// draw cards
		mCardView.refresh();
	}
	
	public void setheader() {
		like_card header = (new like_card(like_me_result,favorite_result ,
				false));

		mCardView.addCard(header);
	}
	
	public void addList() {
		profile_card profile = (new profile_card("이정훈","샘숭샘", 1 ,
				false));

		mCardView.addCard(profile);
	}
	
	public void loadFavorite(String user_srl) {
		

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("category");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("value");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("2");
		Paramvalue.add("3");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(user_srl);

		new AsyncHttpTask(getActivity(), getString(R.string.server_path)
				+ "favorite/favorite_app_read.php", mHandler, Paramname,
				Paramvalue, null, 1, Integer.parseInt(user_srl));
	
}
	
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout
		//	rootView.setSupportProgressBarIndeterminateVisibility(false);
			if (msg.what == -1) {
				Global.ConnectionError(getActivity());
			}

			if (msg.what == 1) {
				String result = msg.obj.toString();
				if(result.matches("document_write_succeed")) {
			//	FinishAct();
				}else{
			//		Global.ConnectionError(document_write.this);
				}
				Log.i("Result","로그 정상 작동");
				Log.i("Result", msg.obj.toString());
			
			}

		}
	};

}
