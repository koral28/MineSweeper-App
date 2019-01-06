package malknaor.android.minesweeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * TableAdapter Sets adapter for the Score Table List
 */
public class TableAdapter extends ArrayAdapter {
    private static String TAG = "FeedAdapter";

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<TableEntry> playersList;

    public TableAdapter(Context context, int resource, List<TableEntry> playersList) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.playersList = playersList;
    }

    @Override
    public int getCount() {
        return playersList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            Log.d(TAG, "getView: called with null convertView");
            try {
                convertView = layoutInflater.inflate(this.layoutResource, parent, false);
            } catch (Exception e) {
                Log.e(TAG, String.format("getView: layoutResource = %s, parent = %s\n", layoutResource, parent.getId()) + e.getMessage());
                e.printStackTrace();
            }

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: provided a convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TableEntry currentEntry = playersList.get(position);

        viewHolder.tvName.setText(currentEntry.getPlayerName());
        viewHolder.tvDifficulty.setText(currentEntry.getPlayerDifficulty());
        viewHolder.tvSummary.setText(currentEntry.getPlayerTime());

        return convertView;
    }

    private class ViewHolder {
        final TextView tvName;
        final TextView tvDifficulty;
        final TextView tvSummary;

        ViewHolder(View v) {
            this.tvName = (TextView) v.findViewById(R.id.player_name_text_view);
            this.tvDifficulty = (TextView) v.findViewById(R.id.player_difficulty_text_view);
            this.tvSummary = (TextView) v.findViewById(R.id.player_time_text_view);
        }
    }
}
