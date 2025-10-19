package eu.coach_yourself.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.model.ModelFile;

public class CardArrayAdapter extends ArrayAdapter<ModelFile> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<ModelFile> items;
    private boolean flagDevice = true;

    public CardArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<ModelFile> items, boolean flagDevice) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = new ArrayList<>();
        this.items.addAll(items);
        this.flagDevice = flagDevice;

    }

    public ModelFile getModelFile(int position) {
        return items.get(position);
    }

    public ArrayList<ModelFile> getModelFile() {
        return items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            mViewHolder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.home_cards, null);
            mViewHolder.txt_categoryname = (TextView) convertView
                    .findViewById(R.id.txt_categoryname);
            mViewHolder.txt_categorytitle = (TextView) convertView
                    .findViewById(R.id.txt_categorytitle);
            mViewHolder.card_image = (ImageView) convertView
                    .findViewById(R.id.card_image);
            mViewHolder.logo_image = (ImageView) convertView
                    .findViewById(R.id.category_iconimage);
            mViewHolder.rootLayout = (RelativeLayout) convertView
                    .findViewById(R.id.rootLayout);
            mViewHolder.subscriptionIcon = (ImageView) convertView
                    .findViewById(R.id.subscriptionIcon);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        mViewHolder.txt_categoryname.setText(items.get(position).getCategoryname());
        mViewHolder.txt_categorytitle.setText(items.get(position).getCategorytitle());
        mViewHolder.txt_categoryname.setTextColor(Color.parseColor(items.get(position).getText_color()));

        Picasso.with(context).load(items.get(position).getCard_image()).into(mViewHolder.card_image);
        Picasso.with(context).load(items.get(position).getLogo_image()).into(mViewHolder.logo_image);

        if (items.get(position).getSubscription().equalsIgnoreCase("1")) {
            mViewHolder.subscriptionIcon.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.subscriptionIcon.setVisibility(View.INVISIBLE);
        }
        mViewHolder.subscriptionIcon.setVisibility(View.INVISIBLE);


//        if (flagDevice) {
//            mViewHolder.txt_categoryname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
//            mViewHolder.txt_categorytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//        }
        return convertView;
    }

    private class MyViewHolder {
        TextView txt_categoryname, txt_categorytitle;
        ImageView card_image, logo_image, subscriptionIcon;
        RelativeLayout rootLayout;
    }
}

