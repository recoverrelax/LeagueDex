package com.greater.leaguedex.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.greater.leaguedex.R

private val PAGE_STATE_SAVE = "MainBottomNavController.PAGE_STATE_SAVE"

class BottomNavigationViewMediator<Screen : Enum<Screen>>(
    private val fragmentManager: FragmentManager,
    private val screenMenuIdMapper: List<Pair<Screen, Int>>,
    private val instantiateFrag: (screen: Screen) -> Fragment
) : BottomNavigationView.OnNavigationItemSelectedListener {

    private var currentScreen: Screen? = null
    private var navigationView: BottomNavigationView? = null

    private val currentFragment: Fragment?
        get() = fragmentManager.findFragmentById(R.id.container)

    private val primaryEntry: Pair<Screen, Int> = this.screenMenuIdMapper.first()

    private fun getMenuIdForScreen(screen: Screen): Int =
        screenMenuIdMapper.first { it.first == screen }.second

    private fun getScreenForMenuId(menuId: Int): Screen =
        screenMenuIdMapper.first { it.second == menuId }.first

    private val backPressHandler = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            val primaryScreen = primaryEntry.first
            goToScreen(newScreen = primaryScreen, updateSelectedNav = true)
        }
    }

    fun initialize(
        activity: ComponentActivity,
        navView: BottomNavigationView,
        initialScreen: Screen
    ) {
        this.navigationView = navView

        // register screen save
        activity.savedStateRegistry.registerSavedStateProvider(PAGE_STATE_SAVE) {
            Bundle().apply {
                currentScreen?.let {
                    putInt(PAGE_STATE_SAVE, getMenuIdForScreen(it))
                }
            }
        }

        // restore screen
        val restoredScreen: Screen? =
            activity.savedStateRegistry.consumeRestoredStateForKey(PAGE_STATE_SAVE)
                ?.getInt(PAGE_STATE_SAVE)
                ?.let { getScreenForMenuId(it) }

        // back press
        activity.onBackPressedDispatcher.addCallback(backPressHandler)

        // clear view references
        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    navigationView = null
                    backPressHandler.remove()
                    super.onDestroy(owner)
                }
            }
        )

        navView.setOnNavigationItemSelectedListener(this)
        goToScreen(restoredScreen ?: initialScreen)
    }

    private fun goToScreen(
        newScreen: Screen,
        updateSelectedNav: Boolean = false
    ) {
        val newScreenTag = newScreen.toString()
        val oldScreen = this.currentScreen
        if (oldScreen == newScreen) return
        this.currentScreen = newScreen

        if (updateSelectedNav) {
            navigationView?.selectedItemId = getMenuIdForScreen(newScreen)
        }
        backPressHandler.isEnabled = newScreen != primaryEntry.first

        fragmentManager.commitNow {
            var frag = fragmentManager.findFragmentByTag(newScreenTag)
            if (frag == null) {
                frag = instantiateFrag(newScreen)
                add(R.id.container, frag, newScreenTag)
            }
            currentFragment?.let { hide(it) }
            show(frag)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        goToScreen(getScreenForMenuId(item.itemId))
        return true
    }
}
