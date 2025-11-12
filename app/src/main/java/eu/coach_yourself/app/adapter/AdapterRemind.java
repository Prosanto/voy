package eu.coach_yourself.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.regex.Pattern;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.callbackinterface.FilterItemCallback;
import eu.coach_yourself.app.model.ModelFile;

public class AdapterRemind extends RecyclerView.Adapter<AdapterRemind.ViewHolder> {
    FilterItemCallback lFilterItemCallback;
    private Context context;
    private List<ModelFile> list;
    private int itemLayout;
    Cursor cursor;

    public AdapterRemind(Context context, List<ModelFile> list) {
        this.context = context;
        this.list = list;
    }

    public void addClick(FilterItemCallback lFilterItemCallback) {
        this.lFilterItemCallback = lFilterItemCallback;
    }

    public ModelFile getModelFile(int position) {
        return this.list.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reminder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelFile modelFile = list.get(position);
        holder.itemView.setTag(modelFile);
        String remindday = list.get(position).getRemind_day();
        Pattern pattern = Pattern.compile(" ");
        final String[] daysString = pattern.split(remindday);
        String textofDay = "";

        for (int i = 0; i <= daysString.length - 1; i++) {
            if (daysString[i].equals("Mo")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.Mo);
            } else if (daysString[i].equals("Di")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.Tu);
            } else if (daysString[i].equals("Mi")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.We);
            } else if (daysString[i].equals("Do")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.Th);
            } else if (daysString[i].equals("Fr")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.Fr);
            } else if (daysString[i].equals("Sa")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.Sa);
            } else if (daysString[i].equals("So")) {
                textofDay = textofDay + " " + context.getResources().getString(R.string.Su);
            }
        }
//
//        String dayTime = list.get(position).getRemind_time();
//        String[] values = dayTime.split(":");
//        int valueHours = Integer.parseInt(values[0].toString().trim());
//        int valueMinute = Integer.parseInt(values[1].toString().trim());
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR, valueHours);
//        cal.set(Calendar.MINUTE, valueMinute);
//        cal.set(Calendar.SECOND, 0);
//
//        DateFormat df = new SimpleDateFormat("hh:mm a");
//        String date = df.format(cal.getTime());
//        holder.txt_alarm_time.setText(date);

       // String value = list.get(position).getRemind_hr().trim() + ":" + list.get(position).getRemind_minut().trim() ;
        String valueHrs= list.get(position).getRemind_hr().trim();
        String valueminutes= list.get(position).getRemind_minut().trim();
        int hours= Integer.parseInt(list.get(position).getRemind_hr());
        int minutes= Integer.parseInt(list.get(position).getRemind_minut());
        if(9>=minutes){
            valueminutes="0"+valueminutes;
        }
       // Log.e("value", value);
        //value = DateUtility.getCovention(value);
        holder.txt_alarm_time.setText(valueHrs+":"+valueminutes);
        holder.txt_alarm_days.setText(textofDay);
//        holder.txt_alarm_time.setText(list.get(position).getRemind_time());
        holder.txt_alarm_title.setText(list.get(position).getRemind_title());
        String id = list.get(position).getRowid();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lFilterItemCallback.ClickFilterItemCallback(0, position);

//                Bundle bundle = new Bundle();
//                bundle.putString("Id", ""+position);
//                Fragment fragment = new ReminderScreen();
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getChildFragmentManager();
//                fragmentManager.beginTransaction().add(R.id.Motivator_fragment, fragment)
//                        .addToBackStack("my_fragment_remnder")
//                        .commit();
////
//                Bundle bundle = new Bundle();
//                bundle.putString("Id", id);
//                AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
//                Fragment fragment = new ReminderScreen();
//                fragment.setArguments(bundle);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Motivator_fragment, fragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_alarm_days, txt_alarm_time, txt_alarm_title;

        public ViewHolder(View view) {
            super(view);

            txt_alarm_days = view.findViewById(R.id.txt_alarm_days);
            txt_alarm_time = view.findViewById(R.id.txt_alarm_time);
            txt_alarm_title = view.findViewById(R.id.txt_alarm_title);


        }
    }

}
