package eu.coach_yourself.app.database;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import eu.coach_yourself.app.utils.DateUtility;

public class DatabaseQueryHelper {
    public static ArrayList<RatingCategorySongs> getRatingCategorySongs(String ratingType, String selectionMonth, String categoryName) {
        ArrayList<RatingCategorySongs> notiDBs = new ArrayList<RatingCategorySongs>();
        List<RatingCategorySongs> teamDB = new Select().all()
                .from(RatingCategorySongs.class)
                .where("ratingstype = ?", ratingType)
                .where("ratingsmonth = ?", selectionMonth)
                .where("categoryname = ?", categoryName)
                .where("ratingsyear = ?", DateUtility.getYearDate())
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;
    }
    public static RatingCategorySongs getRatingCategorySongs(String ratingType, String ratingsID) {
        return new Select()
                .from(RatingCategorySongs.class)
                .where("ratingstype = ?", ratingType)
                .where("ratingsID = ?", ratingsID)
                .executeSingle();
    }
//    public static ArrayList<RatingCategorySongs> getRatingCategorySongs(String selectionMonth) {
//        ArrayList<RatingCategorySongs> notiDBs = new ArrayList<RatingCategorySongs>();
//        List<RatingCategorySongs> teamDB = new Select().all()
//                .from(RatingCategorySongs.class)
//                .where("ratingsmonth = ?", selectionMonth)
//                .groupBy("categoryname")
//                .where("ratingsyear = ?", DateUtility.getYearDate())
//                .execute();
//        notiDBs.addAll(teamDB);
//        return notiDBs;
//    }

    public static ArrayList<RatingCategorySongs> getGroupby() {
        ArrayList<RatingCategorySongs> notiDBs = new ArrayList<RatingCategorySongs>();
        List<RatingCategorySongs> teamDB = new Select().all()
                .from(RatingCategorySongs.class)
                .groupBy("categoryname")
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;

    }


    public static void DeleteAllMaintainMachin() {
        new Delete().from(RatingCategorySongs.class).execute();
    }

    public static void DeleteMaintainMachin(long nrcartao) {
        new Delete().from(RatingCategorySongs.class).where("_id = ?", nrcartao).execute();
    }

    public static ArrayList<FavouriteSongs> getFavouriteSongs() {
        ArrayList<FavouriteSongs> notiDBs = new ArrayList<FavouriteSongs>();
        List<FavouriteSongs> teamDB = new Select().all()
                .from(FavouriteSongs.class)
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;
    }

    public static FavouriteSongs getFavouriteSongs(String categoryid, String songsID) {
        return new Select()
                .from(FavouriteSongs.class)
                .where("categoryid = ?", categoryid)
                .where("songsID = ?", songsID)
                .executeSingle();
    }

    public static void DeleteFavouriteSongs(String categoryid, String songsID) {
        new Delete().from(FavouriteSongs.class)
                .where("categoryid = ?", categoryid)
                .where("songsID = ?", songsID)
                .execute();
    }

    public static ArrayList<PlaySongs> getallPlaySongs() {
        ArrayList<PlaySongs> notiDBs = new ArrayList<PlaySongs>();
        List<PlaySongs> teamDB = new Select().all()
                .from(PlaySongs.class)
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;
    }

    public static ArrayList<PlaySongs> getPlaySongs() {
        ArrayList<PlaySongs> notiDBs = new ArrayList<PlaySongs>();
        List<PlaySongs> teamDB = new Select().all()
                .from(PlaySongs.class)
                .groupBy("categoryid")
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;

    }

    public static ArrayList<PlaySongs> getPlaySongsbycategoryid(String categoryid) {
        ArrayList<PlaySongs> notiDBs = new ArrayList<PlaySongs>();
        List<PlaySongs> teamDB = new Select().all()
                .from(PlaySongs.class)
                .where("categoryid = ?", categoryid)
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;

    }

    public static ArrayList<AlarmData> getAlarmData(String remainderID) {
        ArrayList<AlarmData> notiDBs = new ArrayList<AlarmData>();
        List<AlarmData> teamDB = new Select().all()
                .from(AlarmData.class)
                .where("remainderID = ?", remainderID)
                .execute();
        notiDBs.addAll(teamDB);
        return notiDBs;
    }
    public static void DeleteAlarmData(String remainderID) {
        new Delete().from(AlarmData.class)
                .where("remainderID = ?", remainderID)
                .execute();
    }

}
