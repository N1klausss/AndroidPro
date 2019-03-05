package com.example.a58204.lab11.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by 58204 on 2017/12/23.
 */

public abstract class CardAdapter<T> extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Context mContext;
    private int mlayoutId;
    private List<T> mdata;
    private OnItemClickListener mOnItemClickListener;

    public CardAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mlayoutId = layoutId;
        mdata = data;
    }

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mlayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        convert(holder, mdata.get(position));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public ViewHolder(Context context, View itemView, ViewGroup parent) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        public static ViewHolder get(Context context, ViewGroup parent, int layoutId) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            ViewHolder holder = new ViewHolder(context, itemView, parent);
            return holder;
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView textView = getView(viewId);
            textView.setText(text);
        }

        public void setImg(int viewId, int resId) {
            ImageView imageView = getView(viewId);
            imageView.setImageResource(resId);
        }
    }

    public abstract void convert(ViewHolder holder, T item);


    public List<T> getList() {
        return mdata;
    }

    @Override
    public int getItemCount() {
        if (mdata != null) {
            return mdata.size();
        }
        return 0;
    }

    public void removeItem(int position) {
        mdata.remove(position);
        notifyDataSetChanged();
    }

    public void addData(T data)
    {
        mdata.add(data);
        notifyDataSetChanged();
    }
    public void addListData(List<T> data)
    {
        mdata.addAll(data);
        notifyDataSetChanged();
    }
}
