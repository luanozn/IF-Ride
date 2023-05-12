package com.example.cesar.ifride.utils

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import com.google.firebase.Timestamp

class Util {

    companion object {
        fun dpToPx(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }

        fun removeLinearLayoutChildren(linearLayout: LinearLayout) {
            while (linearLayout.childCount != 0) {
                val childView = linearLayout.getChildAt(0)
                linearLayout.removeView(childView)
            }
        }

        fun standardLinearLayout(context: Context): LinearLayout {
            val linearLayout = LinearLayout(context)
            linearLayout.apply {
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            return linearLayout
        }

        fun dateTimeFromTimestamp(timestamp: Timestamp) {
            val tdate = timestamp.toDate()
        }

    }


}