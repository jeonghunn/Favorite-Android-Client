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

public class like_card extends Card {

	public like_card(String like_me, String favorite, Boolean isClickable) {
		super(like_me, favorite, isClickable);
	}

	@Override
	public View getCardContent(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.like_card, null);
		
		((TextView) v.findViewById(R.id.favorite_textView)).setText(favorite);
		((TextView) v.findViewById(R.id.like_me_textView)).setText(like_me);

		
		if (isClickable == true)
			((LinearLayout) v.findViewById(R.id.contentLayout))
					.setBackgroundResource(R.drawable.selectable_background_cardbank);


	
		return v;
	}

}
