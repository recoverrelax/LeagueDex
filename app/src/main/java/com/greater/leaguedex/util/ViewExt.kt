package com.greater.leaguedex.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

val RecyclerView.verticalLayoutManager: LinearLayoutManager
    get() {
        return LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }
