import android.content.Context //all below is from chatGpt adding databases all me
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TaskShare.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create your tables here
        db.execSQL("CREATE TABLE IF NOT EXISTS Users (user_id INTEGER PRIMARY KEY, groupID INTEGER, task_id INTEGER, first_name TEXT, last_name TEXT, email TEXT, phoneNumber INTEGER)")
        db.execSQL("CREATE TABLE IF NOT EXISTS Groups (group_id INTEGER PRIMARY KEY, group_name TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS Tasks (task_id INTEGER PRIMARY KEY, groupID INTEGER, task_name TEXT, assigner_id INTEGER)")
        db.execSQL("CREATE TABLE IF NOT EXISTS SubTasks (subTask_id INTEGER PRIMARY KEY,assignee_Id INTEGER, subTask_name TEXT, task_status TEXT, comments TEXT, start_date TEXT, end_date TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Perform any necessary migrations here
        //db.execSQL("DROP TABLE IF EXISTS my_table")
        onCreate(db)
    }
}
