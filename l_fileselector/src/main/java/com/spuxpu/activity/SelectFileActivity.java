package com.spuxpu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spuxpu.adapter.DoubleTypeAdapter;
import com.spuxpu.fileselector.R;
import com.spuxpu.service.GetAllFiles;
import com.spuxpu.service.GetAllFiles.GetFileListListenser;
import com.spuxpu.service.Item;
import com.spuxpu.utils.OpenFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("InlinedApi")
public class SelectFileActivity extends BaseActivity {

    public static List<Item> listItemOrigin = new ArrayList<Item>();
    private ListView lv_file;
    private DrawerLayout drawer_layout;
    private List<Item> listItem = new ArrayList<Item>();
    private DoubleTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectfile);
        lv_file = (ListView) findViewById(R.id.lv_file);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lv_file.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                onListViewItemClick(arg0, arg1, arg2, arg3);
            }
        });
        inItData();
    }

    public void open(View v) {
        switchDrawer();
    }

    /**
     * 查询
     *
     * @param v
     */
    public void search(View v) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void showLatest(View v) {
        switchDrawer();
        adapter = new DoubleTypeAdapter(SelectFileActivity.this, listItemOrigin);
        lv_file.setAdapter(adapter);
    }

    public void filterOffice(View v) {

        switchDrawer();
        filterData(new int[]{GetAllFiles.FILE_DOC, GetAllFiles.FILE_DOCX,
                GetAllFiles.FILE_PPT, GetAllFiles.FILE_PPTX});
    }

    public void filterPDF(View v) {
        switchDrawer();
        filterData(new int[]{GetAllFiles.FILE_PDF});
    }

    public void filterTxt(View v) {
        switchDrawer();
        filterData(new int[]{GetAllFiles.FILE_TXT});
    }

    public void filterMap(View v) {
        switchDrawer();
        mapUtils(null);

    }

    private void switchDrawer() {
        if (drawer_layout.isDrawerOpen(Gravity.END)) {
            drawer_layout.closeDrawer(Gravity.END);
        } else {
            drawer_layout.openDrawer(Gravity.END);
        }
    }

    private void onListViewItemClick(AdapterView<?> arg0, View arg1,
                                     int position, long arg3) {
        Item curItem = listItem.get(position);

        if (position == 0 && curItem.getImage() == GetAllFiles.FILE_BACK) {

            mapUtils(curItem.getPath());
        } else {
            File curFile = new File(curItem.getPath());
            if (curFile.isDirectory()) {
                mapUtils(curFile.getPath());
            } else {
                startActivity(OpenFileUtils.openFile(curFile.getPath()));
            }
        }
    }

    private void mapUtils(String curItemPath) {
        new GetAllFiles().getFolder(curItemPath, new GetFileListListenser() {
            @Override
            public void getAllFile(List<Item> listFiles) {
                adapter = new DoubleTypeAdapter(SelectFileActivity.this,
                        listFiles);
                listItem = listFiles;
                lv_file.setAdapter(adapter);
            }
        });
    }

    private void inItData() {

        new GetAllFiles().getAllOfficeFiles(new GetFileListListenser() {
            @Override
            public void getAllFile(final List<Item> listFile) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listItemOrigin = listFile;
                        listItem = listItemOrigin;
                        adapter = new DoubleTypeAdapter(
                                SelectFileActivity.this, listItem);
                        lv_file.setAdapter(adapter);

                    }
                });
            }
        });
    }

    private void filterData(final int[] filter) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Item item;
                List<Item> listItemNew = new ArrayList<Item>();
                for (int i = 0; i < listItemOrigin.size(); i++) {
                    for (int j = 0; j < filter.length; j++) {
                        item = listItemOrigin.get(i);
                        if (item.getImage() == filter[j]) {
                            listItemNew.add(item);
                        }
                    }
                }
                listItem = listItemNew;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new DoubleTypeAdapter(
                                SelectFileActivity.this, listItem);
                        lv_file.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
}
