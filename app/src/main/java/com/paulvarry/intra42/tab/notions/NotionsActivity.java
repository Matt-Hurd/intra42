package com.paulvarry.intra42.tab.notions;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.paulvarry.intra42.Adapter.ViewPagerAdapter;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.ui.BasicTabActivity;
import com.paulvarry.intra42.ui.tools.Navigation;

public class NotionsActivity
        extends BasicTabActivity
        implements NotionsAllFragment.OnFragmentInteractionListener, NotionsTagFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.allowHamburger();
        super.onCreate(savedInstanceState);
        super.setSelectedMenu(Navigation.MENU_SELECTED_ELEARNING);
    }

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

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NotionsAllFragment.newInstance(), getString(R.string.tab_elearning_all));
        adapter.addFragment(NotionsTagFragment.newInstance(), getString(R.string.tab_elearning_tags));
        viewPager.setAdapter(adapter);
    }

    public String getUrlIntra() {
        return "https://elearning.intra.42.fr/";
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
