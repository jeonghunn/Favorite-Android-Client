//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.user.db;

import android.provider.BaseColumns;

// DataBase Table
public final class DataBases {
	
	public static final class CreateDB implements BaseColumns{
		public static final String USER_SRL = "user_srl";
		public static final String PROFILE_UPDATE = "profile_update";
		public static final String PROFILE_UPDATE_THUMBNAIL = "profile_update_thumbnail";
		public static final String PROFILE_PIC = "profile_pic";
		public static final String _TABLENAME = "users";
		public static final String _CREATE = 
			"create table "+_TABLENAME+"(" 
					+_ID+" integer primary key autoincrement, " 	
					+USER_SRL+" text not null , " 
					+PROFILE_UPDATE+" text not null , " 
						+PROFILE_UPDATE_THUMBNAIL+" text not null , " 
					+PROFILE_PIC+" text not null);";
	}
}
