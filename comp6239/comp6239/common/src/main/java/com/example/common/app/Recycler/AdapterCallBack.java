package com.example.common.app.Recycler;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author Huifeng, Kejia, Yunheng
 */

public interface AdapterCallBack<Data>{ //适配器
    void update (Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
