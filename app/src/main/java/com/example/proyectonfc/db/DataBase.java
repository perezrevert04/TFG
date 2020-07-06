package com.example.proyectonfc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.proyectonfc.model.Group;
import com.example.proyectonfc.model.Subject;

public class DataBase extends SQLiteOpenHelper {

    private static final String NOMBRE_DB = "DB6.db";
    private static final int VERSION_DB = 2;
    private static final String TABLA_ALUMNO = "CREATE TABLE ALUMNO(ID TEXT PRIMARY KEY, DNI TEXT, NOMBRE TEXT)";
    private static final String TABLA_ASIGNATURA = "CREATE TABLE ASIGNATURA(ID TEXT PRIMARY KEY, NOMBRE TEXT, TITULACION TEXT, CURSO TEXT, ER_GESTORA TEXT, IDIOMA TEXT, DURACION TEXT)";
    private static final String TABLA_GRUPO = "CREATE TABLE GRUPO(ID TEXT PRIMARY KEY, GRUPO TEXT, H_ENTRADA NUMERIC, H_SALIDA NUMERIC, AULA TEXT)";
    private String listDni = null;
    private String listNombre = null;

    public DataBase(Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLA_ALUMNO);
        sqLiteDatabase.execSQL(TABLA_ASIGNATURA);
        sqLiteDatabase.execSQL(TABLA_GRUPO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ALUMNO");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ASIGNATURA");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS GRUPO");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ALUMNOTEMPORAL");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PROFESOR");

        onCreate(sqLiteDatabase);
    }

    public void borrarTodoProfesores(String identificador) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM PROFESOR WHERE id LIKE '"+identificador+"%'");
            db.close();
        }
    }

    public void borrarTodoAlumnos(String identificador) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM ALUMNO WHERE id LIKE '"+identificador+"%'");
            db.close();
        }
    }

    public void borrarTodoGrupos(String identificador) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM GRUPO WHERE id LIKE '"+identificador+"%'");
            db.close();
        }
    }

    public void agregarAlumno(String identificador, String dni, String nombre) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO ALUMNO VALUES('"+identificador+"','"+dni+"','"+nombre+"') ");
            db.close();
        }
    }

    public void borrarAlumno(String identificador, String nombre, String dni) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM ALUMNO WHERE id LIKE '"+identificador+"%' AND nombre ='"+nombre+"' AND dni='"+dni+"'");
            db.close();
        }
    }

    public void ActualizarAlumno(String identificador, String nombre, String dni) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("UPDATE ALUMNO SET nombre='"+nombre+"', dni='"+dni+"' WHERE id = '"+identificador+"'");
            db.close();
        }
    }

    public void agregarAsignatura(String identificador, String nombre, String titulacion, String curso, String gestora, String idioma, String duracion) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO ASIGNATURA VALUES('"+identificador+"','"+nombre+"','"+titulacion+"','"+curso+"','"+gestora+"','"+idioma+"','"+duracion+"') ");
            db.close();
        }
    }

    public void borrarAsignatura(String identificador) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM ASIGNATURA WHERE id='"+identificador+"'");
            db.close();
        }
    }

    public void updateSubject(Subject subject) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("UPDATE ASIGNATURA SET nombre='"+subject.getName()+"', titulacion='"+subject.getDegree()+"', curso='"+subject.getSchoolYear()+"', er_gestora='"+subject.getDepartment()+"', idioma='"+subject.getLanguage()+"', duracion='"+subject.getDuration()+"' WHERE id='"+subject.getCode()+"'");
            db.close();
        }
    }

    public void agregarGrupo(String identificador, String grupo, String h_entrada, String h_salida, String aula) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO GRUPO VALUES('"+identificador+"','"+grupo+"','"+h_entrada+"','"+h_salida+"','"+aula+"') ");
            db.close();
        }
    }

    public void borrarGrupo(String identificador, String grupo) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM GRUPO WHERE id LIKE '"+identificador+"%' AND grupo ='"+grupo+"'");
            db.close();
        }
    }

    public void updateGroup(Group group) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null) {
            db.execSQL("UPDATE GRUPO SET h_entrada='"+group.getHour()+"', h_salida='"+group.getEnd()+"', aula='"+group.getClassroom()+"' WHERE id = '"+group.getCode()+"'");
            db.close();
        }
    }

    public void consultarAlumno(String asignatura, String identificador) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, dni, nombre FROM ALUMNO WHERE id ='"+asignatura+identificador+"'",null);
        String listDni1= null;
        String listNombre1=null;

        if(cursor != null){
            cursor.moveToFirst();
            do {
                listDni1 =cursor.getString(1);
                listNombre1 =cursor.getString(2);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        this.setListDni(listDni1);
        this.setListNombre(listNombre1);
    }

    public void setListDni(String listDni){
        this.listDni = listDni;
    }
    public void setListNombre(String listNombre){
        this.listNombre = listNombre;
    }
    public String getListDni(){
        return this.listDni;
    }
    public String getListNombre(){
        return this.listNombre;
    }


}
