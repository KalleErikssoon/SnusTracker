package com.kalleerikssoon.snustracker.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Data class for individual markers in a cluster on a Google Map.
 * Implements the ClusterItem interface, allowing it to be used with clusters in the map.
 *
 * @property itemPosition LatLng coordinates of the marker on the map.
 * @property itemTitle title of the marker, displayed when the marker is clicked.
 * @property itemSnippet snippet of additional information, displayed when the marker is clicked.
 */
data class SnusClusterItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String
) : ClusterItem {
    override fun getPosition(): LatLng = itemPosition
    override fun getTitle(): String = itemTitle
    override fun getSnippet(): String = itemSnippet
    override fun getZIndex(): Float = 0f
}

