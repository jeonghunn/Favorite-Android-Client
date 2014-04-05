package com.tarks.favorite.cardsui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tarks.favorite.R;
import com.tarks.favorite.cardsui.objects.Card;

public class profile_card extends Card {

	public profile_card(String title, String des, int user_srl, Boolean isClickable) {
		super(title, des, user_srl, isClickable);
	}

	@Override
	public View getCardContent(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.profile_list, null);
		
		((TextView) v.findViewById(R.id.title)).setText(title);
		((TextView) v.findViewById(R.id.description)).setText(des);

		
		if (isClickable == true)
			((LinearLayout) v.findViewById(R.id.contentLayout))
					.setBackgroundResource(R.drawable.selectable_background_cardbank);


	
		return v;
	}

}
