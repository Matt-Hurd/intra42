package com.paulvarry.intra42.Adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.UserImage;
import com.paulvarry.intra42.api.ScaleTeams;

import java.util.List;

public class ListAdapterScaleTeams extends BaseAdapter {

    private final Activity context;
    List<ScaleTeams> list;
    int flag;

    public ListAdapterScaleTeams(Activity context, List<ScaleTeams> list) {

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
        if (list != null)
            return list.size();
        return 0;
    }

    /**
     * Get the data projectsList associated with the specified position in the data set.
     *
     * @param position Position of the projectsList whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public ScaleTeams getItem(int position) {
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

        ScaleTeams item = getItem(position);
        if (item.corrector != null) {
            UserImage.setImage(context, item.corrector, holder.imageViewUser);
            holder.textViewCorrector.setText(item.corrector.login);
        }

        holder.textViewScale.setText(String.valueOf(item.finalMark));
        holder.textViewComment.setText(item.comment);
        holder.textViewFeedback.setText(item.feedback);
        holder.textViewFeedbackStars.setText(item.feedback_rating);
        if (item.flag.positive) {
            holder.imageViewIconStatus.setImageResource(R.drawable.ic_check_black_24dp);
            holder.imageViewIconStatus.setColorFilter(ContextCompat.getColor(context, R.color.colorTintCheck));
        } else {
            holder.imageViewIconStatus.setImageResource(R.drawable.ic_close_black_24dp);
            holder.imageViewIconStatus.setColorFilter(ContextCompat.getColor(context, R.color.colorTintCross));
        }

        if (item.feedbacks != null) {
            holder.linearLayoutFeedbackMark.setVisibility(View.VISIBLE);
            for (ScaleTeams.Feedback f : item.feedbacks) {
                switch (f.kind) {
                    case "nice":
                        holder.textViewFeedbackNice.setText(String.valueOf(f.rate));
                        break;
                    case "rigorous":
                        holder.textViewFeedbackRigorous.setText(String.valueOf(f.rate));
                        break;
                    case "interested":
                        holder.textViewFeedbackInterested.setText(String.valueOf(f.rate));
                        break;
                    case "punctuality":
                        holder.textViewFeedbackPunctuality.setText(String.valueOf(f.rate));
                        break;
                    default:
                        break;
                }
            }
        }

        return convertView;
    }

    private static class ViewHolder {

        private ImageView imageViewUser;
        private TextView textViewCorrector;
        private TextView textViewScale;
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
    }
}