package com.greater.leaguedex.util.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecorator(
    private val top: Int = 0,
    private val bot: Int = 0,
    private val start: Int = 0,
    private val end: Int = 0
) : RecyclerView.ItemDecoration() {
    constructor(space: Int): this(space, space, space, space)
    override fun getItemOffsets(
        outRect: Rect,
        v: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = top
        outRect.bottom = bot
        outRect.right = end
        outRect.left = start
    }
}
