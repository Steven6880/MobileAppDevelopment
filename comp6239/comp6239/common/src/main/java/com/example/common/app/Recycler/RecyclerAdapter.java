package com.example.common.app.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author Huifeng, Kejia, Yunheng
 */

public abstract class RecyclerAdapter <Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
implements View.OnClickListener , View.OnLongClickListener, AdapterCallBack<Data>{
    //<Data> 规定必须为Data类型
    public final List<Data> mDataList = new ArrayList<Data>();

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position , mDataList.get(position));
    }

    /**
     * 得到布局类型
     */
    @LayoutRes
    protected abstract int  getItemViewType(int position , Data data);

    /**
     * create a ViewHolder
     * @param parent RecyclerView
     * @param viewType Type of the View, the ID of XML
     * @return ViewType
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到LayoutInflater用于把XML初始化
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把XML id为ViewType的文件初始化为一个RootView
        View root = inflater.inflate(viewType , parent , false);
        // 通过子类必须实现的方法，得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        //设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        // 设置View的tag为ViewHolder进行双向绑定
        root.setTag(R.id.tag_recyclerholder , holder);
        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder , root);
        //绑定callback
        holder.callback = this;

        return null;
    }

    public abstract ViewHolder<Data> onCreateViewHolder(View root , int ViewType);

    /**
     * bind the data to one holder
     * @param holder ViewHolder
     * @param position Zuobiao
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        //get the data that needs to be binded
        Data data = mDataList.get(position);
        // make method to bind Holder
        holder.bind(data);

    }

    @Override
    /**
     * get the size of current data
     */
    public int getItemCount() {
        //计数

        return mDataList.size();
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        private AdapterCallBack<Data> callback;
        private Unbinder unbinder;
        //与界面数据绑定在一起
        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Data data){
            /**
             * 用于绑定数据的触发，只能在这个类中使用
             * data绑定的数据
             */
            this.mData = data;
            onBind(data);
        }

        /**
         * 触发绑定数据的回调，必须复写
         * data 绑定的数据
         */
        protected abstract void onBind(Data data); //保护界面，刷新

        public void updateData(Data data){ //update data after user's operating
            if (this.callback != null){
                this.callback.update(data, this);
            }

        }
    }
}
