package elmeniawy.eslam.ytsag.screens.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TorrentViewModel
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class TorrentViewModel implements Parcelable {
    //
    // Attributes.
    //

    private String url;
    private String quality;
    private String size;

    TorrentViewModel() {
    }

    private TorrentViewModel(Parcel in) {
        url = in.readString();
        quality = in.readString();
        size = in.readString();
    }

    public static final Creator<TorrentViewModel> CREATOR = new Creator<TorrentViewModel>() {
        @Override
        public TorrentViewModel createFromParcel(Parcel in) {
            return new TorrentViewModel(in);
        }

        @Override
        public TorrentViewModel[] newArray(int size) {
            return new TorrentViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(quality);
        dest.writeString(size);
    }

    //
    // Setters and getters.
    //

    public void setUrl(String url) {
        this.url = url;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public String getQuality() {
        return quality;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("url: %s, quality: %s, size: %s", url, quality, size);
    }
}
