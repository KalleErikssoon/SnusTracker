package com.kalleerikssoon.snustracker

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class SnusClusterItem(
    val location: LatLng,
    val name: String,
    val description: String?,
    val imageResourceId: Int? = null,
) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String = name

    override fun getSnippet(): String? = description

    override fun getZIndex(): Float = 1f

}
