/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.camera2video.event.StartEvent;
import com.example.android.camera2video.event.StopEvent;
import com.example.android.camera2video.service.BackRecordService;

import org.greenrobot.eventbus.EventBus;

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        if (null == savedInstanceState) {
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.container, Camera2VideoFragment.newInstance())
//                    .commit();
//        }


    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.start:
                startService(new Intent(this, BackRecordService.class));
                break;
            case R.id.stop:
                EventBus.getDefault().post(new StopEvent());
                break;
            default:
                break;
        }

    }

}
