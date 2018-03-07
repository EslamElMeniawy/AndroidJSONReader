package elmeniawy.eslam.ytsag.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Torrent {
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("quality")
    @Expose
    private String quality;

    @SerializedName("size")
    @Expose
    private String size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
