@file:Suppress("NOTHING_TO_INLINE")

package com.greater.leaguedex.util

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * String Resource Inflation
 */

inline fun Activity.stringFromRes(@StringRes res: Int): String = this.getString(res)
inline fun Fragment.stringFromRes(@StringRes res: Int): String = this.getString(res)
inline fun View.stringFromRes(@StringRes res: Int): String = this.context.getString(res)
inline fun Context.stringFromRes(@StringRes res: Int): String = this.getString(res)

inline fun Activity.stringFromRes(@StringRes res: Int, vararg arguments: Any): String = this.getString(res, *arguments)
inline fun Fragment.stringFromRes(@StringRes res: Int, vararg arguments: Any): String = this.getString(res, *arguments)
inline fun View.stringFromRes(@StringRes res: Int, vararg arguments: Any): String = this.context.getString(res, *arguments)
inline fun Context.stringFromRes(@StringRes res: Int, vararg arguments: Any): String = this.getString(res, *arguments)

/**
 * Color Resource Inflation
 */

@ColorInt inline fun Activity.colorFromRes(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)
@ColorInt inline fun Fragment.colorFromRes(@ColorRes id: Int): Int = ContextCompat.getColor(this.requireContext(), id)
@ColorInt inline fun View.colorFromRes(@ColorRes id: Int): Int = ContextCompat.getColor(this.context, id)
@ColorInt inline fun Context.colorFromRes(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

inline fun Activity.colorStateListFromRes(@ColorRes id: Int): ColorStateList? = AppCompatResources.getColorStateList(this, id)
inline fun Fragment.colorStateListFromRes(@ColorRes id: Int): ColorStateList? = AppCompatResources.getColorStateList(this.requireContext(), id)
inline fun View.colorStateListFromRes(@ColorRes id: Int): ColorStateList? = AppCompatResources.getColorStateList(this.context, id)
inline fun Context.colorStateListFromRes(@ColorRes id: Int): ColorStateList? = AppCompatResources.getColorStateList(this, id)

/**
 * Drawable Resource Inflation
 */

inline fun Activity.drawableFromRes(@DrawableRes id: Int): Drawable = AppCompatResources.getDrawable(this, id)!!
inline fun Fragment.drawableFromRes(@DrawableRes id: Int): Drawable = AppCompatResources.getDrawable(this.requireContext(), id)!!
inline fun View.drawableFromRes(@DrawableRes id: Int): Drawable = AppCompatResources.getDrawable(this.context, id)!!
inline fun Context.drawableFromRes(@DrawableRes id: Int): Drawable = AppCompatResources.getDrawable(this, id)!!

/**
 * Dimension Resource Inflation
 */

inline fun Activity.dimenFromRes(@DimenRes dimen: Int) = this.resources.getDimension(dimen)
inline fun Fragment.dimenFromRes(@DimenRes dimen: Int) = this.resources.getDimension(dimen)
inline fun View.dimenFromRes(@DimenRes dimen: Int) = this.resources.getDimension(dimen)
inline fun Context.dimenFromRes(@DimenRes dimen: Int) = this.resources.getDimension(dimen)

/**
 * SP AND DP
 *
 */

fun Activity.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Fragment.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun View.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Activity.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun Fragment.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun View.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

fun Activity.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Fragment.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun View.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

fun Activity.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Fragment.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun View.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
