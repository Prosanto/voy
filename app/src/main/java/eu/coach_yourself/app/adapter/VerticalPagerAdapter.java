package eu.coach_yourself.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.fragment.playerFragment;
import eu.coach_yourself.app.model.ModelFile;

public class VerticalPagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<ModelFile> arrayList = new ArrayList<>();
    private Context context;
    public VerticalPagerAdapter(final Context context, ArrayList<ModelFile> list) {
        mLayoutInflater = LayoutInflater.from(context);
        this.arrayList.clear();
        this.context=context;
        this.arrayList.addAll(list);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = mLayoutInflater.inflate(R.layout.home_cards, container, false);

        TextView txt_categoryname, txt_categorytitle;
        ImageView card_image, logo_image;
        txt_categoryname = view.findViewById(R.id.txt_categoryname);
        txt_categorytitle = view.findViewById(R.id.txt_categorytitle);
        card_image = view.findViewById(R.id.card_image);
        logo_image = view.findViewById(R.id.category_iconimage);

        txt_categoryname.setText(arrayList.get(position).getCategoryname());
        txt_categorytitle.setText(arrayList.get(position).getCategorytitle());
        txt_categoryname.setTextColor(Color.parseColor(arrayList.get(position).getText_color()));
        NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
        String id = arrayList.get(position).getId();
        String Cat_name = arrayList.get(position).getCategoryname();
        String text_color = arrayList.get(position).getText_color();
        String dec = arrayList.get(position).getDescription();
        String dec_img = arrayList.get(position).getDec_img();
        // card_image.setImageResource(Integer.parseInt(list.get(position).getCard_image()));
        Picasso.with(context).load(arrayList.get(position).getCard_image()).into(card_image);
        Picasso.with(context).load(arrayList.get(position).getLogo_image()).into(logo_image);

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
                //activity.getSupportFragmentManager().beginTransaction().replace(R.layout.fragment_home, fragment).addToBackStack(R.class.getName()).commit();
            }
        });

        //setupItem(view, TWO_WAY_LIBRARIES[position]);


        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }
    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}