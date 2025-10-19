package eu.coach_yourself.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.fragment.playerFragment;
import eu.coach_yourself.app.model.ModelFile;

public class ViewPageAdapter extends PagerAdapter {

    private List<ModelFile> list;
    private Context context;
    Fragment fragment;

    public ViewPageAdapter(List<ModelFile> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public ViewPageAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        // return  Integer.MAX_VALUE;
        //
        // return list.size();
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.home_cards, container, false);

        container.addView(view);

        TextView txt_categoryname, txt_categorytitle;
        ImageView card_image, logo_image;
        txt_categoryname = view.findViewById(R.id.txt_categoryname);
        txt_categorytitle = view.findViewById(R.id.txt_categorytitle);
        card_image = view.findViewById(R.id.card_image);
        logo_image = view.findViewById(R.id.category_iconimage);

        txt_categoryname.setText(list.get(position).getCategoryname());
        txt_categorytitle.setText(list.get(position).getCategorytitle());
        txt_categoryname.setTextColor(Color.parseColor(list.get(position).getText_color()));

        NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
        // txt_categoryname.setTextColor((Integer) nf.parse(list.get(position).getText_color()));
        // txt_categoryname.setTextColor(Integer.parseInt("#70a2fd"));
        String id = list.get(position).getId();
        String Cat_name = list.get(position).getCategoryname();
        String text_color = list.get(position).getText_color();
        String dec = list.get(position).getDescription();
        String dec_img = list.get(position).getDec_img();
        // card_image.setImageResource(Integer.parseInt(list.get(position).getCard_image()));
        Picasso.with(context).load(list.get(position).getCard_image()).into(card_image);
        Picasso.with(context).load(list.get(position).getLogo_image()).into(logo_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("Id", id);
                bundle.putString("Cat_name", Cat_name);
                bundle.putString("text_color", text_color);
                bundle.putString("dec", dec);
                bundle.putString("dec_img", dec_img);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                //  Fragment fragment=new playerFragment();
                Fragment fragment = new playerFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.layout.fragment_home, fragment).addToBackStack(R.class.getName()).commit();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
