package com.spuxpu.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class BaseSetAdapter extends BaseAdapter {

    private Context context;
    private List<Item> listItems = new ArrayList<Item>();
    private Item curItem;

    public BaseSetAdapter(Context context) {
        this.context = context;
    }

    public BaseSetAdapter(Context context, List<Item> listItem) {
        this.listItems = listItem;
        this.context = context;
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

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.adapter_file_view, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.iv_photo = (ImageView) convertView
                    .findViewById(R.id.iv_photo);
            holder.tv_sbtitle = (TextView) convertView
                    .findViewById(R.id.tv_sbtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        curItem = listItems.get(position);
        holder.tv_title.setText(filterTitle(curItem));
        holder.iv_photo.setImageResource(getImage(curItem.getImage()));
        holder.tv_sbtitle.setText(getSubTitle(curItem));
        return convertView;
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

        int length = item.getName().length();
        if (length > 20) {
            if (item.getImage() == GetAllFiles.FILE_UNKNOW
                    || item.getImage() == GetAllFiles.FILE_FOLDER) {
                return item.getName().subSequence(0, 18) + "**";
            }
            //不对文档后缀进行处理，如果是正常的文档，虽然没有后缀，但是前边的图片就可以显示出来，所以不用担心。
            return item.getName().subSequence(0, 18) + "**";
        } else {
            return item.getName();
        }
    }

    private class ViewHolder {
        private ImageView iv_photo;
        private TextView tv_title;
        private TextView tv_sbtitle;
    }
}
