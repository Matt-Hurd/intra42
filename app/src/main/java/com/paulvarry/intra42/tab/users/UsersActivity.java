package com.paulvarry.intra42.tab.users;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.paulvarry.intra42.Adapter.ViewPagerAdapter;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.AppSettings;
import com.paulvarry.intra42.ui.BasicTabActivity;
import com.paulvarry.intra42.ui.tools.Navigation;

public class UsersActivity
        extends BasicTabActivity
        implements UsersSearchFragment.OnFragmentInteractionListener, UsersAllFragment.OnFragmentInteractionListener,
        UsersAdvancedFragment.OnFragmentInteractionListener, UsersFriendsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.allowHamburger();
        super.onCreate(savedInstanceState);
        super.setSelectedMenu(Navigation.MENU_SELECTED_USERS);
    }

    @Nullable
    @Override
    public String getUrlIntra() {
        return null;
    }

    @Override
    public boolean getDataOnOtherThread() {
        return true;
    }

    @Override
    public boolean getDataOnMainThread() {
        return true;
    }

    @Override
    public String getToolbarName() {
        return null;
    }

    @Override
    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(UsersSearchFragment.newInstance(), getString(R.string.tab_users_search));
        if (AppSettings.Advanced.getAllowFriends(this))
            adapter.addFragment(UsersFriendsFragment.newInstance(), getString(R.string.tab_users_friends));
        adapter.addFragment(UsersAllFragment.newInstance(), getString(R.string.tab_users_all));
//        adapter.addFragment(UsersAdvancedFragment.newInstance(), getString(R.string.tab_users_advanced_search));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
