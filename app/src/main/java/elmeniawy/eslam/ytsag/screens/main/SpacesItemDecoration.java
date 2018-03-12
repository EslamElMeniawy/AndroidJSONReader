package elmeniawy.eslam.ytsag.screens.main;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import elmeniawy.eslam.ytsag.R;

/**
 * Created by Eslam El-Meniawy on 01-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int space;

    SpacesItemDecoration(Context context, int space) {
        this.space = space;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        outRect.top = 0;
    }
}
