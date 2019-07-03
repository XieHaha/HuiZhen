package com.yht.frame.http.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.yht.frame.data.Tasks;
import com.yht.frame.http.listener.ResponseListener;
import com.yht.frame.widgets.dialog.LoadingDialog;

import java.util.Objects;

import io.reactivex.disposables.Disposable;

/**
 * Observer加入加载框
 *
 * @param <T>
 * @author dundun
 */
public class AbstractBaseObserver<T> extends AbstractDataObserver<T> {
    private boolean mShowDialog;
    private LoadingDialog loadingDialog;
    private Context mContext;
    private Disposable d;

    public AbstractBaseObserver(Context context, Tasks task, ResponseListener listener) {
        this(context, false, task, listener);
    }

    public AbstractBaseObserver(Context context, Boolean showDialog, Tasks task, ResponseListener listener) {
        mContext = context;
        mShowDialog = showDialog;
        super.setParams(task, listener);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        }
        else {
            if (loadingDialog == null && mShowDialog) {
                loadingDialog = new LoadingDialog(mContext);
                loadingDialog.show();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onError(e);
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onComplete();
    }

    private void hidDialog() {
        if (loadingDialog != null && mShowDialog) { loadingDialog.dismiss(); }
        loadingDialog = null;
    }

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     *
     * @return 网络
     */
    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(cm).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }
}

