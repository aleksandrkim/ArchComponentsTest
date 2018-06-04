package aleksandrkim.ArchComponentsTest.Utils

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import kotlin.math.abs

/**
 * Created by Aleksandr Kim on 30 May, 2018 8:45 PM for ArchComponentsTest
 */

class SwipeCallback(private val recyclerItemSwipeListener: RecyclerItemSwipeListener) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder):
            Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        recyclerItemSwipeListener.onSwipe(viewHolder, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let { ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(it.itemView) }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView((viewHolder.itemView))
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val alpha = 1.0f - abs(dX) / viewHolder.itemView.width * 2
            viewHolder.itemView.alpha = alpha
        }

        ItemTouchHelper.Callback.getDefaultUIUtil()
            .onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive)
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
}
