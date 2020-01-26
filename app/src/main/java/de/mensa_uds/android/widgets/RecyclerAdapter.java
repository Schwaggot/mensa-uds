package de.mensa_uds.android.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;

import java.util.Arrays;
import java.util.List;

import de.mensa_uds.android.DataProvider;
import de.mensa_uds.android.data.Day;
import de.mensa_uds.android.data.Meal;
import de.mensa_uds.android.data.Menu;
import redfox.android.mensa.R;

public class RecyclerAdapter extends RecyclerView.Adapter<MealHolder> {

    private List<Meal> mealList;
    private int lastPosition = -1;
    private Context context;

    public RecyclerAdapter(int position, Context context) {
        DataProvider dp = DataProvider.getInstance();
        Menu am = dp.getActiveMenu();
        Day[] days = am.getDays();
        Meal[] meals = days[position].getMeals();

        this.mealList = Arrays.asList(meals);
        this.context = context;
    }

    @Override
    public MealHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_meal, viewGroup, false);
        MealHolder mealHolder = new MealHolder(v);

        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.meal_ratingBar);

        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.GRAY);   // Empty star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)), Color.GRAY); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), Color.rgb(255, 204, 0));  // Full star

        return mealHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MealHolder mealHolder, int position) {
        Meal meal = mealList.get(position);

        mealHolder.category.setText(meal.getCategory());
        mealHolder.title.setText(meal.getTitle());

        if (!meal.getDescription().isEmpty()) {
            mealHolder.description.setVisibility(View.VISIBLE);
            mealHolder.description.setText(meal.getDescription());
        } else {
            mealHolder.description.setVisibility(View.GONE);
        }

        mealHolder.colorView.setBackgroundColor(Color.parseColor(meal.getColor()));

        if (!meal.getPriceStudent().isEmpty() && !meal.getPriceWorker().isEmpty() && !meal.getPriceGuest().isEmpty()) {
            mealHolder.expListView.setVisibility(View.VISIBLE);
            mealHolder.price.setText(String.format("S: %s€    M: %s€    G: %s€", meal.getPriceStudent(), meal.getPriceWorker(), meal.getPriceGuest()));
        } else {
            mealHolder.expListView.setVisibility(View.GONE);
        }

        if (meal.getAllergens().length > 0) {
            mealHolder.expListView.setVisibility(View.VISIBLE);
            ExpandableListAdapter listAdapter = new ExpandableListAdapter(context, Arrays.asList(meal.getAllergens()));
            mealHolder.expListView.setAdapter(listAdapter);
        } else {
            mealHolder.expListView.setVisibility(View.GONE);
        }

        setAnimation(mealHolder.container, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return (mealList != null ? mealList.size() : 0);
    }

}
