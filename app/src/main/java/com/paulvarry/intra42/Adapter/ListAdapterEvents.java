package com.paulvarry.intra42.Adapter;


import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.DateTool;
import com.paulvarry.intra42.Tools.Tag;
import com.paulvarry.intra42.api.Events;
import com.veinhorn.tagview.TagView;

import java.util.List;

import in.uncod.android.bypass.Bypass;

public class ListAdapterEvents extends BaseAdapter {

    private final Context context;
    private List<Events> eventsList;

    public ListAdapterEvents(Context context, List<Events> projectsList) {

        this.context = context;
        this.eventsList = projectsList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return eventsList.size();
    }

    /**
     * Get the data projectsList associated with the specified position in the data set.
     *
     * @param position Position of the projectsList whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Events getItem(int position) {
        return eventsList.get(position);
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

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_event, parent, false);

            holder.textViewDateDay = (TextView) convertView.findViewById(R.id.textViewDateDay);
            holder.textViewDateMonth = (TextView) convertView.findViewById(R.id.textViewDateMonth);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.tagViewKind = (TagView) convertView.findViewById(R.id.tagViewKind);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewDescription);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
            holder.textViewPlace = (TextView) convertView.findViewById(R.id.textViewPlace);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Events item = getItem(position);

        holder.textViewDateDay.setText(DateTool.getDay(item.beginAt));
        holder.textViewDateMonth.setText(DateTool.getMonthMedium(item.beginAt));
        holder.textViewName.setText(item.name);
        holder.textViewDescription.setText(item.description.replace('\n', ' '));

        Bypass b = new Bypass(context);
        String content_tmp = b.markdownToSpannable(item.description).toString().replace('\n', ' ');
        holder.textViewDescription.setText(content_tmp);

        String time;
        time = DateUtils.formatDateRange(context, item.beginAt.getTime(), item.endAt.getTime(), DateUtils.FORMAT_SHOW_TIME);
        if (time.length() > 30)
            time = time.replace(" – ", "\n");
        holder.textViewTime.setText(time);
        holder.textViewPlace.setText(item.location);

        Tag.setTagEvent(item, holder.tagViewKind);

        return convertView;
    }

    private static class ViewHolder {

        TextView textViewDateDay;
        TextView textViewDateMonth;
        TextView textViewName;
        TagView tagViewKind;
        TextView textViewDescription;
        TextView textViewTime;
        TextView textViewPlace;
    }
}