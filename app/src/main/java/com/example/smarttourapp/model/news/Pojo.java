package com.example.smarttourapp.model.news;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pojo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("related")
    @Expose
    private List<Object> related = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Object> getRelated() {
        return related;
    }

    public void setRelated(List<Object> related) {
        this.related = related;
    }

}
