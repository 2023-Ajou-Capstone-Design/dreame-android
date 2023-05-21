package com.example.dreamixmlversion.extension

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

fun String.toToast(context: Context) =
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()