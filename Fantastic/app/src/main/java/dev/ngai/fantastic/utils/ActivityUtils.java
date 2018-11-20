/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ngai.fantastic.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.picturebrowse.PictureBrowseActivity;
import dev.ngai.fantastic.business.picturebrowse.PictureBrowsePresenter;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.Imginfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void startPictureBrowseActivity(@NonNull Context context,
                                                  @NonNull int discoverId,
                                                  @NonNull List<Imginfo> imgs,
                                                  @NonNull int index,
                                                  @NonNull int itemPosition,
                                                  @NonNull String tab) {
        MobclickAgent.onEvent(context,"PictureBrowse-"+tab+"-discoverId("+discoverId+")");
        Intent intent = new Intent(context, PictureBrowseActivity.class);
        ArrayList<Imginfo> list = new ArrayList<>();
        list.addAll(imgs);
        intent.putParcelableArrayListExtra(PictureBrowsePresenter.PICTURE_DATA, list);
//        intent.putStringArrayListExtra(PictureBrowsePresenter.PICTURE_DATA, list);
        intent.putExtra(PictureBrowsePresenter.PICTURE_INDEX, index);
        intent.putExtra(PictureBrowsePresenter.PICTURE_OBJ_ID, discoverId);
        intent.putExtra(PictureBrowsePresenter.PICTURE_ITEM_POSITION, itemPosition);
        context.startActivity(intent);
    }
}
