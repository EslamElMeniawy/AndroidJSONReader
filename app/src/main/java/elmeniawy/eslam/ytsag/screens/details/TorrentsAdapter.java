package elmeniawy.eslam.ytsag.screens.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;

/**
 * TorrentsAdapter
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class TorrentsAdapter extends BaseAdapter {
    private Context context;
    private List<TorrentViewModel> torrents;

    @BindView(R.id.movie_quality_text)
    TextView quality;

    @BindView(R.id.movie_quality_size)
    TextView size;

    TorrentsAdapter(Context context, List<TorrentViewModel> torrents) {
        this.context = context;
        this.torrents = torrents;
    }

    @Override
    public int getCount() {
        return torrents.size();
    }

    @Override
    public Object getItem(int position) {
        return torrents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        gridView = new View(context);

        if (convertView == null) {
            if (inflater != null) {
                gridView = inflater.inflate(R.layout.torrent_item, null);
                ButterKnife.bind(this, gridView);
                TorrentViewModel currentTorrent = torrents.get(position);
                quality.setText(currentTorrent.getQuality());
                size.setText(currentTorrent.getSize());
            }
        } else {
            gridView = convertView;
        }

        return gridView;
    }
}
