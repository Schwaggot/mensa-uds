package de.mensa_uds.android.widgets;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import redfox.android.mensa.R;

public class MealHolder extends RecyclerView.ViewHolder {
    protected TextView title;
    protected TextView description;
    protected TextView category;
    protected TextView price;
    protected RatingBar ratingBar;
    protected View colorView;
    protected CardView container;
    protected CustomExpandedListView expListView;

    public MealHolder(View view) {
        super(view);
        this.category = view.findViewById(R.id.meal_category);
        this.description = view.findViewById(R.id.meal_description);
        this.title = view.findViewById(R.id.meal_title);
        this.ratingBar = view.findViewById(R.id.meal_ratingBar);
        this.colorView = view.findViewById(R.id.meal_color);
        this.price = view.findViewById(R.id.meal_price);
        this.container = itemView.findViewById(R.id.item_layout_container);
        this.expListView = itemView.findViewById(R.id.lvExp);
    }

}