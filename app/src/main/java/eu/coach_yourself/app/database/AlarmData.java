package eu.coach_yourself.app.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "AlarmData", id = "_id")
public class AlarmData extends Model {
    @Column(name = "remainderID")
    private String remainderID="";
    @Column(name = "remainderTime")
    private String remainderTime="";
    @Column(name = "remainderText")
    private String remainderText="";

    public String getRemainderID() {
        return remainderID;
    }

    public void setRemainderID(String remainderID) {
        this.remainderID = remainderID;
    }

    public String getRemainderTime() {
        return remainderTime;
    }

    public void setRemainderTime(String remainderTime) {
        this.remainderTime = remainderTime;
    }

    public String getRemainderText() {
        return remainderText;
    }

    public void setRemainderText(String remainderText) {
        this.remainderText = remainderText;
    }
}
