package com.app.monrotv.ui.dashboard.generic

import com.app.monrotv.ui.dashboard.adapters.GenericAdapter

abstract class AddressListItem {
    var adapterPosition: Int = -1
    var adapterCallbacks: GenericAdapter.AdapterCallbacks<*>? = null
}