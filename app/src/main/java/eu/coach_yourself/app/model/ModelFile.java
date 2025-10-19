package eu.coach_yourself.app.model;

public class ModelFile {
    public String categoryname, categorytitle, Id, remind_title, remind_day, remind_time, rowid, remind_hr, remind_minut, description, contentname;
    public String card_image, logo_image, dec_img;
    public String text_color, design_color, subscription;
    public String favrowid, favcatname, favcontantname, favimg;
    public String contentId;

    public ModelFile() {

    }

    public ModelFile(String contentname, String dec_img, String description, String categoryname, String Id, String categorytitle, String card_image, String logo_image, String text_color, String design_color, String remind_day, String remind_time, String remind_title) {
        this.categoryname = categoryname;
        this.categorytitle = categorytitle;
        this.card_image = card_image;
        this.logo_image = logo_image;
        this.text_color = text_color;
        this.design_color = design_color;

        this.remind_day = remind_day;
        this.remind_time = remind_time;
        this.remind_title = remind_title;
        this.Id = Id;
        this.description = description;
        this.dec_img = dec_img;
        this.contentname = contentname;
    }

    public ModelFile(String contentname, String contentId) {
        this.contentname = contentname;
        this.contentId = contentId;
    }


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public ModelFile(String rowid, String txt_remidtitle, String txt_remindday, String txt_remindtime, String remind_hr, String remind_minut) {
        this.remind_day = txt_remindday;
        this.remind_time = txt_remindtime;
        this.remind_title = txt_remidtitle;
        this.rowid = rowid;
        this.remind_hr = remind_hr;
        this.remind_minut = remind_minut;

    }

    public ModelFile(String favrowid, String cat_name, String contantname, String favimg) {
        this.favrowid = favrowid;
        this.favcatname = cat_name;
        this.favcontantname = contantname;
        this.favimg = favimg;
    }

    public String getDesign_color() {
        return design_color;
    }

    public void setDesign_color(String design_color) {
        this.design_color = design_color;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getFavrowid() {
        return favrowid;
    }

    public void setFavrowid(String favrowid) {
        this.favrowid = favrowid;
    }

    public String getFavcatname() {
        return favcatname;
    }

    public void setFavcatname(String favcatname) {
        this.favcatname = favcatname;
    }

    public String getFavcontantname() {
        return favcontantname;
    }

    public void setFavcontantname(String favcontantname) {
        this.favcontantname = favcontantname;
    }

    public String getFavimg() {
        return favimg;
    }

    public void setFavimg(String favimg) {
        this.favimg = favimg;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public String getDec_img() {
        return dec_img;
    }

    public void setDec_img(String dec_img) {
        this.dec_img = dec_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemind_hr() {
        return remind_hr;
    }

    public void setRemind_hr(String remind_hr) {
        this.remind_hr = remind_hr;
    }

    public String getRemind_minut() {
        return remind_minut;
    }

    public void setRemind_minut(String remind_minut) {
        this.remind_minut = remind_minut;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }


    public String getRemind_title() {
        return remind_title;
    }

    public void setRemind_title(String remind_title) {
        this.remind_title = remind_title;
    }

    public String getRemind_day() {
        return remind_day;
    }

    public void setRemind_day(String remind_day) {
        this.remind_day = remind_day;
    }

    public String getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(String remind_time) {
        this.remind_time = remind_time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategorytitle() {
        return categorytitle;
    }

    public void setCategorytitle(String categorytitle) {
        this.categorytitle = categorytitle;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }
}
