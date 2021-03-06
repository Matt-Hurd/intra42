package com.paulvarry.intra42.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paulvarry.intra42.R;
import com.paulvarry.intra42.api.TeamsUploads;

import java.util.List;

public class ListAdapterScaleTeamsAutomatic extends BaseAdapter {

    private final Activity context;
    List<TeamsUploads> list;
    int flag;

    public ListAdapterScaleTeamsAutomatic(Activity context, List<TeamsUploads> list) {

        this.context = context;
        this.list = list;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the data projectsList associated with the specified position in the data set.
     *
     * @param position Position of the projectsList whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public TeamsUploads getItem(int position) {
        return list.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the projectsList within the adapter's data set whose row id we want.
     * @return The id of the projectsList at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_view_scale_teams, parent, false);

            holder.imageViewUser = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textViewCorrector = (TextView) convertView.findViewById(R.id.textViewCorrector);
            holder.textViewScale = (TextView) convertView.findViewById(R.id.textViewScale);
            holder.textViewComment = (TextView) convertView.findViewById(R.id.textViewComment);

            holder.imageViewIconStatus = (ImageView) convertView.findViewById(R.id.imageViewIconStatus);
            holder.linearLayoutFeedback = (LinearLayout) convertView.findViewById(R.id.linearLayoutFeedback);
            holder.linearLayoutFeedbackMark = (LinearLayout) convertView.findViewById(R.id.linearLayoutFeedbackMark);
            holder.textViewFeedbackInterested = (TextView) convertView.findViewById(R.id.textViewDescription);
            holder.textViewFeedbackNice = (TextView) convertView.findViewById(R.id.textViewFeedbackNice);
            holder.textViewFeedbackPunctuality = (TextView) convertView.findViewById(R.id.textViewFeedbackPunctuality);
            holder.textViewFeedbackRigorous = (TextView) convertView.findViewById(R.id.textViewFeedbackRigorous);
            holder.textViewFeedback = (TextView) convertView.findViewById(R.id.textViewFeedback);
            holder.textViewFeedbackStars = (TextView) convertView.findViewById(R.id.textViewFeedbackStars);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TeamsUploads item = getItem(position);

        holder.textViewCorrector.setText(R.string.moulinette);
        holder.textViewScale.setText(String.valueOf(item.finalMark));
        holder.textViewComment.setText(item.comment);
        holder.linearLayoutFeedback.setVisibility(View.GONE);

        return convertView;
    }

    private static class ViewHolder {

        private TextView textViewComment;
        private ImageView imageViewIconStatus;
        private LinearLayout linearLayoutFeedback;
        private LinearLayout linearLayoutFeedbackMark;
        private TextView textViewFeedbackInterested;
        private TextView textViewFeedbackNice;
        private TextView textViewFeedbackPunctuality;
        private TextView textViewFeedbackRigorous;
        private TextView textViewFeedback;
        private TextView textViewFeedbackStars;
        private ImageView imageViewUser;
        private TextView textViewCorrector;
        private TextView textViewScale;
    }
}
