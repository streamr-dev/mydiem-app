package com.fs.vip.base;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.view.CustomDialog;

import butterknife.Unbinder;


/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseMainFragment extends MySupportFragment {
    protected OnFragmentOpenDrawerListener mOpenDraweListener;
    private CustomDialog dialog;

    protected void initToolbarNav(Toolbar toolbar, boolean isHome) {
        if (isHome){
            toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(v -> {
                if (mOpenDraweListener != null) {
                    mOpenDraweListener.onOpenDrawer();
                }
            });
        }else{
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(v -> pop());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentOpenDrawerListener) {
            mOpenDraweListener = (OnFragmentOpenDrawerListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentOpenDrawerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpenDraweListener = null;
        dismissDialog();
    }

    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }

    // dialog
    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = CustomDialog.instance(getActivity());
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showDialog(String progressTip) {
        getDialog().show();
        if (progressTip != null) {
            getDialog().setTvProgress(progressTip);
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
