package com.ananth.actorsearch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Celebrity(val name: String, val wikipedia: Wikipedia) : Parcelable

@Parcelize
data class Wikipedia(val articles:List<WikipediaArticle>) : Parcelable

@Parcelize
data class WikipediaArticle(val title: String, val url: String) : Parcelable