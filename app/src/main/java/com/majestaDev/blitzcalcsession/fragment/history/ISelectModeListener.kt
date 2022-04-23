package com.majestaDev.blitzcalcsession.fragment.history

interface ISelectModeListener {
    fun selectMode(state: Boolean)
    fun isSelectMode(): Boolean
    fun updateNumberOfSelectedItems(countOfSelectedItems: Int)
    fun selectAll(state: Boolean)
}