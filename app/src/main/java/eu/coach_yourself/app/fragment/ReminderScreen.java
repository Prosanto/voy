package eu.coach_yourself.app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import eu.coach_yourself.app.MainActivity;
import eu.coach_yourself.app.R;
import eu.coach_yourself.app.database.AlarmData;
import eu.coach_yourself.app.database.DBHelper;
import eu.coach_yourself.app.database.DatabaseQueryHelper;
import eu.coach_yourself.app.service.AlertReceiver;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;


public class ReminderScreen extends Fragment {
    TextView btn_d1, btn_d2, btn_d3, btn_d4, btn_d5, btn_d6, btn_d7;

    Button btn_delete;
    AppCompatButton btn_save;
    EditText edit_remind;
    public TimePicker timePicker;
    DBHelper dbHelper;
    String dayString[] = new String[10];
    String days;
    String delimiter = " ";
    SQLiteDatabase database;
    StringBuilder sb = new StringBuilder();
    Bundle bundle;
    Calendar cal = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder_screen, container, false);
        if (PersistentUser.isLanguage(getActivity())) {
            LocaleHelper.setLocale(getContext(), "en");
        } else {
            LocaleHelper.setLocale(getActivity(), "de");
        }
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getReadableDatabase();
        Toolbar toolbar = view.findViewById(R.id.toolbarId);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                } else {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.BackButtonAction();
                }
            }
        });
        edit_remind = (EditText) view.findViewById(R.id.edit_remind);
        timePicker = (TimePicker) view.findViewById(R.id.timepicker);
        btn_d1 = (TextView) view.findViewById(R.id.btn_d1);
        btn_d2 = (TextView) view.findViewById(R.id.btn_d2);
        btn_d3 = (TextView) view.findViewById(R.id.btn_d3);
        btn_d4 = (TextView) view.findViewById(R.id.btn_d4);
        btn_d5 = (TextView) view.findViewById(R.id.btn_d5);
        btn_d6 = (TextView) view.findViewById(R.id.btn_d6);
        btn_d7 = (TextView) view.findViewById(R.id.btn_d7);
        btn_save = (AppCompatButton) view.findViewById(R.id.btn_save);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);

        timePicker.setIs24HourView(true);


        btn_d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d1.isSelected())
                    btn_d1.setSelected(false);
                else
                    btn_d1.setSelected(true);
            }
        });
        btn_d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d2.isSelected())
                    btn_d2.setSelected(false);
                else
                    btn_d2.setSelected(true);
            }
        });
        btn_d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d3.isSelected())
                    btn_d3.setSelected(false);
                else
                    btn_d3.setSelected(true);
            }
        });
        btn_d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d4.isSelected())
                    btn_d4.setSelected(false);
                else
                    btn_d4.setSelected(true);
            }
        });
        btn_d5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d5.isSelected())
                    btn_d5.setSelected(false);
                else
                    btn_d5.setSelected(true);
            }
        });
        btn_d6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d6.isSelected())
                    btn_d6.setSelected(false);
                else
                    btn_d6.setSelected(true);
            }
        });
        btn_d7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_d7.isSelected())
                    btn_d7.setSelected(false);
                else
                    btn_d7.setSelected(true);
            }
        });

        //getRepeat();
//        btn_d1.setOnCheckedChangeListener(this);
//        btn_d2.setOnCheckedChangeListener(this);

        bundle = this.getArguments();
        if (bundle != null) {

            bundle = getArguments();
            String id = bundle.getString("Id");
            Cursor cursor = database.rawQuery("select rowid,title,days,time,hr,minut from reminder where rowid=" + id, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int index1 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID);
                int index2 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME);
                int index3 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_DAY);
                int index5 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_HR);
                int index6 = cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_MINUT);

                String rowid = cursor.getString(index1);
                String remidtitle = cursor.getString(index2);
                String remindday = cursor.getString(index3);
                String remind_hr = cursor.getString(index5);
                String remind_minut = cursor.getString(index6);

                Pattern pattern = Pattern.compile(" ");
                final String[] daysString = pattern.split(remindday);
                for (int i = 0; i <= daysString.length - 1; i++) {
                    if (daysString[i].equals("Mo")) {
                        btn_d1.setSelected(true);
                    } else if (daysString[i].equals("Di")) {
                        btn_d2.setSelected(true);
                    } else if (daysString[i].equals("Mi")) {
                        btn_d3.setSelected(true);
                    } else if (daysString[i].equals("Do")) {
                        btn_d4.setSelected(true);
                    } else if (daysString[i].equals("Fr")) {
                        btn_d5.setSelected(true);
                    } else if (daysString[i].equals("Sa")) {
                        btn_d6.setSelected(true);
                    } else if (daysString[i].equals("So")) {
                        btn_d7.setSelected(true);
                    }
                }


                edit_remind.setText(remidtitle);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    timePicker.setHour(Integer.parseInt(remind_hr));
                    timePicker.setMinute(Integer.parseInt(remind_minut));
                }
            }
        } else {
            Calendar mCalendar = Calendar.getInstance();
            int number = mCalendar.get(Calendar.DAY_OF_WEEK);
            if (number == 2) {
                btn_d1.setSelected(true);
            } else if (number == 3) {
                btn_d2.setSelected(true);
            } else if (number == 4) {
                btn_d3.setSelected(true);
            } else if (number == 5) {
                btn_d4.setSelected(true);
            } else if (number == 6) {
                btn_d5.setSelected(true);
            } else if (number == 7) {
                btn_d6.setSelected(true);
            } else if (number == 1) {
                btn_d7.setSelected(true);
            }


        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getRepeat();
                bundle = getArguments();

                if (bundle != null) {
                    String id = bundle.getString("Id");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, timePicker.getMinute());
                    cal.set(Calendar.SECOND, 0);
                    String time, title, hr, minut;
                    hr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                    minut = String.valueOf(cal.get(Calendar.MINUTE));
                    title = String.valueOf(edit_remind.getText());
                    time = String.valueOf(cal.get(Calendar.HOUR) + " : " + String.valueOf(cal.get(Calendar.MINUTE) + " "));
                    dbHelper.updateremind(id, title, days, time, hr, minut);
                    removeReminder(Long.parseLong(id));
                    updateTimeText(cal);
                    startAlarm(cal, Long.parseLong(id), title);
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, timePicker.getMinute());
                    cal.set(Calendar.SECOND, 0);
                    String time, title, hr, minut;
                    hr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                    minut = String.valueOf(cal.get(Calendar.MINUTE));
                    title = String.valueOf(edit_remind.getText());
                    time = String.valueOf(cal.get(Calendar.HOUR) + " : " + String.valueOf(cal.get(Calendar.MINUTE) + " "));
                    long id = dbHelper.insertreminder(title, days, time, hr, minut);
                    updateTimeText(cal);
                    startAlarm(cal, id, title);
                }
            }

        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = getArguments();
                if (bundle != null) {
                    String id = bundle.getString("Id");
                    removeReminder(Long.parseLong(id));
                    dbHelper.deleteReminder(Integer.valueOf(id));
                    //Toast.makeText(getContext(), "Reminder Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent("my-reminder");
                    intent1.putExtra("isreminder", true);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);

                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                        getParentFragmentManager().popBackStack();
                    } else {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.BackButtonAction();
                    }

//
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);


    }


    @SuppressLint("ResourceAsColor")
    private ArrayList<Integer> getRepeat() {

        ArrayList<Integer> repeatList = new ArrayList<>();

        if (btn_d1.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 1);
            repeatList.add(Calendar.MONDAY);
            dayString[0] = "Mo";
        }
        if (btn_d2.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 2);
            repeatList.add(Calendar.TUESDAY);
            dayString[1] = "Di";
        }
        if (btn_d3.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 3);
            repeatList.add(Calendar.WEDNESDAY);
            dayString[2] = "Mi";

        }
        if (btn_d4.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 4);
            repeatList.add(Calendar.THURSDAY);
            dayString[3] = "Do";

        }
        if (btn_d5.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 5);
            repeatList.add(Calendar.FRIDAY);
            dayString[4] = "Fr";

        }
        if (btn_d6.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 6);
            repeatList.add(Calendar.SATURDAY);
            dayString[5] = "Sa";

        }
        if (btn_d7.isSelected()) {
            cal.set(Calendar.DAY_OF_WEEK, 7);
            repeatList.add(Calendar.SUNDAY);
            dayString[6] = "So";
        }

        for (int i = 0; i <= 6; i++) {
            if (dayString[i] != null) {
                if (sb.length() > 0) {
                    sb.append(delimiter);
                }
                sb.append(dayString[i]);
            }

        }
        days = sb.toString();

        return repeatList;
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Reminder set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        timeText = getActivity().getResources().getString(R.string.notificaion_save);
        Toast.makeText(getContext(), timeText, Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent("my-reminder");
        intent1.putExtra("isreminder", true);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);

        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.BackButtonAction();
        }

    }

    @SuppressLint("ScheduleExactAlarm")
    private void startAlarm(Calendar c, long id, String remainderText) {
        ArrayList<Integer> week = getRepeat();
        for (int x : week) {
            c.set(Calendar.DAY_OF_WEEK, x);
            AlarmData mAlarmData = new AlarmData();
            mAlarmData.setRemainderID("" + id);
            mAlarmData.setRemainderText(remainderText);
            mAlarmData.setRemainderTime("" + c.getTimeInMillis());
            mAlarmData.save();

            Log.e("c", "" + c.getTimeInMillis());
            long idalarm = mAlarmData.getId();
            int requestData = Integer.parseInt("" + mAlarmData.getId());

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlertReceiver.class);
            intent.putExtra("remainderText", remainderText);

            PendingIntent pendingIntent ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(getActivity(), requestData, intent, PendingIntent.FLAG_MUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } else {
                pendingIntent= PendingIntent.getBroadcast(getActivity(), requestData, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }

            Log.e("idalarm", "" + idalarm);

//            if (c.before(Calendar.getInstance())) {
//
//            }
        }
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_MUTABLE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getContext(), "Reminder Canceled", Toast.LENGTH_SHORT).show();
    }


    public void removeReminder(long id) {
        ArrayList<AlarmData> allAlarmData = DatabaseQueryHelper.getAlarmData("" + id);
        for (AlarmData mAlarmData : allAlarmData) {
            long idalarm = mAlarmData.getId();
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(getActivity(), AlertReceiver.class);

            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) idalarm, myIntent, PendingIntent.FLAG_MUTABLE);
            } else {
                pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) idalarm, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            alarmManager.cancel(pendingIntent);
        }
        DatabaseQueryHelper.DeleteAlarmData("" + id);


    }

}