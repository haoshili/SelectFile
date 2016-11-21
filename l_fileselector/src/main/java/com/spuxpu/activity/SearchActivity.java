package com.spuxpu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.spuxpu.adapter.DoubleTypeAdapter;
import com.spuxpu.fileselector.R;
import com.spuxpu.service.Item;

public class SearchActivity extends BaseActivity {

	private ListView lv_search;
	private EditText et_search;
	private DoubleTypeAdapter adapter;
	private String searchText;
	private List<Item> searchResult = new ArrayList<Item>();

	private static final int MSG_EXCUTE = 0;
	private static final int MSG_REFRESH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		lv_search = (ListView) findViewById(R.id.lv_search);
		et_search = (EditText) findViewById(R.id.et_search);
		inItView();
	}

	/**
	 * 初始化视图界
	 */
	private void inItView() {
		adapter = new DoubleTypeAdapter(this, SelectFileActivity.listItemOrigin);
		lv_search.setAdapter(adapter);

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				searchText = arg0.toString();
				handler.sendEmptyMessageDelayed(MSG_EXCUTE, 100);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_EXCUTE:
				searchExcuter();
				break;

			case MSG_REFRESH:
				refreshUI();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 执行搜索
	 */
	private void searchExcuter() {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				searchResult.clear();
				for(Item item: SelectFileActivity.listItemOrigin){
					if(item.getName().contains(searchText)){
						searchResult.add(item);
					}
				}
				handler.sendEmptyMessage(MSG_REFRESH);
			}
		}).start();
		
		
	}
	
	/**
	 * 刷新UI
	 */
	private void refreshUI(){
		adapter = new DoubleTypeAdapter(this, searchResult,true,searchText);
		lv_search.setAdapter(adapter);
	}

}
