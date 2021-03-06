package com.paulvarry.intra42.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.Tag;
import com.paulvarry.intra42.Tools.mImage;
import com.paulvarry.intra42.api.Achievements;
import com.paulvarry.intra42.tab.user.UserActivity;
import com.veinhorn.tagview.TagView;

import java.util.List;

public class GridAdapterAchievements extends BaseAdapter {

    private final UserActivity activity;
    List<Achievements> achievements;

    public GridAdapterAchievements(UserActivity activity, List<Achievements> achievements) {

        this.activity = activity;
        this.achievements = achievements;
    }

    @Override
    public int getCount() {
        return achievements.size();
    }

    @Override
    public Achievements getItem(int position) {
        return achievements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return achievements.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.grid_view_achievements, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.tagView = (TagView) convertView.findViewById(R.id.tagView);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewDescription);
            holder.textViewTier = (TextView) convertView.findViewById(R.id.textViewTier);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Achievements achievements = getItem(position);

        if (activity.picAchievements.containsKey(achievements.imageUrl))
            holder.imageView.setImageBitmap(activity.picAchievements.get(achievements.imageUrl));
        else
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap d = mImage.loadImageSVG("http://cdn.intra.42.fr" + achievements.imageUrl.replace("/uploads/", "/"));
                    if (d != null) {
                        activity.picAchievements.put(achievements.imageUrl, d);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.imageView.setImageBitmap(d);
                            }
                        });
                    }
                }
            }).start();

        Tag.setTagAchievement(activity, achievements, holder.tagView);
        holder.textViewName.setText(achievements.name);
        holder.textViewDescription.setText(achievements.description);
        holder.textViewTier.setText(achievements.kind);
        holder.progressBar.setProgress(100);

        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;
        private TextView textViewName;
        private TagView tagView;
        private TextView textViewDescription;
        private TextView textViewTier;
        private ProgressBar progressBar;
    }
}
