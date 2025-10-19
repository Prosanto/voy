package eu.coach_yourself.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.callbackinterface.FilterItemCallback;
import eu.coach_yourself.app.database.FavouriteSongs;

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.ViewHolder> {

    private Context context;
    private List<FavouriteSongs> list;
    private int itemLayout;
    Cursor cursor;
    private FilterItemCallback lFilterItemCallback;
    public AdapterFavourite(List<FavouriteSongs> list) {
        this.list = list;
    }
    public AdapterFavourite(Context context, List<FavouriteSongs> list) {
        this.context = context;
        this.list = list;
    }
    public FavouriteSongs getModelAt(int position) {
        return list.get(position);
    }

    public void setOnItemClickListener(FilterItemCallback lFilterItemCallback) {
        this.lFilterItemCallback = lFilterItemCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favourite, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavouriteSongs modelFile = list.get(position);
        holder.itemView.setTag(modelFile);
        holder.txt_favcat_name.setText(list.get(position).getCategoryname());
        holder.txt_favcantant_name.setText(list.get(position).getContentname());
        Picasso.with(context).load(list.get(position).getCategory_iconimage()).into(holder.img_fav);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lFilterItemCallback.ClickFilterItemCallback(0, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_favcat_name, txt_favcantant_name;
        ImageView img_fav;

        public ViewHolder(View view) {
            super(view);
            txt_favcat_name = view.findViewById(R.id.txt_favcat_name);
            txt_favcantant_name = view.findViewById(R.id.txt_favcantant_name);
            img_fav = view.findViewById(R.id.img_fav);


        }
    }

}
