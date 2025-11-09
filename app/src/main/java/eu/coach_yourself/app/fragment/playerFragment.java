package eu.coach_yourself.app.fragment;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eu.coach_yourself.app.DownloadedActivity;
import eu.coach_yourself.app.MyIntro;
import eu.coach_yourself.app.R;
import eu.coach_yourself.app.database.DatabaseQueryHelper;
import eu.coach_yourself.app.database.FavouriteSongs;
import eu.coach_yourself.app.database.PlaySongs;
import eu.coach_yourself.app.database.RatingCategorySongs;
import eu.coach_yourself.app.model.ContentList;
import eu.coach_yourself.app.model.ModelFile;
import eu.coach_yourself.app.model.Playlist;
import eu.coach_yourself.app.myapplication.Myapplication;
import eu.coach_yourself.app.reservoir.Reservoir;
import eu.coach_yourself.app.service.MyMediaPlayerService;
import eu.coach_yourself.app.utils.AlertMessage;
import eu.coach_yourself.app.utils.AllUrls;
import eu.coach_yourself.app.utils.ConstantFunctions;
import eu.coach_yourself.app.utils.DateUtility;
import eu.coach_yourself.app.utils.Helpers;
import eu.coach_yourself.app.utils.PersistentUser;
import eu.coach_yourself.app.utils.PrefManager;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class playerFragment extends BaseFragment {
    ImageButton btn_detail, btn_share;
    ToggleButton btn_favourite;
    private TextView tv_title, txt_duration, txt_duration_play;
    private ImageView img_play_logo;
    private SeekBar seekBar2;
    private CircularSeekBar seekbar;
    //String url_media;
    Spinner spin_time;
    Spinner spin_style;
    ArrayList<String> contentnameArray;
    private ArrayList<ContentList> allLists = new ArrayList<>();
    private AudioManager leftAm;
    private boolean flag = false;
    private boolean flagBackground = false;
    private boolean isNewAudio = false;
    private ModelFile mModelFile = null;
    private PrefManager prefManager;
    private ImageView btn_play;
    private ImageView btn_download_delete;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");
        wakeLock.acquire();

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarId);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
        RelativeLayout fragment_player = (RelativeLayout) view.findViewById(R.id.fragment_player);
        fragment_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        prefManager = new PrefManager(getContext());
        Bundle bundle = this.getArguments();
        mModelFile = Myapplication.mModelFile;
        seekbar = (CircularSeekBar) view.findViewById(R.id.seekbar);
        seekBar2 = (SeekBar) view.findViewById(R.id.seekbar2);
        seekBar2.setMax(100);
        seekBar2.setProgress(50);

        btn_download_delete = (ImageView) view.findViewById(R.id.btn_download_delete);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        txt_duration = (TextView) view.findViewById(R.id.txt_duration);
        txt_duration_play = (TextView) view.findViewById(R.id.txt_duration_play);
        img_play_logo = (ImageView) view.findViewById(R.id.img_play_logo);
        tv_title.setTextColor(Color.parseColor(mModelFile.getText_color()));
        tv_title.setText(mModelFile.getCategoryname());
        txt_duration_play.setTextColor(Color.parseColor(mModelFile.getText_color()));
        txt_duration.setTextColor(Color.parseColor(mModelFile.getText_color()));
        btn_favourite = (ToggleButton) view.findViewById(R.id.btn_favourite);
        Picasso.with(getContext()).load(mModelFile.getDec_img()).into(img_play_logo);

        btn_detail = (ImageButton) view.findViewById(R.id.btn_query);
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new description_fragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fragment_player, fragment)
                        .addToBackStack("my_fragment")
                        .commit();
            }
        });
        btn_share = (ImageButton) view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String app_url = getActivity().getResources().getString(R.string.share_text) + " \nhttps://voy-app.com/";
                shareIntent.putExtra(Intent.EXTRA_TEXT, getActivity().getResources().getString(R.string.share_text));
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        btn_play = (ImageView) view.findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allLists.size() > 0) {

                    int postion = spin_time.getSelectedItemPosition();
                    ContentList mModelFile = allLists.get(postion);

                    if (mModelFile.getSubscription().equalsIgnoreCase("0")) {
                        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("my-event"));
                        playSongsCalculation();
                    } else {
                        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("my-event"));
                        playSongsCalculation();


