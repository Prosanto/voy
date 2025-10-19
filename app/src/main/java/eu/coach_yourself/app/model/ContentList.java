package eu.coach_yourself.app.model;

public class ContentList {
    private String intId="";
    private String category_id="";
    private String contentname="";
    private String description="";
    private String mp3file="";
    private String subscription="";
    private String status="";

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMp3file() {
        return mp3file;
    }

    public void setMp3file(String mp3file) {
        this.mp3file = mp3file;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
