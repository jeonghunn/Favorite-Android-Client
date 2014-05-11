package com.tarks.favorite.contacts;

import java.util.ArrayList;
import java.util.List;

import com.tarks.favorite.contacts.Contact;
import com.tarks.favorite.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ContactsArray {
	public ArrayList<Contact> getContactList(Context cx) {

		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

		String[] selectionArgs = null;

		String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		Cursor contactCursor = 	cx.getContentResolver().query(uri, projection, null, null, null);

		ArrayList<Contact> contactlist = new ArrayList<Contact>();

		if (contactCursor.moveToFirst()) {
			do {
				String phonenumber = contactCursor.getString(1).replaceAll("-",
						"");
				if (phonenumber.length() == 10) {
					phonenumber = phonenumber.substring(0, 3) + "-"
							+ phonenumber.substring(3, 6) + "-"
							+ phonenumber.substring(6);
				} else if (phonenumber.length() > 8) {
					phonenumber = phonenumber.substring(0, 3) + "-"
							+ phonenumber.substring(3, 7) + "-"
							+ phonenumber.substring(7);
				}

				Contact acontact = new Contact();
				acontact.setPhotoid(contactCursor.getLong(0));
				acontact.setPhonenum(phonenumber);
				acontact.setName(contactCursor.getString(2));

				contactlist.add(acontact);
			} while (contactCursor.moveToNext());
		}

		return contactlist;

	}
	
	
}
