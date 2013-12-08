package com.tarks.favorite.cardsui.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tarks.favorite.R;
import com.tarks.favorite.cardsui.Utils;

public abstract class Card extends AbstractCard {

	public interface OnCardSwiped {
		public void onCardSwiped(Card card, View layout);
	}

	private OnCardSwiped onCardSwipedListener;
	private OnClickListener mListener;
	protected View mCardLayout;

	public Card() {

	}

	public Card(String title) {
		this.title = title;
	}
	
	public Card(String title, String desc) {
		this.title = title;
		this.desc = desc;
	}

	public Card(String title, int image) {
		this.title = title;
		this.image = image;
	}
	
	
	public Card(String titlePlay, String description, String color,
			String titleColor, Boolean hasOverflow, Boolean isClickable, int image) {
		this.titlePlay = titlePlay;
		this.description = description;
		this.color = color;
		this.titleColor = titleColor;
		this.hasOverflow = hasOverflow;
		this.isClickable = isClickable;
		this.image = image;
	}

	public Card(String titlePlay, String description, String color,
			String titleColor, Boolean hasOverflow, Boolean isClickable) {

		this.titlePlay = titlePlay;
		this.description = description;
		this.color = color;
		this.titleColor = titleColor;
		this.hasOverflow = hasOverflow;
		this.isClickable = isClickable;
	}
	
	public Card(String like_me, String favorite, Boolean isClickable) {
		this.like_me = like_me;
		this.favorite = favorite;
		this.isClickable = isClickable;
	}


	@Override
	public View getView(Context context, boolean swipable) {
		return getView(context, false);
	}

	@Override
	public View getView(Context context) {

		View view = LayoutInflater.from(context).inflate(getCardLayout(), null);

		mCardLayout = view;

		try {
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// ((TextView) view.findViewById(R.id.title)).setText(this.title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int bottom = Utils.convertDpToPixelInt(context, 12);
		lp.setMargins(0, 0, 0, bottom);

		view.setLayoutParams(lp);

		return view;
	}

	public View getViewLast(Context context) {

		View view = LayoutInflater.from(context).inflate(getLastCardLayout(),
				null);

		mCardLayout = view;

		try {
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// ((TextView) view.findViewById(R.id.title)).setText(this.title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int bottom = Utils.convertDpToPixelInt(context, 12);
		lp.setMargins(0, 0, 0, bottom);

		view.setLayoutParams(lp);

		return view;
	}

	public View getViewFirst(Context context) {

		View view = LayoutInflater.from(context).inflate(getFirstCardLayout(),
				null);

		mCardLayout = view;

		try {
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// ((TextView) view.findViewById(R.id.title)).setText(this.title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int bottom = Utils.convertDpToPixelInt(context, 12);
		lp.setMargins(0, 0, 0, bottom);

		view.setLayoutParams(lp);

		return view;
	}

	public abstract View getCardContent(Context context);

	public OnClickListener getClickListener() {
		return mListener;
	}

	public void setOnClickListener(OnClickListener listener) {
		mListener = listener;
	}

	public void OnSwipeCard() {
		if (onCardSwipedListener != null)
			onCardSwipedListener.onCardSwiped(this, mCardLayout);
		// TODO: find better implementation to get card-object's used content
		// layout (=> implementing getCardContent());
	}

	public OnCardSwiped getOnCardSwipedListener() {
		return onCardSwipedListener;
	}

	public void setOnCardSwipedListener(OnCardSwiped onEpisodeSwipedListener) {
		this.onCardSwipedListener = onEpisodeSwipedListener;
	}

	protected int getCardLayout() {
		return R.layout.item_card;
	}

	protected int getLastCardLayout() {
		return R.layout.item_card_empty_last;
	}

	protected int getFirstCardLayout() {
		return R.layout.item_play_card_empty_first;
	}

}
