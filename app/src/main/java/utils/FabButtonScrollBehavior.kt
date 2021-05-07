package utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 *  客製化 Floating Action Button 的滑動事件
 *  向下滑時 Floating Action Button 會消失
 *  向上滑時 Floating Action Button 會顯示
 *  @author vincent created on 8, May, 2021
 */

class FabButtonScrollBehavior(context: Context?, attrs: AttributeSet?) :
    FloatingActionButton.Behavior(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed
        )

        /**
         *  往下滑 : > 0
         *  往上滑 : < 0
         */
        if(dyConsumed > 0){
            animateOut(child)
        }else{
            animateIn(child)
        }
    }

    private fun animateOut(floatingActionButton: FloatingActionButton){
        val params = floatingActionButton.layoutParams as CoordinatorLayout.LayoutParams
        val margin = params.bottomMargin
        floatingActionButton.animate().translationY((floatingActionButton.height+margin).toFloat()).setInterpolator(LinearInterpolator()).start()

    }

    private fun animateIn(floatingActionButton: FloatingActionButton){
        floatingActionButton.animate().translationY(0f).setInterpolator(LinearInterpolator()).start()
    }
}