package com.paulvarry.intra42.tab.tags;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.paulvarry.intra42.Adapter.ViewPagerAdapter;
import com.paulvarry.intra42.AppClass;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.api.Tags;
import com.paulvarry.intra42.oauth.ServiceGenerator;
import com.paulvarry.intra42.tab.user.UserActivity;
import com.paulvarry.intra42.ui.BasicTabActivity;

import java.io.IOException;

import retrofit2.Response;

public class TagsActivity
        extends BasicTabActivity
        implements TagsForumFragment.OnFragmentInteractionListener, TagsNotionsFragment.OnFragmentInteractionListener, TagsProjectsFragment.OnFragmentInteractionListener {

    private static final String INTENT_TAG = "login";
    private static final String INTENT_TAG_ID = "tag_id";
    public Tags tag;
    AppClass app;
    int tagId;

    public static boolean openIt(Context context, Tags tag) {
        if (tag != null) {
            Intent intent = new Intent(context, UserActivity.class);
            intent.putExtra(INTENT_TAG, ServiceGenerator.getGson().toJson(tag));
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean openIt(Context context, int tag) {

        Intent intent = new Intent(context, TagsActivity.class);
        intent.putExtra(INTENT_TAG_ID, tag);
        context.startActivity(intent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (AppClass) getApplication();

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_TAG_ID))
            tagId = intent.getIntExtra(INTENT_TAG_ID, -1);

        if (intent.hasExtra(INTENT_TAG))
            ServiceGenerator.getGson().fromJson(intent.getStringExtra(INTENT_TAG), Tags.class);

        super.allowHamburger();
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean getDataOnOtherThread() {
        if (tag == null) {
            try {
                Response<Tags> call = app.getApiService().getTag(tagId).execute();
                if (call.isSuccessful())
                    tag = call.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean getDataOnMainThread() {
        return false;
    }

    @Override
    public String getToolbarName() {
        if (tag != null)
            return tag.name;
        return String.valueOf(tagId);
    }

    @Override
    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(TagsProjectsFragment.newInstance(), getString(R.string.tab_tags_projects));
        adapter.addFragment(TagsForumFragment.newInstance(), getString(R.string.tab_tags_forum));
        adapter.addFragment(TagsNotionsFragment.newInstance(), getString(R.string.tab_tags_notions));
        viewPager.setAdapter(adapter);
    }

    public String getUrlIntra() {
        return null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
