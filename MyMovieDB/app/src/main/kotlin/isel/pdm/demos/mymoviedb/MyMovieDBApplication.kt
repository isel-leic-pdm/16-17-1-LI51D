package isel.pdm.demos.mymoviedb

import android.app.Application
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo


class MyMovieDBApplication(var apiConfigurationInfo: ConfigurationInfo?) : Application() {

    public constructor() : this(null)
}