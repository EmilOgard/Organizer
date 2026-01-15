package no.emil.organizer

import android.app.Application
import androidx.room.Room
import no.emil.organizer.data.database.OrganizerDatabase

class OrganizerApp : Application() {
    lateinit var db: OrganizerDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            OrganizerDatabase::class.java,
            "organizer.db"
        ).build()
    }
}