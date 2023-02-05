package com.deal.bytee.vendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.model.BannerItem;

import java.util.List;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    List<BannerItem> bannerDatumList;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, List<BannerItem> bannerDatumList) {
        this.context = context;
        this.bannerDatumList = bannerDatumList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bannerDatumList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_banner, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Glide.with(context).load(bannerDatumList.get(position).getBimg()).placeholder(R.drawable.vendor_logo).into(imageView);
        container.addView(itemView);
            /*imageView.setOnClickListener(v -> {
                if (!bannerDatumList.get(position).getCid().equalsIgnoreCase("0")) {
                    homeActivity.showMenu();
                    Bundle args = new Bundle();
                    args.putInt("id", Integer.parseInt(bannerDatumList.get(position).getCid()));
                    Fragment fragment = new SubCategoryFragment();
                    fragment.setArguments(args);
                    HomeActivity.getInstance().callFragment(fragment);
                }
            });*/
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}