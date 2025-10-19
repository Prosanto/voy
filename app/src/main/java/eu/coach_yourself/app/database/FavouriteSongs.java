package eu.coach_yourself.app.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "FavouriteSongs", id = "_id")
public class FavouriteSongs extends Model {
    @Column(name = "categoryid")
    public String categoryid;
    @Column(name = "categoryname")
    public String categoryname;
    @Column(name = "categorytitle")
    public String categorytitle;
    @Column(name = "language")
    public String language;
    @Column(name = "description")
    public String description;
    @Column(name = "text_color")
    public String text_color;
    @Column(name = "design_color")
    public String design_color;
    @Column(name = "category_iconimage")
    public String category_iconimage;
    @Column(name = "card_image")
    public String card_image;
    @Column(name = "category_infoiconimage")
    public String category_infoiconimage;
    @Column(name = "subscription")
    public String subscription;
    @Column(name = "songsID")
    public String songsID;
    @Column(name = "contentname")
    public String contentname;
    @Column(name = "songdescription")
    public String songdescription;
    @Column(name = "mp3file")
    public String mp3file;
    @Column(name = "songsubscription")
    public String songsubscription;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getDesign_color() {
        return design_color;
    }

    public void setDesign_color(String design_color) {
        this.design_color = design_color;
    }

    public String getCategory_iconimage() {
        return category_iconimage;
    }

    public void setCategory_iconimage(String category_iconimage) {
        this.category_iconimage = category_iconimage;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getCategory_infoiconimage() {
        return category_infoiconimage;
    }

    public void setCategory_infoiconimage(String category_infoiconimage) {
        this.category_infoiconimage = category_infoiconimage;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getSongsID() {
        return songsID;
    }

    public void setSongsID(String songsID) {
        this.songsID = songsID;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public String getSongdescription() {
        return songdescription;
    }

    public void setSongdescription(String songdescription) {
        this.songdescription = songdescription;
    }

    public String getMp3file() {
        return mp3file;
    }

    public void setMp3file(String mp3file) {
        this.mp3file = mp3file;
    }

    public String getSongsubscription() {
        return songsubscription;
    }

    public void setSongsubscription(String songsubscription) {
        this.songsubscription = songsubscription;
    }
}
