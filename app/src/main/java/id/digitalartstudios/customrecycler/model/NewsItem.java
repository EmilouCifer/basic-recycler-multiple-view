package id.digitalartstudios.customrecycler.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryan on 02/10/2017.
 */

public class NewsItem {
    String title, content, time, source, thumb, sourceImages;
    int layoutType;

    public NewsItem() {
    }

    public NewsItem(String title, String content, String time, String source, String thumb, String sourceImages) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.source = source;
        this.thumb = thumb;
        this.sourceImages = sourceImages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSourceImages() {
        return sourceImages;
    }

    public void setSourceImages(String sourceImages) {
        this.sourceImages = sourceImages;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    @Override
    public String toString() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("title", title);
//        stringMap.put("content", content);
        stringMap.put("time", time);
        stringMap.put("source", source);
        stringMap.put("thumb", thumb);
        stringMap.put("sourceImages", sourceImages);
        stringMap.put("layoutType", String.valueOf(layoutType));

        return stringMap.toString();
    }

}
