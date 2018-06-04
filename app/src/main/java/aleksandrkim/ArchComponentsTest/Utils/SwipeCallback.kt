package aleksandrkim.ArchComponentsTest.Utils

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

/**
 * Created by Aleksandr Kim on 30 May, 2018 8:45 PM for ArchComponentsTest
 */

class SwipeCallback(private val recyclerItemSwipeListener: RecyclerItemSwipeListener) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    val TAG = "SwipeCallback"

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder):
            Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        recyclerItemSwipeListener.onSwipe(viewHolder, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let { ItemTouchHelper.Callback.getDefaultUIUtil().onSelected((it as SwipeLayout).viewForeground) }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView((viewHolder as SwipeLayout).viewForeground)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        Log.d("TAG", "onChildDraw: ")
        val sourceVh = viewHolder as SwipeLayout
        val layoutParams = sourceVh.swipeActionIcon.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = when {
            dX > 0 -> (Gravity.START or Gravity.CENTER_VERTICAL)
            else -> (Gravity.END or Gravity.CENTER_VERTICAL)
        }
        sourceVh.swipeActionIcon.layoutParams = layoutParams

        ItemTouchHelper.Callback.getDefaultUIUtil()
            .onDraw(c, recyclerView, sourceVh.viewForeground, dX, dY, actionState, isCurrentlyActive)
    }

    interface RecyclerItemSwipeListener {

        /**
         * Action to take on swipe
         *
         * @param viewHolder the ViewHolder that was swiped
         * @param swipeDir   the direction of the swipe
         */
        fun onSwipe(viewHolder: RecyclerView.ViewHolder, swipeDir: Int)
    }

    interface SwipeLayout {

        /**
         * @return the foreground view of the swipeable laout
         */
        val viewForeground: View

        /**
         * @return the view on the background layout representing the action being taken e.g. trash bin, favorite icon, text message
         */
        val swipeActionIcon: View
    }
}
