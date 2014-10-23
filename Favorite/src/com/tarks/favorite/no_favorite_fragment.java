//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class no_favorite_fragment extends Fragment  {

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.no_favorite, container, false);

		Button bt = (Button) rootView.findViewById(R.id.button1);
		
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main activity = (main) getActivity();
				activity.selectItem(2);
				
		
			}
		});
	
		return rootView;
	}
	
	

}
