package aleksandrkim.ArchComponentsTest.Utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Aleksandr Kim on 30 May, 2018 8:45 PM for ArchComponentsTest
 */

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    RecyclerItemSwipeListener recyclerItemSwipeListener;

    public SwipeCallback(RecyclerItemSwipeListener recyclerItemSwipeListener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.recyclerItemSwipeListener = recyclerItemSwipeListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        recyclerItemSwipeListener.onSwipe(viewHolder, direction);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) getDefaultUIUtil().onSelected(((SwipeLayout) viewHolder).getViewForeground());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(((SwipeLayout) viewHolder).getViewForeground());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        SwipeLayout sourceVh = (SwipeLayout) viewHolder;
        FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) sourceVh.getSwipeActionIcon().getLayoutParams();
        l.gravity = dX > 0
                ? (Gravity.START | Gravity.CENTER_VERTICAL)
                : (Gravity.END | Gravity.CENTER_VERTICAL);
        sourceVh.getSwipeActionIcon().setLayoutParams(l);

        getDefaultUIUtil().onDraw(c, recyclerView, sourceVh.getViewForeground(), dX, dY, actionState, isCurrentlyActive);
    }

    public interface RecyclerItemSwipeListener {

        /**
         * Action to take on swipe
         *
         * @param viewHolder the ViewHolder that was swiped
         * @param swipeDir   the direction of the swipe
         */
        void onSwipe(RecyclerView.ViewHolder viewHolder, int swipeDir);
    }

    public interface SwipeLayout {

        /**
         * @return the foreground view of the swipeable laout
         */
        View getViewForeground();

        /**
         * @return the view on the background layout representing the action being taken e.g. trash bin, favorite icon, text message
         */
        View getSwipeActionIcon();
    }
}