//                        boolean monthSubscription = PersistentUser.iSMonthlysubscription(getActivity());
//                        boolean yearSubscription = PersistentUser.iSYearlysubscription(getActivity());
//
//                        if (monthSubscription || yearSubscription) {
//                            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("my-event"));
//                            playSongsCalculation();
//                        } else {
//                            Fragment fragment = new purchase();
//                            fragment.setArguments(bundle);
//                            FragmentManager fragmentManager = getChildFragmentManager();
//                            fragmentManager.beginTransaction().add(R.id.fragment_player, fragment)
//                                    .addToBackStack("my_fragment_purchase")
//                                    .commit();
//                        }
                    }

                }

            }
        });
        btn_download_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 33) {
                    String[] permissions = {Manifest.permission.READ_MEDIA_AUDIO};
                    Permissions.check(getActivity(), permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            CheckEventStart();
                        }

                    });

                } else {

                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    Permissions.check(getActivity()/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            CheckEventStart();
                        }
                    });
                }

                // ValidationPermsion();

            }
        });

        spin_style = (Spinner) view.findViewById(R.id.spin_style);
        spin_time = (Spinner) view.findViewById(R.id.spin_time);
        String[] s1 = getResources().getStringArray(R.array.Spin1);
        @SuppressLint("ResourceType")
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, s1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_style.setAdapter(dataAdapter);
        spin_style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor(mModelFile.getText_color()));
                if (flagBackground) {
                    flagBackground = false;
                    int postion = spin_style.getSelectedItemPosition();
                    if (postion == 0) {
                        Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                        intent.putExtra(MyMediaPlayerService.BACKGROUND_STOP, true);
                        intent.putExtra("currentBackgroundSongs", postion);
                        getActivity().startService(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                        intent.putExtra(MyMediaPlayerService.BACKGROUND_PLAY, true);
                        intent.putExtra("currentBackgroundSongs", postion);
                        getActivity().startService(intent);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        contentnameArray = new ArrayList<>();
        spin_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor(mModelFile.getText_color()));
                if (flag && allLists.size() != 0) {

                    flag = false;
                    isNewAudio = true;
                    checkFavourite();
                    int postion = 0;
                    Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                    intent.putExtra(MyMediaPlayerService.STOPP_LAY, true);
                    intent.putExtra("postion", postion);
                    getActivity().startService(intent);
                    downloadVideoCheck();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spin_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flag = true;
                return false;
            }
        });
        spin_style.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flagBackground = true;
                return false;
            }
        });
        btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_favourite.isChecked()) {
                    if (allLists.size() > 0) {
                        int postion = spin_time.getSelectedItemPosition();
                        ContentList mContentList = allLists.get(postion);
                        FavouriteSongs mFavouriteSongs = new FavouriteSongs();
                        mFavouriteSongs.setCategoryid(mModelFile.getId());
                        mFavouriteSongs.setCategoryname(mModelFile.getCategoryname());
                        mFavouriteSongs.setCategorytitle(mModelFile.getCategorytitle());
                        mFavouriteSongs.setDescription(mModelFile.getDescription());
                        mFavouriteSongs.setText_color(mModelFile.getText_color());
                        mFavouriteSongs.setDesign_color(mModelFile.getDesign_color());
                        mFavouriteSongs.setCategory_iconimage(mModelFile.getDec_img());
                        mFavouriteSongs.setCard_image(mModelFile.getCard_image());
                        mFavouriteSongs.setCategory_infoiconimage(mModelFile.getLogo_image());
                        mFavouriteSongs.setSubscription(mModelFile.getSubscription());
                        mFavouriteSongs.setSongsID(mContentList.getIntId());
                        mFavouriteSongs.setContentname(mContentList.getContentname());
                        mFavouriteSongs.setSongdescription(mContentList.getDescription());
                        mFavouriteSongs.setSongsubscription(mContentList.getSubscription());
                        mFavouriteSongs.setMp3file(mContentList.getMp3file());
                        FavouriteSongs mFavouriteSongsaa = DatabaseQueryHelper.getFavouriteSongs(mContentList.getCategory_id(), mContentList.getIntId());
                        if (mFavouriteSongsaa == null) {
                            mFavouriteSongs.save();

                        }
                    }

                } else {
                    if (allLists.size() > 0) {
                        btn_favourite.setChecked(false);
                        int selection = spin_time.getSelectedItemPosition();
                        ContentList mContentList = allLists.get(selection);
                        FavouriteSongs mFavouriteSongs = DatabaseQueryHelper.getFavouriteSongs(mContentList.getCategory_id(), mContentList.getIntId());
                        if (mFavouriteSongs != null) {
                            DatabaseQueryHelper.DeleteFavouriteSongs(mContentList.getCategory_id(), mContentList.getIntId());
                        }
                    }

                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress,
                                          boolean fromUser) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if (MyMediaPlayerService.commonPlaylistcontent != null) {
                    Playlist m = MyMediaPlayerService.commonPlaylistcontent;
                    if (m.getCategory_id().equalsIgnoreCase(mModelFile.getId())) {
                        int pro = (int) seekBar.getProgress();
                        Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                        intent.putExtra(MyMediaPlayerService.SEEK_TO, true);
                        intent.putExtra("seek", pro);
                        intent.putExtra("seekTuch", false);
                        getActivity().startService(intent);
                    } else {
                        txt_duration_play.setText("0:0");
                        txt_duration.setText("0:0");
                        seekbar.setProgress(0);
                    }
                } else {
                    txt_duration_play.setText("0:0");
                    txt_duration.setText("0:0");
                    seekbar.setProgress(0);
                }

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                if (MyMediaPlayerService.commonPlaylistcontent != null) {
                    Playlist m = MyMediaPlayerService.commonPlaylistcontent;
                    if (m.getCategory_id().equalsIgnoreCase(mModelFile.getId())) {
                        int pro = (int) seekBar.getProgress();
                        Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                        intent.putExtra(MyMediaPlayerService.SEEK_TO, true);
                        intent.putExtra("seek", pro);
                        intent.putExtra("seekTuch", true);
                        getActivity().startService(intent);
                    }
                } else {
                    txt_duration_play.setText("0:0");
                    txt_duration.setText("0:0");
                    seekbar.setProgress(0);
                }


            }
        });

        if (MyMediaPlayerService.commonPlaylistcontent != null) {
            Log.e("Prosanto", "A");
            isNewAudio = false;
            if (MyMediaPlayerService.isPlaying()) {
                if (!MyMediaPlayerService.commonPlaylistcontent.getCategory_id().equalsIgnoreCase(mModelFile.getId())) {
                    isNewAudio = true;
                    Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                    intent.putExtra(MyMediaPlayerService.STOPP_LAY, true);
                    intent.putExtra("postion", 0);
                    getActivity().startService(intent);

                    Intent mIntent = new Intent(getActivity(), MyMediaPlayerService.class);
                    mIntent.putExtra(MyMediaPlayerService.BACKGROUND_STOP, true);
                    mIntent.putExtra("currentBackgroundSongs", 0);
                    getActivity().startService(mIntent);

                } else {
                    // MyMediaPlayerService.commonPlaylistcontent = null;
                    isNewAudio = false;

                }
            } else {

                isNewAudio = true;
                Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                intent.putExtra(MyMediaPlayerService.STOPP_LAY, true);
                intent.putExtra("postion", 0);
                getActivity().startService(intent);

                Intent mIntent = new Intent(getActivity(), MyMediaPlayerService.class);
                mIntent.putExtra(MyMediaPlayerService.BACKGROUND_STOP, true);
                mIntent.putExtra("currentBackgroundSongs", 0);
                getActivity().startService(mIntent);

            }
        } else {
            Log.e("Prosanto", "E");

            isNewAudio = true;
            Intent mIntent = new Intent(getActivity(), MyMediaPlayerService.class);
            mIntent.putExtra(MyMediaPlayerService.BACKGROUND_STOP, true);
            mIntent.putExtra("currentBackgroundSongs", 0);
            getActivity().startService(mIntent);

        }

        int volume = PersistentUser.getvolume(getActivity());
        seekBar2.setMin(0);
        seekBar2.setMax(100);
        seekBar2.setProgress(volume);

        Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
        intent.putExtra(MyMediaPlayerService.PLAYER_VOLUME, true);
        intent.putExtra("setplayervolume", volume);
        getActivity().startService(intent);

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar a0, int a1,
                                          boolean a2) {
                Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                intent.putExtra(MyMediaPlayerService.PLAYER_VOLUME, true);
                intent.putExtra("setplayervolume", a1);
                getActivity().startService(intent);
                PersistentUser.setvolume(getActivity(), a1);

            }
        });


        if (Helpers.isNetworkAvailable(getActivity())) {
            getSpinArray();
        } else {
            dataParsing();
        }

        return view;
    }

    public void playSongsCalculation() {
        if (prefManager.IsShowRating() && isNewAudio) {
            alertfornewuser();
        } else {
            int postion = spin_time.getSelectedItemPosition();
            ContentList mModelFile = allLists.get(postion);
            Playlist mPlaylist = new Playlist();
            mPlaylist.setTitle(mModelFile.getContentname());
            mPlaylist.setTrack_url(mModelFile.getMp3file());
            mPlaylist.setIntId(mModelFile.getIntId());
            mPlaylist.setCategory_id(mModelFile.getCategory_id());
            mPlaylist.setContentname(mModelFile.getContentname());
            mPlaylist.setDescription(mModelFile.getDescription());
            mPlaylist.setMp3file(mModelFile.getMp3file());
            mPlaylist.setSubscription(mModelFile.getSubscription());
            mPlaylist.setStatus(mModelFile.getStatus());
            MyMediaPlayerService.commonPlaylistcontent = mPlaylist;
            playOrPause();
        }
    }

    private void playOrPause() {

        Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
        intent.putExtra(MyMediaPlayerService.START_PLAY, true);
        intent.putExtra("postion", 0);
        getActivity().startService(intent);
    }

    AlertDialog alertDialog = null;

    public void alertfornewuser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        View mView = inflater.inflate(R.layout.dialog_ratings, null);
        RatingBar ratingBar1 = (RatingBar) mView.findViewById(R.id.ratingBar1);
        final TextView ratingNumber = (TextView) mView.findViewById(R.id.ratingNumber);
        final LinearLayout Okaylayout = (LinearLayout) mView.findViewById(R.id.Okaylayout);
        final TextView textRatings = (TextView) mView.findViewById(R.id.textRatings);
        textRatings.setText(getActivity().getResources().getString(R.string.rating_text));
        ratingBar1.setRating(1f);
        ratingNumber.setText("1");

        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                int text = (int) ratingBar.getRating();
                ratingNumber.setText("" + text);
                if (rating < 1.0f)
                    ratingBar.setRating(1.0f);
