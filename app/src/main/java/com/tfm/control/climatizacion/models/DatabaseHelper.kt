package com.tfm.control.climatizacion.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4) {

    companion object {
        private const val DATABASE_NAME = "ControlClimatizacion"
        private const val TABLE_NAME = "Rutinas"
        private const val COLUMN_ID = "RoutineId"
        private const val COLUMN_SENSOR_ID = "SensorId"
        private const val COLUMN_PLUG_ID = "PlugId"
        private const val COLUMN_NAME = "Name"
        private const val COLUMN_CONDITION = "Condition"
        private const val COLUMN_ACTION = "ActionValue"
        private const val COLUMN_TEMPERATURE = "Temperature"
        private const val COLUMN_ACTIVE = "Active"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val ceateTableQuery =
            "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_SENSOR_ID TEXT, " +
                    "$COLUMN_PLUG_ID TEXT,$COLUMN_NAME TEXT, $COLUMN_CONDITION BIT, $COLUMN_ACTION INTEGER,$COLUMN_TEMPERATURE DOUBLE, $COLUMN_ACTIVE BIT )"
        db?.execSQL(ceateTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertRoutine(routine: Routine): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SENSOR_ID, routine.sensorId)
            put(COLUMN_PLUG_ID, routine.plugId)
            put(COLUMN_NAME, routine.name)
            put(COLUMN_ACTION, routine.action)
            put(COLUMN_TEMPERATURE, routine.temperatureCondition)
            put(COLUMN_CONDITION, routine.condition.ordinal)
            put(COLUMN_ACTIVE, routine.isActivated)
        }
        val result = db.insert(TABLE_NAME, null, values)

        db.close()
        return  result
    }

    fun getAllRoutines(): ArrayList<Routine> {
        val db = writableDatabase
        val routines = ArrayList<Routine>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val sensorId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SENSOR_ID))
            val plugId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLUG_ID))
            val isActivated = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE)) == 1
            val temperatureCondition =
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TEMPERATURE))
            val iCondition = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                    COLUMN_CONDITION
                )
            )
            val condition = Condition.values()[iCondition]
            val action = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTION)) == 1

            routines.add(
                Routine(
                    id,
                    name,
                    sensorId,
                    plugId,
                    isActivated,
                    temperatureCondition,
                    condition,
                    action
                )
            )
        }
//        db.close()

        return routines


    }
}