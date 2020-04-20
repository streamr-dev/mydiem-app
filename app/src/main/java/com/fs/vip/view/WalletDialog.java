package com.fs.vip.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fs.vip.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class WalletDialog extends Dialog {
    private  ImageView ivEr;
    private Context mContext;
    private String url;
    private RelativeLayout rlAll;


    public WalletDialog(Context context, int themeResId,String url) {
        super(context, themeResId);
        this.mContext = context;
        this.url = url;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wallet);
        setCanceledOnTouchOutside(false);
        ivEr = findViewById(R.id.iv_er);
        rlAll = findViewById(R.id.rl_all);
        rlAll.setOnClickListener(view -> dismiss());
        ivEr.setImageBitmap(CodeUtils.createImage(url, 400, 400, null));

    }

}
