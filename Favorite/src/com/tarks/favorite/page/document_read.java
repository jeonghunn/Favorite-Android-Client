//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.page;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.GalleryView;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.global.Filedw;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;

public class document_read extends SherlockActivity {

	private String local_path;
	private String externel_path;
	// Profile
	private ImageView profile;
	private TextView profile_title;
	private TextView profile_des;
	private TextView comment_count;
	private TextView doc_content;
	private ListView header_listView;
	// Member srl
	private String doc_srl = "0";
	private String user_srl = "0";
	private String comments = "0";
	private String status = "0";
	private String AttachFileName;
	private int comments_count = 0;
	private int previous_count = 1;
	private int contextmenu_number = 0;
	private int contextmenu_status = 0;
	private int contextmenu_you_status = 0;
	// Edittext
	private EditText comment_edittext;
	// ImageButton
	private ImageButton send_button;
	// Button
	private Button previous_comments;
	private WebView webview;

	// ListView
	private ListView listView;

	// List
	ArrayList<HeaderList> header_m_orders = new ArrayList<HeaderList>();
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	HeaderListAdapter header_m_adapter;
	ListAdapter m_adapter;
	// ClipBoard
	CharSequence clipboard_content;
	int you_doc_status;
	Context ct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.doclistview);
		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);

		ct = this;
		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		doc_srl = intent.getStringExtra("doc_srl");
	
		Log.i("Doc srl", doc_srl);
		try {
			externel_path = getExternalCacheDir().getAbsolutePath() + "/";
			local_path = getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}

		loadView();

	}

	public void loadView() {
		// Set List Adapter
		listView = (ListView) findViewById(R.id.listView1);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (parent.getAdapter() instanceof HeaderViewListAdapter) {
					if (((HeaderViewListAdapter) parent.getAdapter())
							.getWrappedAdapter() instanceof ListAdapter) {
						ListAdapter ca = (ListAdapter) ((HeaderViewListAdapter) parent
								.getAdapter()).getWrappedAdapter();

						try {
							List ls = (List) ca.getItem(position - 1);

							// Log.i("LongClick", "Clicked");
							clipboard_content = Global.getValue(ls.getDes());
							contextmenu_number = ls.getTag();
							contextmenu_status = ls.getStatus();
							contextmenu_you_status = ls.getYouStatus();
							// parent.showContextMenu();

						} catch (Exception e) {
						}
						// Log.i("LongClick", "Clicked");
					}
				}

				return false;

			}

		});
		registerForContextMenu(listView);
		// Header, Footer 생성 및 등록
		View header = getLayoutInflater().inflate(R.layout.doclist_header,
				null, false);
		profile = (ImageView) header.findViewById(R.id.img);
		profile_title = (TextView) header.findViewById(R.id.title);
		profile_des = (TextView) header.findViewById(R.id.description);
		header_listView = (ListView) header.findViewById(R.id.header_listview);
		doc_content = (TextView) header.findViewById(R.id.content);
		comment_count = (TextView) header.findViewById(R.id.comment_count);
		header_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				HeaderListAdapter ca = (HeaderListAdapter) arg0.getAdapter();

				HeaderList ls = (HeaderList) ca.getItem(arg2);

				try {
					if (ls.getPath().endsWith("jpg")) {
						Intent intent = new Intent(document_read.this,
								GalleryView.class);
						intent.putExtra("path", String.valueOf(ls.getPath()));
						startActivity(intent);
					} else {
						webview = new WebView(ct);
						webview.setDownloadListener(new DownloadListener() {
							@Override
							public void onDownloadStart(String url,
									String userAgent,
									String contentDisposition, String mimetype,
									long contentLength) {
								Global.toast(getString(R.string.start_downloading));
								Filedw.startdownload(url, userAgent,
										contentDisposition, mimetype,
										contentLength);
								webview.setDownloadListener(null);
								webview = null;
							}
						});
						webview.loadUrl(ls.getPath());
					}
				} catch (Exception e) {
				}

			}
		});
		// profile_edit.setOnClickListener(l)
		header_m_adapter = new HeaderListAdapter(this,
				R.layout.doc_header_list, header_m_orders);
		header_listView.setAdapter(header_m_adapter);
		doc_content.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub

				clipboard_content = doc_content.getText().toString();
				contextmenu_number = 0;
				// registerForContextMenu(doc_content);
				v.showContextMenu();
				return true;
			}
		});
		profile_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoPage(user_srl);
			}
		});

		profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoPage(user_srl);
			}
		});

		listView.addHeaderView(header);

		// Comment
		comment_edittext = (EditText) findViewById(R.id.comment_edittext);
		send_button = (ImageButton) findViewById(R.id.send);

		send_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Globalvariable.okbutton == true) {
					// Set ok button disable
					Globalvariable.okbutton = false;
					Global.ButtonEnable(1);
					String comment_value = comment_edittext.getText()
							.toString();

					if (!comment_value.matches("")) {
						CommentPostAct();
					}

				}
			}
		});
		previous_comments = (Button) findViewById(R.id.previous_comments_button);
		previous_comments.setVisibility(View.GONE);
		previous_comments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				previous_count = previous_count + 1;
				getCommentsList(getStartComment(comments_count),
						getStartComment(comments_count) == 0 ? comments_count
								- (previous_count - 1) * 10 : 10);
				if (getStartComment(comments_count) == 0)
					previous_comments.setVisibility(View.GONE);
				// listView.smoothScrollToPosition(0);
				// Log.i("Count", getStartComment(comments_count) + "");
			}

		});
		m_adapter = new ListAdapter(this, R.layout.comment_list, m_orders);
		listView.setAdapter(m_adapter);
		getDoc();

		//
	}

	public void refreshAct() {
		comments_count = 0;
		previous_count = 1;
		m_adapter.clear();
		header_m_adapter.clear();
		getDoc();

	}

	public void getDoc() {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("doc_srl");
		Paramname.add("doc_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(doc_srl);
		Paramvalue
				.add("page_srl//user_srl//name//title//content//date//status//privacy//comments//attach//recommend//negative");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname,
				Paramvalue, null, 1, 0);
	}

	public void getCommentsList(int start_comment, int number) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("doc_srl");
		Paramname.add("start_comment");
		Paramname.add("comment_number");
		Paramname.add("comment_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(doc_srl);
		Paramvalue.add(String.valueOf(start_comment));
		Paramvalue.add(String.valueOf(number));
		Paramvalue.add("srl//user_srl//name//content//date//status//privacy");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/comment_app_read.php", mHandler, Paramname,
				Paramvalue, null, 5, 0);
	}

	public void setList(int moreload, int srl, String user_srl, String name,
			String contents, String date, int status, int you_status) {

		// Get Profile
		// getMemberInfo(user_srl);
		List p1 = new List(user_srl, name, contents, date, srl, status,
				you_status);
		if (moreload == -1)
			m_orders.add(p1);
		else
			m_orders.add(moreload, p1);

		// ListView listview = (ListView) findViewById(R.id.listView1);

	}

	public void setHeaderList(String path, String title, String des) {

		// Get Profile
		// getMemberInfo(user_srl);
		HeaderList p1 = new HeaderList(path, title, des);

		header_m_orders.add(p1);

		// ListView listview = (ListView) findViewById(R.id.listView1);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		menu.setHeaderIcon(android.R.drawable.btn_star);
		// menu.setHeaderTitle("공지사항");
		if (contextmenu_number == 0) {
			menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.copy));
		}
		if (contextmenu_number != 0) {
			menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.copy));
			if (contextmenu_you_status == 4) {
				menu.add(Menu.NONE, 2, Menu.NONE, getString(R.string.delete));
				menu.add(Menu.NONE, 3, Menu.NONE,
						getString(R.string.privacy_content));
			}
		}
		super.onCreateContextMenu(menu, v, menuInfo);

	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			// ClipData clip = ClipData.newPlainText("content",
			// clipboard_content);
			// clipboard.setPrimaryClip(clip);
			clipboard.setText(clipboard_content);
			break;

		case 2:
			// Delete
			CommentStatusUpdate(String.valueOf(contextmenu_number), "5", 7);
			break;
		case 3:
			// StatusUpdate
			Intent intent1 = new Intent(document_read.this,
					privacy_category.class);
			intent1.putExtra("status", String.valueOf(contextmenu_status));
			startActivityForResult(intent1, 2);
			break;
		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	public int getStartComment(int comment) {
		comment = comment - previous_count * 10;
		if (comment < 0)
			comment = 0;

		return comment;
	}

	public void seePreviousComments(int comments) {
		if (comments - previous_count * 10 > 0) {
			previous_comments.setVisibility(View.VISIBLE);
		}
	}

	public void setCommentsCount(int count) {
		comments_count = count;
		comment_count.setText(String.valueOf(count));
		if(count != 0) comment_count.setVisibility(View.VISIBLE);
	}

	public void getMemberInfo(String user_srl) {
		if (Global.getUpdatePossible(user_srl)) {
			ArrayList<String> Paramname = new ArrayList<String>();
			Paramname.add("authcode");
			Paramname.add("user_srl");
			Paramname.add("user_srl_auth");
			Paramname.add("profile_user_srl");
			Paramname.add("member_info");

			ArrayList<String> Paramvalue = new ArrayList<String>();
			Paramvalue.add("642979");
			Paramvalue.add(Global.getSetting("user_srl",
					Global.getSetting("user_srl", "0")));
			Paramvalue.add(Global.getSetting("user_srl_auth",
					Global.getSetting("user_srl_auth", "null")));
			Paramvalue.add(String.valueOf(user_srl));
			Paramvalue.add("profile_pic//profile_update");

			new AsyncHttpTask(this, getString(R.string.server_path)
					+ "member/profile_info.php", mHandler, Paramname,
					Paramvalue, null, 2, Integer.parseInt(user_srl));

		}
	}

	public void GoPage(String user_srl) {
		Intent intent = new Intent(document_read.this, ProfileActivity.class);
		intent.putExtra("member_srl", user_srl);
		startActivity(intent);
	}

	public void ProfileUserImageDownload(String user_srl) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/thumbnail/" + user_srl + ".jpg", mHandler, 3,
				Integer.parseInt(user_srl));
	}

	public void ImageDownload(String url) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		int index = url.lastIndexOf("/");
		String fileName = url.substring(index + 1);
		AttachFileName = fileName;
		new ImageDownloader(this, url, mHandler, 11, Integer.parseInt(user_srl));
	}

	public void CommentPostAct() {
		setSupportProgressBarIndeterminateVisibility(true);
		comment_edittext.setEnabled(false);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("doc_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("content");
		Paramname.add("permission");
		Paramname.add("privacy");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(doc_srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(Global.setValue(comment_edittext.getText().toString()));
		Paramvalue.add("3");
		Paramvalue.add("0");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/comment_app_write.php", mHandler, Paramname,
				Paramvalue, null, 4, 0);
	}

	public void StatusUpdate(String status, int handler) {
		// IF Sucessfull no timeout
		setSupportProgressBarIndeterminateVisibility(true);
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("doc_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("status");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("0");
		Paramvalue.add(doc_srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(status);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_write.php", mHandler, Paramname,
				Paramvalue, null, handler, 0);
	}

	public void CommentStatusUpdate(String srl, String status, int handler) {
		// IF Sucessfull no timeout
		setSupportProgressBarIndeterminateVisibility(true);
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("comment_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("status");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("0");
		Paramvalue.add(srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(status);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/comment_app_write.php", mHandler, Paramname,
				Paramvalue, null, handler, 0);
	}

	public void AttachDownload() {
		setSupportProgressBarIndeterminateVisibility(true);
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("doc_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("5");
		Paramvalue.add(doc_srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname,
				Paramvalue, null, 10, 0);
	}

	public void DeleteAlert() {
		// Alert
		AlertDialog.Builder builder = new AlertDialog.Builder(
				document_read.this);
		builder.setMessage(getString(R.string.delete_des)).setTitle(
				getString(R.string.delete));
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (Globalvariable.okbutton == true) {
							// Set ok button disable
							StatusUpdate("5", 6);
						}
					}
				});
		builder.setNegativeButton(getString(R.string.no), null);
		builder.show();

	}

	// @Override
	// protected void onDestroy() {
	// // Log.d("OOMTEST", "onDestroy");
	//
	// Global.recycleBitmap(profile);
	// Global.recycleBitmap(send_button);
	// super.onDestroy();
	// }

	private class ListAdapter extends ArrayAdapter<List> {

		private ArrayList<List> items;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<List> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.comment_list, null);
			}
			final List p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.title);
				TextView bt = (TextView) v.findViewById(R.id.description);
				TextView date = (TextView) v.findViewById(R.id.date);
				ImageView image = (ImageView) v.findViewById(R.id.img);
				if (tt != null) {
					tt.setText(p.getTitle());
				}
				if (bt != null) {
					bt.setText(Global.getValue(p.getDes()));
				}
				if (date != null) {
					date.setText(p.getDate());
				}
				if (image != null) {

					boolean state = Global.CheckFileState(local_path
							+ "thumbnail/" + p.getUserSrl() + ".jpg");

					if (state) {
						image.setImageDrawable(Drawable
								.createFromPath(local_path + "thumbnail/"
										+ p.getUserSrl() + ".jpg"));
					} else {
						image.setImageResource(R.drawable.person);
					}
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(document_read.this,
									ProfileActivity.class);
							intent.putExtra("member_srl", p.getUserSrl());
							startActivity(intent);
						}
					});
				}
			}
			return v;
		}
	}

	class List {

		private String user_srl;
		private String Title;
		private String Description;
		private String Date;
		private int Tag;
		private int status;
		private int you_status;

		public List(String _user_Srl, String _Title, String _Description,
				String _Date, int _Tag, int _Status, int _you_status) {
			this.user_srl = _user_Srl;
			this.Title = _Title;
			this.Description = _Description;
			this.Date = _Date;
			this.Tag = _Tag;
			this.status = _Status;
			this.you_status = _you_status;
		}

		public String getUserSrl() {
			return user_srl;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		public String getDate() {
			return Date;
		}

		public int getTag() {
			return Tag;
		}

		public int getStatus() {
			return status;
		}

		public int getYouStatus() {
			return you_status;
		}

	}

	private class HeaderListAdapter extends ArrayAdapter<HeaderList> {

		private ArrayList<HeaderList> items;

		public HeaderListAdapter(Context context, int textViewResourceId,
				ArrayList<HeaderList> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.doc_header_list, null);
			}
			final HeaderList p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.title);
				TextView bt = (TextView) v.findViewById(R.id.description);
				ImageView image = (ImageView) v.findViewById(R.id.img);
				ImageView thumb_image = (ImageView) v
						.findViewById(R.id.thumb_img);
				if (tt != null) {
					if (p.getTitle() != null) {
						tt.setText(p.getTitle());
					} else {
						tt.setVisibility(View.GONE);
					}
				}
				if (bt != null) {
					if (bt != null) {
						if (p.getDes() != null) {
							bt.setText(p.getDes());
						} else {
							bt.setVisibility(View.GONE);

						}
					}
				}
				if (image != null && p.getPath().endsWith("jpg") || p.getPath().endsWith("jpeg")) {
					Bitmap bm = Global.UriToBitmapCompress(Uri
							.fromFile(new File(p.getPath())));
					image.setImageBitmap(bm);
					thumb_image.setVisibility(View.GONE);

					// image.setOnClickListener(new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// Intent intent = new Intent(document_read.this,
					// ProfileActivity.class);
					// intent.putExtra("member_srl", p.getUserSrl());
					// startActivity(intent);
					// }
					// });
				}
			}
			return v;
		}
	}

	class HeaderList {

		private String Path;
		private String Title;
		private String Description;
		private int you_status;

		public HeaderList(String _Path, String _Title, String _Description) {
			this.Path = _Path;
			this.Title = _Title;
			this.Description = _Description;
			// this.you_status = _you_status;
		}

		public String getPath() {
			return Path;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		// public int getYouStatus() {
		// return you_status;
		// }

	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);

			if (msg.what == -1) {
				Global.ConnectionError(document_read.this);
			}

			if (msg.what == 1) {

				try {
					// page_srl//user_srl//name//title//content//date//status//privacy//comments//recommend//negative
					String[] array = msg.obj.toString().split("/LINE/.");
					// Global.dumpArray(array);
					String page_srl = array[0];
					user_srl = array[1];
					String name = array[2];
					String title = array[3];
					String content = array[4];
					String date = array[5];
					status = array[6];
					String privacy = array[7];
					comments = array[8];
					String attach = array[9];
					String recommend = array[10];
					String negative = array[11];
					you_doc_status = Integer.parseInt(array[12]);

					// SetTitled
					if (title.matches("null")) {
						getSupportActionBar().setTitle(name);
					} else {
						getSupportActionBar().setTitle(title);
					}

					getMemberInfo(user_srl);

					// Profiile

					boolean state = Global.CheckFileState(local_path
							+ "thumbnail/" + user_srl + ".jpg");

					if (state) {
						profile.setImageDrawable(Drawable
								.createFromPath(local_path + "thumbnail/"
										+ user_srl + ".jpg"));
					} else {
						profile.setImageResource(R.drawable.person);
					}

					profile_title.setText(name);
					profile_des.setText(Global.formatTimeString(Long
							.parseLong(date)));
					setCommentsCount(Integer.parseInt(comments));
					doc_content.setText(Global.getValue(content));
					// Set comment
					getCommentsList(getStartComment(comments_count), 10);
					// Load menu again
					invalidateOptionsMenu();

					if (!attach.matches("0"))
						AttachDownload();

				} catch (Exception e) {
//					Global.Infoalert(document_read.this, getString(R.string.error),
//							getString(R.string.doc_error_des),
//							getString(R.string.yes));
					
					Global.toast(getString(R.string.doc_error_des));
					finish();
				}
			}

			if (msg.what == 2) {

				try {

					String[] array = msg.obj.toString().split("/LINE/.");
					// Global.dumpArray(array);
					String profile_pic = array[0];
					String profile_update = array[1];

					String user_srl = String.valueOf(msg.arg1);

					Global.SaveUserSetting(user_srl, null,
							String.valueOf(Global.getCurrentTimeStamp()),
							profile_pic);

					if (profile_pic.matches("Y")) {
						// Global.SaveUserSetting(user_srl, profile_update);
						ProfileUserImageDownload(user_srl);
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + user_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/"
								+ user_srl + ".jpg");
						file_thum.delete();
					}
				} catch (Exception e) {
					// MemberInfoError();

				}
			}

			if (msg.what == 3) {
				// Save File cache
				try {
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path
							+ "thumbnail/", msg.arg1 + ".jpg");
					m_adapter.notifyDataSetChanged();

					// Set Profile
					// profile.setImageDrawable(Drawable.createFromPath(local_path
					// + member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

			if (msg.what == 4) {
				String result = msg.obj.toString();
				comment_edittext.setEnabled(true);
				if (result.matches("comment_write_succeed")) {
					// setCommentsCount(comments_count + 1);
					previous_count = 1;
					m_adapter.clear();
					comment_edittext.setText(null);
					getDoc();
					listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				} else {
					Global.ConnectionError(document_read.this);
				}
				// Log.i("Result","로그 정상 작동");
				Log.i("Result", msg.obj.toString());

			}

			if (msg.what == 5) {
				try {
					int moreload = -1;
					Log.i("Cmt", msg.obj.toString());
					String[] cmt = msg.obj.toString().split("/CMT/.");

					for (int i = 0; i < cmt.length; i++) {
						String[] array = cmt[i].split("/LINE/.");
						// Global.dumpArray(array);
						// srl//user_srl//name//content//date//status/privacy
						String srl = array[0];
						String user_srl = array[1];
						String name = array[2];
						String content = array[3];
						String date = array[4];
						String status = array[5];
						String privacy = array[6];
						String you_status = array[7];
						// Log.i("user", user_srl);

						if (previous_count > 1)
							moreload = i;
						getMemberInfo(user_srl);
						setList(moreload, Integer.parseInt(srl), user_srl,
								name, content,
								Global.formatTimeString(Long.parseLong(date)),
								Integer.parseInt(status),
								Integer.parseInt(you_status));
						m_adapter.notifyDataSetChanged();
					}
					seePreviousComments(comments_count);
				} catch (Exception e) {

				}
			}

			// DeleteAct
			if (msg.what == 6) {
				// Log.i("result", msg.obj.toString());
				String result = msg.obj.toString();
				if (result.matches("document_update_succeed")) {
					Global.toast(getString(R.string.deleted));
					UpdateFinishAct();
				} else {
					Global.toast(getString(R.string.error_des));
				}

			}

			// DeleteCommentAct
			if (msg.what == 7) {
				// Log.i("result", msg.obj.toString());
				String result = msg.obj.toString();
				if (result.matches("comment_update_succeed")) {
					Global.toast(getString(R.string.deleted));
					previous_count = 1;
					m_adapter.clear();
					comment_edittext.setText(null);
					getDoc();
					listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				} else {
					Global.toast(getString(R.string.error_des));
				}

			}
			// Doc status update
			if (msg.what == 8) {
				// Log.i("result", msg.obj.toString());
				String result = msg.obj.toString();
				if (result.matches("document_update_succeed")) {
					Global.toast(getString(R.string.changed));
					refreshAct();
				} else {
					Global.toast(getString(R.string.error_des));
				}

			}
			// Comment status update
			if (msg.what == 9) {
				// Log.i("result", msg.obj.toString());
				String result = msg.obj.toString();
				if (result.matches("comment_update_succeed")) {
					Global.toast(getString(R.string.changed));
					refreshAct();
				} else {
					Global.toast(getString(R.string.error_des));
				}

			}

			// Attach list download
			if (msg.what == 10) {
				// Log.i("result", msg.obj.toString());
				String[] array = msg.obj.toString().split("/LINE/.");

				for (int i = 0; i < array.length; i++) {
					if (array[i].endsWith("jpg") || array[i].endsWith("jpeg")) {
						int index = array[i].lastIndexOf("/");
						String fileName = array[i].substring(index + 1);

						if (!Global.CheckFileState(externel_path + fileName)) {
							ImageDownload(array[i]);
						} else {
							setHeaderList(externel_path + fileName, null, null);
							header_m_adapter.notifyDataSetChanged();
						}
					} else {
						// int index = array[i].lastIndexOf("/");
						String name = array[i].substring(
								array[i].lastIndexOf("&n=") + 3,
								array[i].lastIndexOf("&e="));
						String extension = array[i].substring(
								array[i].lastIndexOf("=") + 1,
								array[i].length()).toUpperCase();
						setHeaderList(array[i], name, extension + " "
								+ getString(R.string.file));
						header_m_adapter.notifyDataSetChanged();
					}
				}

			}

			if (msg.what == 11) {
				// Log.i("result", msg.obj.toString());
try{
				Global.SaveBitmapToFileCache((Bitmap) msg.obj, externel_path,
						AttachFileName);
}catch(Exception e){Global.toast(getString(R.string.no_storage_error));}
				setHeaderList(externel_path + AttachFileName, null, null);
				header_m_adapter.notifyDataSetChanged();
			}

		}

	};

	public void UpdateFinishAct() {
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// setListAdapter();
			String status = data.getStringExtra("status");
			StatusUpdate(status, 8);
		}

		if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			// setListAdapter();
			String status = data.getStringExtra("status");
			CommentStatusUpdate(String.valueOf(contextmenu_number), status, 9);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item;

		menu.add(0, 0, 0, getString(R.string.refresh)).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);
		if (you_doc_status >= 4) {
			menu.add(0, 1, 0, getString(R.string.delete)).setShowAsAction(
					MenuItem.SHOW_AS_ACTION_NEVER);
			menu.add(0, 2, 0, getString(R.string.privacy_content))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}

		// item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
		// item.setIcon(R.drawable.ic_menu_add_bookmark);

		return true;
	}

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case 0:
			// Refresh
			if (Globalvariable.okbutton == true) {
				// Set ok button disable
				refreshAct();
			}

			return true;
		case 1:
			// Delete
			// Refresh
			DeleteAlert();

			return true;
		case 2:
			Intent intent1 = new Intent(document_read.this,
					privacy_category.class);
			intent1.putExtra("status", status);
			startActivityForResult(intent1, 1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
