/*
 * Copyright (c) 2012 Eddie Ringle
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.idlesoft.android.apps.github.ui.fragments;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import net.idlesoft.android.apps.github.R;
import net.idlesoft.android.apps.github.ui.activities.BaseActivity;
import net.idlesoft.android.apps.github.ui.activities.BaseDashboardActivity;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GsonUtils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import static net.idlesoft.android.apps.github.HubroidConstants.ARG_TARGET_ISSUE;
import static net.idlesoft.android.apps.github.HubroidConstants.ARG_TARGET_REPO;
import static net.idlesoft.android.apps.github.HubroidConstants.ARG_TARGET_USER;

public class BaseFragment extends SherlockFragment {

    protected Configuration mConfiguration;

    private boolean mCreateActionBarCalled = false;

    protected HashMap<String, Object> mArgumentMap = new HashMap<String, Object>();

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConfiguration = getResources().getConfiguration();

		/*
         * Process arguments Bundle into a HashMap of different objects
		 */
        final Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getString(ARG_TARGET_USER) != null) {
                mArgumentMap.put(ARG_TARGET_USER,
                        GsonUtils.fromJson(arguments.getString(ARG_TARGET_USER),
                                User.class));
            }
            if (arguments.getString(ARG_TARGET_ISSUE) != null) {
                mArgumentMap.put(ARG_TARGET_ISSUE,
                        GsonUtils.fromJson(arguments.getString(ARG_TARGET_ISSUE),
                                Issue.class));
            }
            if (arguments.getString(ARG_TARGET_REPO) != null) {
                mArgumentMap.put(ARG_TARGET_ISSUE,
                        GsonUtils.fromJson(arguments.getString(ARG_TARGET_ISSUE),
                                Repository.class));
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Let the system know this fragment can provide an options menu */
        setHasOptionsMenu(true);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public Context getContext() {
        return getBaseActivity().getContext();
    }

    protected View getFragmentContainer() {
        return getBaseActivity().findViewById(R.id.container_main);
    }

    protected boolean isMultiPane() {
        return getBaseActivity().isMultiPane();
    }

    public void onCreateActionBar(ActionBar bar, Menu menu, MenuInflater inflater) {
        mCreateActionBarCalled = true;

        bar.setHomeButtonEnabled(true);

        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (getBaseActivity() instanceof BaseDashboardActivity &&
                ((BaseDashboardActivity)getBaseActivity()).isDrawerOpened()) {
            return;
        } else {
            mCreateActionBarCalled = false;
            onCreateActionBar(getBaseActivity().getSupportActionBar(), menu, inflater);
            if (!mCreateActionBarCalled) {
                throw new IllegalStateException("You must call super() in onCreateActionBar()");
            }
        }
    }

    /**
     * @return The target user sent to this DataFragment as an argument, or null if none exists.
     */
    public User getTargetUser() {
        return (User) mArgumentMap.get(ARG_TARGET_USER);
    }

    /**
     * @return The target issue sent to this DataFragment as an argument, or null if none exists.
     */
    public Issue getTargetIssue() {
        return (Issue) mArgumentMap.get(ARG_TARGET_ISSUE);
    }

    /**
     * @return The target repository sent to this DataFragment as an argument, or null if none
     *         exists.
     */
    public Repository getTargetRepo() {
        return (Repository) mArgumentMap.get(ARG_TARGET_REPO);
    }
}
