package com.yht.yihuantong.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.login.LoginOptionsActivity;
import com.yht.yihuantong.ui.main.MainActivity;

import butterknife.BindView;

/**
 * 启动界面
 *
 * @author DUNDUN
 */
public class SplashActivity extends BaseActivity implements DocAuthStatus {
    @BindView(R.id.iv_gif)
    ImageView ivGif;

    @Override
    public int getLayoutID() {
        return R.layout.act_splash;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        hideBottomUIMenu();
        load();
    }

    private void load() {
        RequestOptions options = new RequestOptions().priority(Priority.NORMAL)
                                                     .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).load(R.mipmap.pic_splash_gif).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                    boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                    DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).apply(options).into(ivGif);
    }

    /**
     * 页面初始化
     */
    private void initPage() {
        if (loginBean != null) {
            int checked = loginBean.getApprovalStatus();
            switch (checked) {
                case AUTH_NONE:
                case AUTH_WAITTING:
                case AUTH_FAILD:
                    startActivity(new Intent(this, LoginOptionsActivity.class));
                    startActivity(new Intent(this, AuthDoctorActivity.class));
                    finish();
                    break;
                case AUTH_SUCCESS:
                default:
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(CommonData.KEY_EASE_LOGIN_STATUS, true);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    break;
            }
        }
        else {
            startLoginPage();
        }
    }

    /**
     * 跳转登录界面
     */
    private void startLoginPage() {
        startActivity(new Intent(this, LoginOptionsActivity.class));
        finish();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
