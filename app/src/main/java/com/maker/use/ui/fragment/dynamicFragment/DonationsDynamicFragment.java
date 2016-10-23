/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.maker.use.ui.fragment.dynamicFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.maker.use.R;
import com.maker.use.ui.adapter.DonationsDynamicXRecyclerViewAdapter;
import com.maker.use.ui.view.myXRecyclerView.CampusDynamicXRecyclerView;
import com.maker.use.utils.UIUtils;

/**
 * 捐赠动态
 */
public class DonationsDynamicFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CoordinatorLayout cl_root = (CoordinatorLayout) inflater.inflate(R.layout.viewpage_list_dynamic, container, false);
        RelativeLayout rl_root = (RelativeLayout) cl_root.findViewById(R.id.rl_root);
        CampusDynamicXRecyclerView xRecyclerView = new CampusDynamicXRecyclerView(UIUtils.getContext(), cl_root);
        rl_root.addView(xRecyclerView, 0);
        setupXRecyclerView(xRecyclerView);
        return cl_root;
    }

    private void setupXRecyclerView(CampusDynamicXRecyclerView xRecyclerView) {
        xRecyclerView.setAdapter(new DonationsDynamicXRecyclerViewAdapter(getActivity()));
    }

}
