/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.yht.yihuantong.scheme;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * @author Joe
 * @date 2017/4/2.
 * Email lovejjfg@gmail.com
 */
public class ViewUtils {
    public static boolean isLaunchedActivity(Context context, Class<?> clazz) {
        try {
            Intent intent = new Intent(context, clazz);
            ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
            // 说明系统中存在这个activity
            if (cmpName != null) {
                ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
                for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                    // 说明它已经启动了
                    if (taskInfo.baseActivity.equals(cmpName)) {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
