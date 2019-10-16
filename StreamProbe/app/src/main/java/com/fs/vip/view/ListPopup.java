package com.fs.vip.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.fs.vip.R;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ListPopup extends BasePopupWindow {
    private ListView mListView;
    private OnListPopupItemClickListener mOnListPopupItemClickListener;

    private ListPopup(Context context) {
        super(context);
    }

    private ListPopup(Context context, Builder builder) {
        this(context);
        mListView = (ListView) findViewById(R.id.popup_list);
        setAdapter(context, builder);
        setPopupGravity(Gravity.CENTER);
        setClipChildren(false);
    }

    public static class Builder {
        private List<Object> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public Builder addItem(String itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

//        public Builder addItem(int clickTag, String itemTx) {
//            mItemEventList.add(new clickItemEvent(clickTag, itemTx));
//            return this;
//        }

        public List<Object> getItemEventList() {
            return mItemEventList;
        }

        public ListPopup build() {
            return new ListPopup(mContext, this);
        }

    }

    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Object> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Object> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public String getItem(int position) {
            if (mItemList.get(position) instanceof String) {
                return (String) mItemList.get(position);
            }
//            if (mItemList.get(position) instanceof clickItemEvent) {
//                return ((clickItemEvent) mItemList.get(position)).getItemTx();
//            }
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup_list, parent, false);
                vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(getItem(position));
            return convertView;
        }

        public List<Object> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            public TextView mTextView;
        }
    }

    //=============================================================init
    private void setAdapter(Context context, Builder builder) {
        if (builder.getItemEventList() == null || builder.getItemEventList().size() == 0) return;
        final ListPopupAdapter adapter = new ListPopupAdapter(context, builder.getItemEventList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnListPopupItemClickListener != null) {
                    Object clickObj = adapter.getItemList().get(position);
                    if (clickObj instanceof String) {
                        mOnListPopupItemClickListener.onItemClick((String)clickObj);
                    }
//                    if (clickObj instanceof clickItemEvent) {
//                        int what = ((clickItemEvent) clickObj).clickTag;
//                        mOnListPopupItemClickListener.onItemClick(what);
//                    }
                }
            }
        });

    }

    //=============================================================super methods
    @Override
    protected Animation onCreateShowAnimation() {
        return null;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return null;
    }

    @Override
    public Animator onCreateShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(getDisplayAnimateView(), "rotationX", 90f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "translationY", 250f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "alpha", 0f, 1f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_list);
    }


    @Override
    public Animator onCreateDismissAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(getDisplayAnimateView(), "rotationX", 0f, 90f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "translationY", 0f, 250f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "alpha", 1f, 0f).setDuration(400 * 3 / 2));
        return set;
    }

    //=============================================================interface

    public OnListPopupItemClickListener getOnListPopupItemClickListener() {
        return mOnListPopupItemClickListener;
    }

    public void setOnListPopupItemClickListener(OnListPopupItemClickListener onListPopupItemClickListener) {
        mOnListPopupItemClickListener = onListPopupItemClickListener;
    }

    public interface OnListPopupItemClickListener {
        void onItemClick(String what);
    }
}
