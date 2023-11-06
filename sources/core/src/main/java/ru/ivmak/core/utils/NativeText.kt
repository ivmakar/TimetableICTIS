package ru.ivmak.core.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class NativeText {
    data class Simple(val text: String) : NativeText()
    data class Resource(@StringRes val id: Int) : NativeText()
    data class Arguments(
        @StringRes val id: Int, val args: List<Any>
    ) : NativeText()
    data class Multi(val text: List<NativeText>) : NativeText()
}

fun NativeText.toCharSequence(context: Context): CharSequence {
    return when (this) {
        is NativeText.Resource -> context.getString(id)
        is NativeText.Simple -> text
        is NativeText.Arguments -> context.getString(id, args)
        is NativeText.Multi -> text.joinToString { it.toCharSequence(context) }
    }
}

fun String.toNativeText(): NativeText = NativeText.Simple(this)