package com.gossip.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @Author: WK
 * @Data: 2019/7/22 20:27
 * @Description: com.gossip.pojo
 */
public class News implements Serializable {
    @Field
    private String id;
    @Field
    private String title;
    @Field
    private String time;
    @Field
    private String source;
    @Field
    private String content;
    @Field
    private String editor;
    @Field
    private String docurl;


    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                ", editor='" + editor + '\'' +
                ", docurl='" + docurl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }
}
