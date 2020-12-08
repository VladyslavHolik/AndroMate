package com.whiteursa.andromate;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.whiteursa.andromate.splash.SplashAsyncTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mAcivityTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    private SplashActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mAcivityTestRule.getActivity();
    }

    @Test
    public void testLaunch() {
        View view = mActivity.findViewById(R.id.appName);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}