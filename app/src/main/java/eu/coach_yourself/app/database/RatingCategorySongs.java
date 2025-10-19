package eu.coach_yourself.app.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "RatingCategorySongs", id = "_id")
public class RatingCategorySongs extends Model {

    @Column(name = "categoryid")
    public String categoryid;

    @Column(name = "songsid")
    public String songsid;

    @Column(name = "categoryname")
    public String categoryname;

    @Column(name = "categoryColor")
    public String categoryColor;

    @Column(name = "categorytitle")
    public String categorytitle;

    @Column(name = "description")
    public String description;

    @Column(name = "ratingstype")
    public String ratingstype;

    @Column(name = "ratingstime")
    public String ratingstime;

    @Column(name = "ratingsmonth")
    public String ratingsmonth;

    @Column(name = "ratingsyear")
    public String ratingsyear;

    @Column(name = "ratingsnumber")
    public String ratingsnumber;

    @Column(name = "ratingsID")
    public String ratingsID;

    public String getRatingsID() {
        return ratingsID;
    }

    public void setRatingsID(String ratingsID) {
        this.ratingsID = ratingsID;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getRatingsmonth() {
        return ratingsmonth;
    }

    public void setRatingsmonth(String ratingsmonth) {
        this.ratingsmonth = ratingsmonth;
    }

    public String getRatingsyear() {
        return ratingsyear;
    }

    public void setRatingsyear(String ratingsyear) {
        this.ratingsyear = ratingsyear;
    }

    public String getRatingsnumber() {
        return ratingsnumber;
    }

    public void setRatingsnumber(String ratingsnumber) {
        this.ratingsnumber = ratingsnumber;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getSongsid() {
        return songsid;
    }

    public void setSongsid(String songsid) {
        this.songsid = songsid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRatingstype() {
        return ratingstype;
    }

    public void setRatingstype(String ratingstype) {
        this.ratingstype = ratingstype;
    }

    public String getRatingstime() {
        return ratingstime;
    }

    public void setRatingstime(String ratingstime) {
        this.ratingstime = ratingstime;
    }
}


