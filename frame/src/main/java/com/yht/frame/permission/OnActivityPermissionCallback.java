package com.yht.frame.permission;

import androidx.annotation.NonNull;

/**
 * @author dundun
 */
public interface OnActivityPermissionCallback {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onActivityForResult(int requestCode);
}
