
package elmeniawy.eslam.ytsag.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("version")
    @Expose
    private Long version;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
