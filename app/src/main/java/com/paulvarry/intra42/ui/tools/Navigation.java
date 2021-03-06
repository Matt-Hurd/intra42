package com.paulvarry.intra42.ui.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import com.paulvarry.intra42.AppClass;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.ApiParams;
import com.paulvarry.intra42.activity.AboutActivity;
import com.paulvarry.intra42.activity.MainActivity;
import com.paulvarry.intra42.activity.SettingsActivity;
import com.paulvarry.intra42.tab.forum.ForumActivity;
import com.paulvarry.intra42.tab.home.HomeActivity;
import com.paulvarry.intra42.tab.notions.NotionsActivity;
import com.paulvarry.intra42.tab.projects.ProjectsActivity;
import com.paulvarry.intra42.tab.users.UsersActivity;

import static com.paulvarry.intra42.AppClass.CACHE_API_CAMPUS;
import static com.paulvarry.intra42.AppClass.CACHE_API_CURSUS;
import static com.paulvarry.intra42.AppClass.CACHE_API_ME;

public class Navigation {

    public static final int MENU_SELECTED_HOME = 0;
    public static final int MENU_SELECTED_USERS = 1;
    public static final int MENU_SELECTED_PROJECTS = 2;
    public static final int MENU_SELECTED_FORUM = 3;
    public static final int MENU_SELECTED_ELEARNING = 4;


    public static boolean onNavigationItemSelected(Activity activity, MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else if (id == R.id.nav_users) {
            Intent intent = new Intent(activity, UsersActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else if (id == R.id.nav_projects) {
            Intent intent = new Intent(activity, ProjectsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else if (id == R.id.nav_forum) {
            Intent intent = new Intent(activity, ForumActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else if (id == R.id.nav_elearning) {
            Intent intent = new Intent(activity, NotionsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(activity, AboutActivity.class);
            activity.startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.paulvarry.intra42");
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        } else if (id == R.id.nav_logout) {
            ((AppClass) activity.getApplication()).logout();
            SharedPreferences sharedPreferences = ApiParams.getSharedPreferences(activity);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CACHE_API_ME);
            editor.remove(CACHE_API_CURSUS);
            editor.remove(CACHE_API_CAMPUS);
            editor.apply();

            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
            activity.finish();
        } else if (id == R.id.nav_settings) {
            final Intent i = new Intent(activity, SettingsActivity.class);
            activity.startActivity(i);
        }
        return true;
    }
}
