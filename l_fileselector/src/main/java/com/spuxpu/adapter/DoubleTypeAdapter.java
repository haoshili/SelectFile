package com.spuxpu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuxpu.fileselector.R;
import com.spuxpu.service.GetAllFiles;
import com.spuxpu.service.Item;
import com.spuxpu.utils.TimeUtils;

public class DoubleTypeAdapter extends BaseAdapter {

	private Context context;
	private List<Item> listItems = new ArrayList<Item>();
	private Item curItem;

	private final static int TYPE_FOLDER = 0;
	private final static int TYPE_FILE = 1;
	private boolean isHtml = false;
	private String searchText;

	public DoubleTypeAdapter(Context context, List<Item> mDatas) {
		this.context = context;
		this.listItems = mDatas;
	}

	public DoubleTypeAdapter(Context context, List<Item> mDatas,
			boolean isHtml, String searchText) {
		this.context = context;
		this.listItems = mDatas;
		this.isHtml = isHtml;
		this.searchText = searchText;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		if (listItems.get(position).getImage() == GetAllFiles.FILE_FOLDER
				|| listItems.get(position).getImage() == GetAllFiles.FILE_BACK) {
			return TYPE_FOLDER;
		} else {
			return TYPE_FILE;
		}

	}

	@Override
	public int getCount() {

		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderFolder holderFolder = null;
		ViewHolderFile holderFile = null;
		curItem = listItems.get(position);

		int type = getItemViewType(position);

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			switch (type) {
			case TYPE_FOLDER:
				convertView = inflater.inflate(R.layout.adapter_folder_view,
						null);
				holderFolder = new ViewHolderFolder();
				holderFolder.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holderFolder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_title);
				convertView.setTag(holderFolder);
				break;

			case TYPE_FILE:
				convertView = inflater
						.inflate(R.layout.adapter_file_view, null);
				holderFile = new ViewHolderFile();
				holderFile.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holderFile.tv_name = (TextView) convertView
						.findViewById(R.id.tv_title);
				holderFile.tv_des = (TextView) convertView
						.findViewById(R.id.tv_sbtitle);

				convertView.setTag(holderFile);
				break;
			default:
				break;
			}
		} else {

			switch (type) {
			case TYPE_FOLDER:
				holderFolder = (ViewHolderFolder) convertView.getTag();
				break;
			case TYPE_FILE:
				holderFile = (ViewHolderFile) convertView.getTag();
				break;
			default:
				break;
			}

		}

		// 设置布局
		switch (type) {
		case TYPE_FOLDER:

			holderFolder.tv_name.setText(curItem.getName());
			holderFolder.iv_icon.setImageResource(getImage(curItem.getImage()));
			break;
		case TYPE_FILE:

			holderFile.iv_icon.setImageResource(getImage(curItem.getImage()));
			holderFile.tv_des.setText(getSubTitle(curItem));
			holderFile.tv_name.setText(Html.fromHtml(filterTitle(curItem)));
			break;
		default:
			break;
		}
		return convertView;
	}

	/**
	 * 图片布局
	 * 
	 * @author Administrator
	 * 
	 */
	private class ViewHolderFile {
		private ImageView iv_icon;
		private TextView tv_name;
		private TextView tv_des;
	}

	private class ViewHolderFolder {
		private ImageView iv_icon;
		private TextView tv_name;
	}

	/**
	 * 获取文件名称
	 * 
	 * @param type
	 * @return
	 */
	private int getImage(int type) {

		switch (type) {
		case GetAllFiles.FILE_DOC:
			return R.drawable.mz_ic_list_doc_small;
		case GetAllFiles.FILE_DOCX:
			return R.drawable.mz_ic_list_doc_small;
		case GetAllFiles.FILE_PDF:
			return R.drawable.mz_ic_list_pdf_small;
		case GetAllFiles.FILE_PPT:
			return R.drawable.mz_ic_list_ppt_small;
		case GetAllFiles.FILE_PPTX:
			return R.drawable.mz_ic_list_ppt_small;
		case GetAllFiles.FILE_TXT:
			return R.drawable.mz_ic_list_txt_small;
		case GetAllFiles.FILE_XLS:
			return R.drawable.mz_ic_list_xls_small;
		case GetAllFiles.FILE_XLSX:
			return R.drawable.mz_ic_list_xls_small;
		case GetAllFiles.FILE_FOLDER:
			return R.drawable.ic_folder;
		case GetAllFiles.FILE_BACK:
			return R.drawable.ic_folder_fast;
		default:
			return R.drawable.mz_ic_list_unknow_small;
		}
	}

	/**
	 * 获取小标题
	 * 
	 * @param item
	 * @return
	 */
	private String getSubTitle(Item item) {

		long data = item.getData() / 1024;
		if (data > 1024) {
			return (data / 1024) + "MB  " + getSubTitleTime(item);
		} else {
			return data + "KB  " + getSubTitleTime(item);
		}
	}

	/**
	 * 获取小标题时间
	 * 
	 * @param item
	 * @return
	 */
	private String getSubTitleTime(Item item) {
		long time = TimeUtils.getTimeBeginOfDay(System.currentTimeMillis());
		if ((item.getDate() - time) > 0) {
			return TimeUtils.getTimebyLong(item.getDate(), "HH:mm");
		} else {
			return TimeUtils.getTimebyLong(item.getDate(), "MM/dd");
		}
	}

	/**
	 * 对标题进行过滤
	 * 
	 * @return
	 */
	private String filterTitle(Item item) {

		if (isHtml ) {
			String result = item.getName();
		
			 
			return 	 result.replaceAll(searchText, "<font color=\"#f0693a\" >"+searchText+"</font>");
		} else {

			int length = item.getName().length();
			if (length > 20) {
				if (item.getImage() == GetAllFiles.FILE_UNKNOW
						|| item.getImage() == GetAllFiles.FILE_FOLDER) {
					return item.getName().subSequence(0, 18) + "...";
				}
				// 不对文档后缀进行处理，如果是正常的文档，虽然没有后缀，但是前边的图片就可以显示出来，所以不用担心。
				return item.getName().subSequence(0, 18) + "...";
			} else {
				return item.getName();
			}
		}
	}
}
