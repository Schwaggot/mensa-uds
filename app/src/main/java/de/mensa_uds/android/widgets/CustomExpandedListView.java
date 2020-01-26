package de.mensa_uds.android.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class CustomExpandedListView extends ExpandableListView {

    private int old_count = 0;

    public CustomExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != old_count) {
            old_count = getCount();
            android.view.ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getCount() * (old_count > 0 ? getChildAt(0).getHeight() + 1 : 0);
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }
}
