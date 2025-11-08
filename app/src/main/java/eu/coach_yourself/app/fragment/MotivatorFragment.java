package eu.coach_yourself.app.fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.google.common.reflect.Reflection.getPackageName;

import android.Manifest;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.adapter.AdapterRemind;
import eu.coach_yourself.app.callbackinterface.FilterItemCallback;
import eu.coach_yourself.app.database.DBHelper;
import eu.coach_yourself.app.model.ModelFile;


public class MotivatorFragment extends BaseFragment {
    private Button btn_add_reminder;
    private RecyclerView recycle_reminder;
    private TextView txt_motivedetail;
    private DBHelper dbHelper;
    private List<ModelFile> list;
    private AdapterRemind adapterRemind;
    private long count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motivator, container, false);
        dbHelper = new DBHelper(getContext());
        recycle_reminder = (RecyclerView) view.findViewById(R.id.recycle_reminder);
        txt_motivedetail = (TextView) view.findViewById(R.id.txt_motivedetail);
        btn_add_reminder = (Button) view.findViewById(R.id.btn_add_reminder);

        dataLoadion();

        return view;
    }

    public void dataLoadion() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        count = dbHelper.getProfilesCount();
        database.close();
        list = new ArrayList<>();
        adapterRemind = new AdapterRemind(getContext(), list);
        btn_add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    if (!alarmManager.canScheduleExactAlarms()) {
                        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent); // Or use ActivityResultLauncher for better handling
                    } else {
                        Fragment fragment = new ReminderScreen();
                        FragmentManager fragmentManager = getChildFragmentManager();
                        fragmentManager.beginTransaction().add(R.id.Motivator_fragment, fragment)
                                .addToBackStack("my_fragment_remnder")
                                .commit();
                    }
                } else {
                    Fragment fragment = new ReminderScreen();
                    FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.Motivator_fragment, fragment)
                            .addToBackStack("my_fragment_remnder")
                            .commit();
                }

//
//                if (Build.VERSION.SDK_INT >= 33) {
//                    String[] permissions = {Manifest.permission.SCHEDULE_EXACT_ALARM};
//                    Permissions.check(getActivity(), permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
//                        @Override
//                        public void onGranted() {
////                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
////                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
////                            intent.setData(uri);
////                            startActivity(intent);
//                            Fragment fragment = new ReminderScreen();
//                            FragmentManager fragmentManager = getChildFragmentManager();
//                            fragmentManager.beginTransaction().add(R.id.Motivator_fragment, fragment)
//                                    .addToBackStack("my_fragment_remnder")
//                                    .commit();
//                        }
//                    });
//
//                } else {
//                    Fragment fragment = new ReminderScreen();
//                    FragmentManager fragmentManager = getChildFragmentManager();
//                    fragmentManager.beginTransaction().add(R.id.Motivator_fragment, fragment)
//                            .addToBackStack("my_fragment_remnder")
//                            .commit();
//                }


            }
        });

        if (count != 0) {
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from reminder", new String[]{});
            if (cursor != null) {
                cursor.moveToFirst();
            }
            txt_motivedetail.setVisibility(View.GONE);
            recycle_reminder.setVisibility(View.VISIBLE);
        } else {
            txt_motivedetail.setVisibility(View.VISIBLE);
            recycle_reminder.setVisibility(View.GONE);
        }

        recycle_reminder.setHasFixedSize(true);
        recycle_reminder.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle_reminder.setItemAnimator(new DefaultItemAnimator());
        adapterRemind = new AdapterRemind(getContext(), dbHelper.getAllReminderData());
        recycle_reminder.setAdapter(adapterRemind);
        adapterRemind.addClick(lFilterItemCallback);

    }

    public FilterItemCallback lFilterItemCallback = new FilterItemCallback() {
        @Override
        public void ClickFilterItemCallback(int type, int position) {
            ModelFile mModelFile = adapterRemind.getModelFile(position);
            Bundle bundle = new Bundle();
            bundle.putString("Id", mModelFile.getRowid());
            Fragment fragment = new ReminderScreen();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().add(R.id.Motivator_fragment, fragment)
                    .addToBackStack("my_fragment_remnder")
                    .commit();
        }
    };

    @Override
    protected void onVisible() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("my-reminder"));

    }

    @Override
    protected void onInvisible() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("my-reminder"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dataLoadion();
        }
    };


}