//                ratingBar1.setRating(ratingBar.getRating());
            }
        });
        Okaylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                isNewAudio = false;
                int postion = spin_time.getSelectedItemPosition();
                ContentList mContentList = allLists.get(postion);
                Playlist mPlaylist = new Playlist();
                mPlaylist.setTitle(mContentList.getContentname());
                mPlaylist.setTrack_url(mContentList.getMp3file());
                mPlaylist.setIntId(mContentList.getIntId());
                mPlaylist.setCategory_id(mContentList.getCategory_id());
                mPlaylist.setContentname(mContentList.getContentname());
                mPlaylist.setDescription(mContentList.getDescription());
                mPlaylist.setMp3file(mContentList.getMp3file());
                mPlaylist.setSubscription(mContentList.getSubscription());
                mPlaylist.setStatus(mContentList.getStatus());
                MyMediaPlayerService.commonPlaylistcontent = mPlaylist;


                String ratingstime = DateUtility.getTodaysDate().toString().trim();
                String ratingsMonth = DateUtility.getCurrentNumberMonthj().toString().trim();
                String ratingYear = DateUtility.getYearDate().toString().trim();
                String ratingNumber = ratingBar1.getRating() + "";

                RatingCategorySongs mRatingCategorySongs = new RatingCategorySongs();
                mRatingCategorySongs.setCategoryid(mContentList.getCategory_id());
                mRatingCategorySongs.setSongsid(mContentList.getIntId());
                mRatingCategorySongs.setCategoryname(mModelFile.getCategoryname());
                mRatingCategorySongs.setCategorytitle(mModelFile.getContentname());
                mRatingCategorySongs.setDescription(mModelFile.getDescription());
                mRatingCategorySongs.setCategoryColor(mModelFile.getText_color());
                mRatingCategorySongs.setRatingstime(ratingstime);
                mRatingCategorySongs.setRatingsmonth(ratingsMonth);
                mRatingCategorySongs.setRatingsyear(ratingYear);
                mRatingCategorySongs.setRatingsnumber(ratingNumber);
                mRatingCategorySongs.setRatingstype("0");
                mRatingCategorySongs.save();

                Log.w("are", "are" + mRatingCategorySongs.getId());
                Myapplication.ratingsID = "" + mRatingCategorySongs.getId();


                savePlaySongs();
                playOrPause();
            }
        });
        builder.setView(mView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    public void alertforEnd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_ratings, null);
        RatingBar ratingBar1 = (RatingBar) mView.findViewById(R.id.ratingBar1);
        final TextView ratingNumber = (TextView) mView.findViewById(R.id.ratingNumber);
        final LinearLayout Okaylayout = (LinearLayout) mView.findViewById(R.id.Okaylayout);
        final TextView textRatings = (TextView) mView.findViewById(R.id.textRatings);
        textRatings.setText(getActivity().getResources().getString(R.string.rating_text_end));
        ratingBar1.setRating(1f);
        ratingNumber.setText("1");
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                int text = (int) ratingBar.getRating();
                ratingNumber.setText("" + text);
                if (rating < 1.0f)
                    ratingBar.setRating(1.0f);

            }
        });
        Okaylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                String ratingstime = DateUtility.getTodaysDate().toString().trim();
                String ratingsMonth = DateUtility.getCurrentNumberMonthj().toString().trim();
                String ratingYear = DateUtility.getYearDate().toString().trim();
                String ratingNumber = ratingBar1.getRating() + "";

                int postion = spin_time.getSelectedItemPosition();
                ContentList mContentList = allLists.get(postion);
                RatingCategorySongs mRatingCategorySongs = new RatingCategorySongs();
                mRatingCategorySongs.setCategoryid(mContentList.getCategory_id());
                mRatingCategorySongs.setSongsid(mContentList.getIntId());
                mRatingCategorySongs.setCategoryname(mModelFile.getCategoryname());
                mRatingCategorySongs.setCategorytitle(mModelFile.getContentname());
                mRatingCategorySongs.setDescription(mModelFile.getDescription());
                mRatingCategorySongs.setCategoryColor(mModelFile.getText_color());

                mRatingCategorySongs.setRatingstime(ratingstime);
                mRatingCategorySongs.setRatingsmonth(ratingsMonth);
                mRatingCategorySongs.setRatingsyear(ratingYear);
                mRatingCategorySongs.setRatingsnumber(ratingNumber);
                mRatingCategorySongs.setRatingsID(Myapplication.ratingsID);

                mRatingCategorySongs.setRatingstype("1");
                mRatingCategorySongs.save();

            }
        });
        builder.setView(mView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        // alertDialog.get.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void getSpinArray() {

        String url_media = AllUrls.getcontentURL(mModelFile.getId());
        Log.e("url_media", url_media);
        final ProgressDialog progressDialogmedia = new ProgressDialog(getContext());
        progressDialogmedia.setMessage("Loading...");
        progressDialogmedia.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url_media, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialogmedia.dismiss();
                try {
                    Reservoir.put("content_" + mModelFile.getId(), response.toString());
                    dataParsing();
                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialogmedia.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void dataParsing() {
        try {

            String responseServer = "";
            String saveData = "";
            if (PersistentUser.isLanguage(getActivity())) {
                saveData = "content_" + mModelFile.getId();
            } else {
                saveData = "content_" + mModelFile.getId();
            }
            boolean objectExists = Reservoir.contains(saveData);
            if (objectExists) {
                Type resultType = new TypeToken<String>() {
                }.getType();
                responseServer = Reservoir.get(saveData, resultType);
            } else {
                Log.e("no save data", "No save data");
                return;
            }

            JSONArray response = new JSONArray(responseServer);
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                String category_id = jsonObject.getString("category_id");
                Log.e("mModelFile", "are" + mModelFile.getId());
                Log.e("mModelFile", "are" + category_id);

                if (mModelFile.getId().equalsIgnoreCase(category_id)) {

                    Log.e("response", response.toString());

                    contentnameArray.add(jsonObject.getString(("contentname")));
                    String contantId = jsonObject.getString(("intId"));
                    String contentname = jsonObject.getString(("contentname"));
                    String description = jsonObject.getString(("description"));
                    String mp3file = jsonObject.getString(("mp3file"));
                    String mp3subscription = jsonObject.getString(("subscription"));
                    String status = jsonObject.getString(("status"));

                    ContentList mContentList = new ContentList();
                    mContentList.setIntId(contantId);
                    mContentList.setCategory_id(category_id);
                    mContentList.setContentname(contentname);
                    mContentList.setDescription(description);
                    mContentList.setMp3file(mp3file);
                    mContentList.setSubscription(mp3subscription);
                    mContentList.setStatus(status);

                    allLists.add(mContentList);

                }
            }
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, contentnameArray);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_time.setAdapter(dataAdapter2);

            if (MyMediaPlayerService.commonPlaylistcontent != null) {
                if (MyMediaPlayerService.isPlaying()) {
                    for (int index = 0; index < allLists.size(); index++) {
                        if (allLists.get(index).getIntId().equalsIgnoreCase(MyMediaPlayerService.commonPlaylistcontent.getIntId())) {
                            spin_time.setSelection(index);
                        }
                    }
                    if (MyMediaPlayerService.isbackgroundPlay()) {
                        spin_style.setSelection(MyMediaPlayerService.currentBackgroundSongs);
                    }

                }

            }
            if (Myapplication.openScreenType == 4) {
                for (int index = 0; index < allLists.size(); index++) {
                    if (allLists.get(index).getIntId().equalsIgnoreCase(mModelFile.getContentId())) {
                        spin_time.setSelection(index);
                    }
                }
            }
            checkFavourite();
            boolean IsShowTipsHome = prefManager.IsAlreadyPlayerpageTips();

            if (prefManager.IsShowTips() && !IsShowTipsHome) {
                prefManager.setAlreadyPlayerpageTips(true);
                Tooltip(1);
            }
            downloadVideoCheck();
        } catch (Exception ex) {

        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean completeSongs = intent.getBooleanExtra("completeSongs", false);
            Log.e("completeSongs", "are" + completeSongs);
            if (!completeSongs) {
                Playlist m = MyMediaPlayerService.commonPlaylistcontent;
                if (m.getCategory_id().equalsIgnoreCase(mModelFile.getId())) {
                    boolean isPlayed = intent.getBooleanExtra("isplay", false);
                    String currentDuration = intent.getStringExtra("currentDuration");
                    String totalDuration = intent.getStringExtra("totalDuration");
                    int progress = intent.getIntExtra("progress", 0);
                    txt_duration_play.setText("" + totalDuration);
                    int currentPlaySongs = intent.getIntExtra("currentPlaySongs", 0);
                    txt_duration.setText("" + currentDuration);

                    if (isPlayed) {
                        btn_play.setImageResource(R.drawable.new_paush);
                    } else {
                        btn_play.setImageResource(R.drawable.new_play);
                    }
                    seekbar.setProgress(progress);
                } else {
                    txt_duration_play.setText("0:0");
                    txt_duration.setText("0:0");
                    seekbar.setProgress(0);
                }

            } else {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
                btn_play.setImageResource(R.drawable.new_play);
                txt_duration_play.setText("0:0");
                txt_duration.setText("0:0");
                seekbar.setProgress(0);
                if (prefManager.IsShowRating() && MyMediaPlayerService.commonPlaylistcontent != null) {
                    MyMediaPlayerService.commonPlaylistcontent = null;
                    alertforEnd();
                }
            }
        }
    };

    @Override
    protected void onVisible() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("my-event"));
    }

    @Override
    protected void onInvisible() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

    }

    public void checkFavourite() {
        if (allLists.size() > 0) {
            int selection = spin_time.getSelectedItemPosition();
            ContentList mContentList = allLists.get(selection);
            if (DatabaseQueryHelper.getFavouriteSongs(mContentList.getCategory_id(), mContentList.getIntId()) != null) {
                btn_favourite.setChecked(true);
            } else {
                btn_favourite.setChecked(false);
            }

        }
    }

    public void savePlaySongs() {
        int postion = spin_time.getSelectedItemPosition();
        ContentList mContentList = allLists.get(postion);
        PlaySongs mFavouriteSongs = new PlaySongs();
        mFavouriteSongs.setCategoryid(mModelFile.getId());
        mFavouriteSongs.setCategoryname(mModelFile.getCategoryname());
        mFavouriteSongs.setCategorytitle(mModelFile.getCategorytitle());
        mFavouriteSongs.setDescription(mModelFile.getDescription());
        mFavouriteSongs.setText_color(mModelFile.getText_color());
        mFavouriteSongs.setDesign_color(mModelFile.getDesign_color());
        mFavouriteSongs.setCategory_iconimage(mModelFile.getDec_img());
        mFavouriteSongs.setCard_image(mModelFile.getCard_image());
        mFavouriteSongs.setCategory_infoiconimage(mModelFile.getLogo_image());
        mFavouriteSongs.setSubscription(mModelFile.getSubscription());
        mFavouriteSongs.setSongsID(mContentList.getIntId());
        mFavouriteSongs.setContentname(mContentList.getContentname());
        mFavouriteSongs.setSongdescription(mContentList.getDescription());
        mFavouriteSongs.setSongsubscription(mContentList.getSubscription());
        mFavouriteSongs.setMp3file(mContentList.getMp3file());
        mFavouriteSongs.save();
    }

    public void Tooltip(int postion) {
        String MusicDropdown = getActivity().getResources().getString(R.string.MusicDropdown);
        String AudioDropdown = getActivity().getResources().getString(R.string.AudioDropdown);
        String bottomslider = getActivity().getResources().getString(R.string.bottomslider);
        String downLoadButton = getActivity().getResources().getString(R.string.downloadtext);

        String iButton = getResources().getString(R.string.iButton);
        if (postion == 1) {
            new SimpleTooltip.Builder(getActivity())
                    .anchorView(spin_time)
                    .text(MusicDropdown)
                    .gravity(Gravity.BOTTOM)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            Tooltip(2);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();

        } else if (postion == 2) {
            new SimpleTooltip.Builder(getActivity())
                    .anchorView(spin_style)
                    .text(AudioDropdown)
                    .gravity(Gravity.BOTTOM)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            Tooltip(3);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();
        } else if (postion == 3) {
            new SimpleTooltip.Builder(getActivity())
                    .anchorView(seekBar2)
                    .text(bottomslider)
                    .gravity(Gravity.BOTTOM)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            Tooltip(4);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();
        } else if (postion == 4) {
            new SimpleTooltip.Builder(getActivity())
                    .anchorView(btn_download_delete)
                    .text(downLoadButton)
                    .gravity(Gravity.BOTTOM)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            Tooltip(5);
                            // PersistentUser.setPlayerpageToolsTips(getActivity(), false);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();
        } else if (postion == 5) {
            new SimpleTooltip.Builder(getActivity())
                    .anchorView(btn_detail)
                    .text(iButton)
                    .gravity(Gravity.BOTTOM)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            prefManager.setAlreadyPlayerpageTips(true);
                            //PersistentUser.setPlayerpageToolsTips(getActivity(), false);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();
        }
    }

    public void downloadVideoCheck() {
        ValidationPermsionForInitial();


    }

    public File downloadAndUnzipContent(String FolderName) {
        File d = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getAlbumName());
        if (!d.exists()) {
            d.mkdirs();
        }
        String path = d.getAbsolutePath().concat("/").concat(FolderName);
        File myDir = new File(path);
        if (myDir.isFile()) {
            return myDir;
        } else {
            return null;
        }

    }

    private String getAlbumName() {
        return "voy"; //getString(R.string.app_name);
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS_AGENT = 100;

    public void ValidationPermsion() {


        List<String> permissions = new ArrayList<String>();
        if (Build.VERSION.SDK_INT > 22) {
            String cameraPermission = Manifest.permission.CAMERA;
            String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String storagePermission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
            int hascameraPermission = getActivity().checkSelfPermission(cameraPermission);
            int hasstoragePermission = getActivity().checkSelfPermission(storagePermission);
            int hasstoragePermission2 = getActivity().checkSelfPermission(storagePermission2);

            if (hascameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(cameraPermission);
            }
            if (hasstoragePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(storagePermission);
            }

            if (hasstoragePermission2 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(storagePermission2);
            }

            if (!permissions.isEmpty()) {
                String[] params = permissions.toArray(new String[permissions.size()]);
                requestPermissions(params, REQUEST_CODE_ASK_PERMISSIONS_AGENT);
            } else {
                CheckEventStart();

            }
        } else {
            CheckEventStart();
        }
    }

    private int downloadSongsListPosition = 0;

    private void CheckEventStart() {
        if (allLists.size() > 0) {
            int postion = spin_time.getSelectedItemPosition();
            ContentList mModelFile = allLists.get(postion);
            String fileName = ConstantFunctions.trackurlSplit(mModelFile.getMp3file());
            File mFile = downloadAndUnzipContent(fileName);
            if (mFile != null) {
                Log.e("mFile", mFile.getAbsolutePath());
                mFile.delete();
                btn_download_delete.setImageResource(R.drawable.download_image);
            } else {

                if (Helpers.isNetworkAvailable(getActivity())) {
                    Intent mmIntent = new Intent(getActivity(), DownloadedActivity.class);
                    mmIntent.putExtra("download_path", mModelFile.getMp3file());
                    startActivityForResult(mmIntent, 1010);
                } else {
                    AlertMessage.showMessage(getActivity(), "Internet connection", "Internet connection not available");
                }


            }
        }

//        int postion = spin_time.getSelectedItemPosition();
//        ContentList mModelFile = allLists.get(postion);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS_AGENT:
                boolean flag = false;
                if (grantResults.length > 0) {
                    for (int x = 0; x < grantResults.length; x++) {
                        if (grantResults[x] != PackageManager.PERMISSION_GRANTED) {
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    Toast.makeText(getActivity(), "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                } else {
                    CheckEventStart();
                }
                break;
            case 2296: {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        CheckEventStart();
                    } else {
                        Toast.makeText(getActivity(), "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode == getActivity().RESULT_OK) {
            downloadVideoCheck();
            makeFavourite();
        }
    }

    public void makeFavourite() {
        if (allLists.size() > 0) {
            int postion = spin_time.getSelectedItemPosition();
            ContentList mContentList = allLists.get(postion);
            FavouriteSongs mFavouriteSongs = new FavouriteSongs();
            mFavouriteSongs.setCategoryid(mModelFile.getId());
            mFavouriteSongs.setCategoryname(mModelFile.getCategoryname());
            mFavouriteSongs.setCategorytitle(mModelFile.getCategorytitle());
            mFavouriteSongs.setDescription(mModelFile.getDescription());
            mFavouriteSongs.setText_color(mModelFile.getText_color());
            mFavouriteSongs.setDesign_color(mModelFile.getDesign_color());
            mFavouriteSongs.setCategory_iconimage(mModelFile.getDec_img());
            mFavouriteSongs.setCard_image(mModelFile.getCard_image());
            mFavouriteSongs.setCategory_infoiconimage(mModelFile.getLogo_image());
            mFavouriteSongs.setSubscription(mModelFile.getSubscription());
            mFavouriteSongs.setSongsID(mContentList.getIntId());
            mFavouriteSongs.setContentname(mContentList.getContentname());
            mFavouriteSongs.setSongdescription(mContentList.getDescription());
            mFavouriteSongs.setSongsubscription(mContentList.getSubscription());
            mFavouriteSongs.setMp3file(mContentList.getMp3file());
            FavouriteSongs mFavouriteSongsaa = DatabaseQueryHelper.getFavouriteSongs(mContentList.getCategory_id(), mContentList.getIntId());
            if (mFavouriteSongsaa == null) {
                mFavouriteSongs.save();
                btn_favourite.setChecked(true);
            }
        }

    }

    private void galleryAddPic(String currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void ValidationPermsionForInitial() {
        List<String> permissions = new ArrayList<String>();
        if (Build.VERSION.SDK_INT > 22) {
            String cameraPermission = Manifest.permission.CAMERA;
            String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String storagePermission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
            int hascameraPermission = getActivity().checkSelfPermission(cameraPermission);
            int hasstoragePermission = getActivity().checkSelfPermission(storagePermission);
            int hasstoragePermission2 = getActivity().checkSelfPermission(storagePermission2);

            if (hascameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(cameraPermission);
            }
            if (hasstoragePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(storagePermission);
            }

            if (hasstoragePermission2 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(storagePermission2);
            }

            if (!permissions.isEmpty()) {
                btn_download_delete.setImageResource(R.drawable.download_image);
            } else {
                CheckInital();

            }
        } else {
            CheckInital();
        }
    }

    public void CheckInital() {
        btn_download_delete.setImageResource(R.drawable.download_image);
        int postion = spin_time.getSelectedItemPosition();
        ContentList mModelFile = allLists.get(postion);
        String fileName = ConstantFunctions.trackurlSplit(mModelFile.getMp3file());
        File mFile = downloadAndUnzipContent(fileName);
        if (mFile != null) {
            btn_download_delete.setImageResource(R.drawable.delete_image);
        } else {
            btn_download_delete.setImageResource(R.drawable.download_image);
        }
    }
}