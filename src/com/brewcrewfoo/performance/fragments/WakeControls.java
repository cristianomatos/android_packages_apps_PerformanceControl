/*
 * Performance Control - An Android CPU Control application Copyright (C) 2012
 * James Roberts
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.brewcrewfoo.performance.fragments;

import android.app.ActivityManager;
import android.os.SystemProperties;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.brewcrewfoo.performance.R;
import com.brewcrewfoo.performance.activities.PCSettings;
import com.brewcrewfoo.performance.util.CMDProcessor;
import com.brewcrewfoo.performance.util.Constants;
import com.brewcrewfoo.performance.util.Helpers;

import java.io.File;

public class WakeControls extends PreferenceFragment implements Constants {

    private CheckBoxPreference mDt2w;
    private CheckBoxPreference mS2w;
    private CheckBoxPreference mS2sOnly;
    private CheckBoxPreference mPkeySuspend;
 
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getActivity();
        addPreferencesFromResource(R.xml.wake_controls);

        mDt2w = (CheckBoxPreference) findPreference(DT2W_PATH);
        mS2w = (CheckBoxPreference) findPreference(S2W_PATH);
        mS2sOnly = (CheckBoxPreference) findPreference(S2W_SLEEPONLY_PATH);
        mPkeySuspend = (CheckBoxPreference) findPreference(POWER_KEY_SUSPEND_PATH);
        
        if (!new File(DT2W_PATH).exists()) {
            PreferenceCategory hideCat = (PreferenceCategory) findPreference("dt2w");
            getPreferenceScreen().removePreference(hideCat);
        } else {
            mDt2w.setChecked(Helpers.readOneLine(DT2W_PATH).equals("1"));
        }

        if (!new File(S2W_PATH).exists()) {
            PreferenceCategory hideCat = (PreferenceCategory) findPreference("s2w");
            getPreferenceScreen().removePreference(hideCat);
        } else {
            mS2w.setChecked(Helpers.readOneLine(S2W_PATH).equals("1"));
        }

        if (!new File(S2W_SLEEPONLY_PATH).exists()) {
            PreferenceCategory hideCat = (PreferenceCategory) findPreference("s2sonly");
            getPreferenceScreen().removePreference(hideCat);
        } else {
            mS2sOnly.setChecked(Helpers.readOneLine(S2W_SLEEPONLY_PATH).equals("1"));
        }

        if (!new File(POWER_KEY_SUSPEND_PATH).exists()) {
            PreferenceCategory hideCat = (PreferenceCategory) findPreference("pkeysuspend");
            getPreferenceScreen().removePreference(hideCat);
        } else {
            mPkeySuspend.setChecked(Helpers.readOneLine(POWER_KEY_SUSPEND_PATH).equals("1"));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!getResources().getBoolean(R.bool.config_showPerformanceOnly)) {
            inflater.inflate(R.menu.wake_control_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_settings:
                Intent intent = new Intent(context, PCSettings.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (preference == mDt2w) {
            if (Integer.parseInt(Helpers.readOneLine(DT2W_PATH)) == 0) {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(DT2W_PATH, "1");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 1 > " + DT2W_PATH);
                }
            } else {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(DT2W_PATH, "0");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 0 > " + DT2W_PATH);
                }
            }
            return true;
        } else if (preference == mS2w) {
            if (Integer.parseInt(Helpers.readOneLine(S2W_PATH)) == 0) {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(S2W_PATH, "1");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 1 > " + S2W_PATH);
                }
            } else {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(S2W_PATH, "0");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 0 > " + S2W_PATH);
                }
            }
            return true;
        } else if (preference == mS2sOnly) {
            if (Integer.parseInt(Helpers.readOneLine(S2W_SLEEPONLY_PATH)) == 0) {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(S2W_SLEEPONLY_PATH, "1");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 1 > " + S2W_SLEEPONLY_PATH);
                }
            } else {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(S2W_SLEEPONLY_PATH, "0");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 0 > " + S2W_SLEEPONLY_PATH);
                }
            }
            return true;
        } else if (preference == mPkeySuspend) {
            if (Integer.parseInt(Helpers.readOneLine(POWER_KEY_SUSPEND_PATH)) == 0) {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(POWER_KEY_SUSPEND_PATH, "1");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 1 > " + POWER_KEY_SUSPEND_PATH);
                }
            } else {
                if (Helpers.isSystemApp(getActivity())) {
                    Helpers.writeOneLine(POWER_KEY_SUSPEND_PATH, "0");
                } else {
                    new CMDProcessor().su.runWaitFor("busybox echo 0 > " + POWER_KEY_SUSPEND_PATH);
                }
            }
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
