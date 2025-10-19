package eu.coach_yourself.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.fragment.playerFragment;
import eu.coach_yourself.app.model.ModelFile;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


    private Context context;
    private List<ModelFile> list;


    public CardAdapter(Context c, List<ModelFile> list) {
        this.context = c;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_cards, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelFile modelFile = list.get(position);

        holder.txt_categoryname.setText(list.get(position).getCategoryname());
        holder.txt_categorytitle.setText(list.get(position).getCategorytitle());
        holder.txt_categoryname.setTextColor(Color.parseColor(list.get(position).getText_color()));

        Picasso.with(context).load(list.get(position).getLogo_image()).into(holder.logo_image);
        Picasso.with(context).load(list.get(position).getCard_image()).resize(120, 60).into(holder.card_image);
        String id = list.get(position).getId();


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_categoryname, txt_categorytitle;
        ImageView card_image, logo_image;

        public ViewHolder(View view) {
            super(view);

            txt_categoryname = view.findViewById(R.id.txt_categoryname);
            txt_categorytitle = view.findViewById(R.id.txt_categorytitle);
            card_image = view.findViewById(R.id.card_image);
            logo_image = view.findViewById(R.id.category_iconimage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  Bundle bundle=new Bundle();
                    //  bundle.putString("Id",id);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new playerFragment();
                    // fragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.layout.fragment_home, fragment).addToBackStack(null).commit();

                }
            });

        }
    }

}
