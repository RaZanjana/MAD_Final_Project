package com.example.fillandgo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "carsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String CARS_TABLE = "cars";
    private static final String ID = "id";
    private static final String CAR_NAME = "name";
    private static final String CAR_FUELTYPE= "fuel";


    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    public void onCreate( SQLiteDatabase db ) {
        // build sql create statement
        String sqlCreate = "create table " + CARS_TABLE + "( " + ID;
        sqlCreate += " integer primary key autoincrement, " + CAR_NAME;
        sqlCreate += " text, " + CAR_FUELTYPE + " text"+")" ;

        db.execSQL( sqlCreate );
    }

    public void onUpgrade( SQLiteDatabase db,
                           int oldVersion, int newVersion ) {
        // Drop old table if it exists
        db.execSQL( "drop table if exists " + CARS_TABLE );
        // Re-create tables
        onCreate( db );
    }

    public void insert( Car_get_set cars) {
        SQLiteDatabase db = this.getWritableDatabase( );
        String sqlInsert = "insert into " + CARS_TABLE;
        sqlInsert += " values( null, '" + cars.getCarName( );
        sqlInsert += "', '" + cars.getFuelType( ) + "' )";

        db.execSQL( sqlInsert );
        db.close( );
    }

    public void deleteById( int id ) {
        SQLiteDatabase db = this.getWritableDatabase( );
        String sqlDelete = "delete from " + CARS_TABLE;
        sqlDelete += " where " + ID + " = " + id;

        db.execSQL( sqlDelete );
        db.close( );
    }



    public ArrayList<Car_get_set> selectAll( ) {
        String sqlQuery = "select * from " + CARS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase( );
        Cursor cursor = db.rawQuery( sqlQuery, null );

        ArrayList<Car_get_set> cars = new ArrayList<Car_get_set>( );
        while( cursor.moveToNext( ) ) {
            Car_get_set car
                    = new Car_get_set( Integer.parseInt( cursor.getString( 0 ) ),
                    cursor.getString( 1 ), cursor.getString( 2 ));
            cars.add( car );
        }
        db.close( );
        return cars;
    }


}
