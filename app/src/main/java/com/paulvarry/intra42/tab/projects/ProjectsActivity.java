package com.paulvarry.intra42.tab.projects;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.paulvarry.intra42.Adapter.ViewPagerAdapter;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.ui.BasicTabActivity;
import com.paulvarry.intra42.ui.tools.Navigation;

public class ProjectsActivity extends BasicTabActivity
        implements ProjectsDoingFragment.OnFragmentInteractionListener, ProjectsAllFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        allowHamburger();
        super.onCreate(savedInstanceState);
        super.setSelectedMenu(Navigation.MENU_SELECTED_PROJECTS);
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
        return getString(R.string.projects);
    }

    @Override
    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProjectsDoingFragment.newInstance(), getString(R.string.tab_projects_doing));
        adapter.addFragment(ProjectsAllFragment.newInstance(), getString(R.string.tab_projects_all));
//        adapter.addFragment(ProjectsGraphFragment.newInstance(), getString(R.string.tab_projects_graphic));
        viewPager.setAdapter(adapter);
    }

    @Nullable
    @Override
    public String getUrlIntra() {
        return null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
