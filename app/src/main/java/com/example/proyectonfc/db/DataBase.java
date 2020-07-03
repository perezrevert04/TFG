package com.example.proyectonfc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    private static final String NOMBRE_DB = "DB6.db";
    private static final int VERSION_DB = 1;
    private static final String TABLA_ALUMNO = "CREATE TABLE ALUMNO(ID TEXT PRIMARY KEY, DNI TEXT, NOMBRE TEXT)";
    private static final String TABLA_PROFESOR = "CREATE TABLE PROFESOR(ID TEXT PRIMARY KEY, DNI TEXT, NOMBRE TEXT)";
    private static final String TABLA_ASIGNATURA = "CREATE TABLE ASIGNATURA(ID TEXT PRIMARY KEY, NOMBRE TEXT, TITULACION TEXT, CURSO TEXT, ER_GESTORA TEXT, IDIOMA TEXT, DURACION TEXT)";
    private static final String TABLA_GRUPO = "CREATE TABLE GRUPO(ID TEXT PRIMARY KEY, GRUPO TEXT, H_ENTRADA NUMERIC, H_SALIDA NUMERIC, AULA TEXT)";
    private static final String TABLA_ALUMNO_TEMPORAL = "CREATE TABLE ALUMNOTEMPORAL(ID TEXT PRIMARY KEY)";
    private String listIdentificador = null;
    private String listDni = null;
    private String listNombre = null;
    private String listaAsignaturas = null;



    public DataBase(Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    public DataBase(Context context, String nombreDB, Object o, int version) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLA_ALUMNO);
        sqLiteDatabase.execSQL(TABLA_PROFESOR);
        sqLiteDatabase.execSQL(TABLA_ASIGNATURA);
        sqLiteDatabase.execSQL(TABLA_GRUPO);
        sqLiteDatabase.execSQL(TABLA_ALUMNO_TEMPORAL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_ALUMNO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_PROFESOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_ASIGNATURA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_GRUPO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_ALUMNO_TEMPORAL);
        sqLiteDatabase.execSQL(TABLA_ALUMNO);
        sqLiteDatabase.execSQL(TABLA_PROFESOR);
        sqLiteDatabase.execSQL(TABLA_ASIGNATURA);
        sqLiteDatabase.execSQL(TABLA_GRUPO);
        sqLiteDatabase.execSQL(TABLA_ALUMNO_TEMPORAL);
    }

    public void borrarTodo() {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM ASIGNATURA");
            db.execSQL("DELETE FROM PROFESOR");
            db.execSQL("DELETE FROM ALUMNO");
            db.execSQL("DELETE FROM GRUPO");
            db.execSQL("DELETE FROM ALUMNOTEMPORAL");
            db.close();
        }
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

//    public void agregarAlumnoTemporal(String identificador) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        if(db!=null) {
//            db.execSQL("INSERT INTO ALUMNOTEMPORAL VALUES('"+identificador+"') ");
//            db.close();
//        }
//    }

    public void agregarAlumno(String identificador, String dni, String nombre) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO ALUMNO VALUES('"+identificador+"','"+dni+"','"+nombre+"') ");
            db.close();
        }
    }

    public void insertarAlumno(String asignatura) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO ALUMNO (ID, DNI, NOMBRE) VALUES('"+asignatura+"1111111111','11111111','Alumno1') ");
            db.execSQL("INSERT INTO ALUMNO (ID, DNI, NOMBRE) VALUES('"+asignatura+"2222222222','22222222','Alumno2') ");
            db.execSQL("INSERT INTO ALUMNO (ID, DNI, NOMBRE) VALUES('"+asignatura+"3333333333','33333333','Alumno3') ");
            db.execSQL("INSERT INTO ALUMNO (ID, DNI, NOMBRE) VALUES('"+asignatura+"4444444444','44444444','Alumno4') ");
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

    public void agregarProfesor(String identificador, String dni, String nombre) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO PROFESOR VALUES('"+identificador+"','"+dni+"','"+nombre+"') ");
            db.close();
        }
    }

    public void insertarProfesor(String asignatura) {
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null) {
            db.execSQL("INSERT INTO PROFESOR (ID, DNI, NOMBRE) VALUES('"+asignatura+"1111111111','11111111','Profesor1') ");
            db.execSQL("INSERT INTO PROFESOR (ID, DNI, NOMBRE) VALUES('"+asignatura+"2222222222','22222222','Profesor2') ");
            db.execSQL("INSERT INTO PROFESOR (ID, DNI, NOMBRE) VALUES('"+asignatura+"3333333333','33333333','Profesor3') ");
            db.execSQL("INSERT INTO PROFESOR (ID, DNI, NOMBRE) VALUES('"+asignatura+"4444444444','44444444','Profesor4') ");
            db.close();
        }
    }

    public void borrarProfesor(String identificador, String nombre, String dni) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("DELETE FROM PROFESOR WHERE id LIKE '"+identificador+"%' AND nombre ='"+nombre+"' AND dni='"+dni+"'");
            db.close();
        }
    }

    public void ActualizarProfesor(String identificador, String nombre, String dni) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("UPDATE PROFESOR SET nombre='"+nombre+"', dni='"+dni+"' " +
                    "WHERE id = '"+identificador+"'");
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

    public void ActualizarAsignatura(String identificador, String nombre, String titulacion, String curso, String gestora, String idioma, String duracion) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("UPDATE ASIGNATURA SET nombre='"+nombre+"', titulacion='"+titulacion+"', curso='"+curso+"', er_gestora='"+gestora+"', idioma='"+idioma+"', duracion='"+duracion+"' WHERE id='"+identificador+"'");
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

    public void ActualizarGrupo(String identificador, String entrada, String salida, String aula) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null) {
            db.execSQL("UPDATE GRUPO SET h_entrada='"+entrada+"', h_salida='"+salida+"', aula='"+aula+"' WHERE id = '"+identificador+"'");
            db.close();
        }
    }

    public void consultarProfesor(String asignatura, String identificador) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, dni, nombre FROM PROFESOR WHERE id ='"+asignatura+identificador+"'",null);
        //String[] listIdentificador1 = new String[1000];
        String listIdentificador1 = null;
        //String[] listDni1 = new String[1000];
        String listDni1= null;
        //String[] listNombre1 = new String[1000];
        String listNombre1=null;

        if(cursor != null){
            cursor.moveToFirst();
            do {
                //listIdentificador1[count] =cursor.getString(0);
                listIdentificador1 =cursor.getString(0);
                //listDni1[count] =cursor.getString(1);
                listDni1 =cursor.getString(1);
                //listNombre1[count] =cursor.getString(2);
                listNombre1 =cursor.getString(2);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        this.setListIdentificador(listIdentificador1);
        this.setListDni(listDni1);
        this.setListNombre(listNombre1);
    }

    public void consultarAlumno(String asignatura, String identificador) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, dni, nombre FROM ALUMNO WHERE id ='"+asignatura+identificador+"'",null);
        //String[] listIdentificador1 = new String[1000];
        String listIdentificador1 = null;
        //String[] listDni1 = new String[1000];
        String listDni1= null;
        //String[] listNombre1 = new String[1000];
        String listNombre1=null;

        if(cursor != null){
            cursor.moveToFirst();
            do {
                //listIdentificador1[count] =cursor.getString(0);
                listIdentificador1 =cursor.getString(0);
                //listDni1[count] =cursor.getString(1);
                listDni1 =cursor.getString(1);
                //listNombre1[count] =cursor.getString(2);
                listNombre1 =cursor.getString(2);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        this.setListIdentificador(listIdentificador1);
        this.setListDni(listDni1);
        this.setListNombre(listNombre1);
    }

    public void consultarAlumnoTemporal(String asignatura, String identificador) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, dni, nombre FROM ALUMNOTEMPORAL WHERE id ='"+asignatura+identificador+"'",null);
        //String[] listIdentificador1 = new String[1000];
        String listIdentificador1 = null;
        //String[] listDni1 = new String[1000];
        String listDni1= null;
        //String[] listNombre1 = new String[1000];
        String listNombre1=null;

        if(cursor != null){
            cursor.moveToFirst();
            do {
                //listIdentificador1[count] =cursor.getString(0);
                listIdentificador1 =cursor.getString(0);
                //listDni1[count] =cursor.getString(1);
                listDni1 =cursor.getString(1);
                //listNombre1[count] =cursor.getString(2);
                listNombre1 =cursor.getString(2);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        this.setListIdentificador(listIdentificador1);
        this.setListDni(listDni1);
        this.setListNombre(listNombre1);
    }



    public void consultarAsignaturas() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorAsignaturas = db.rawQuery("SELECT id FROM ASIGNATURA", null);
        String listaAsignaturas1 = null;

        if(cursorAsignaturas != null){
            cursorAsignaturas.moveToFirst();
            do {
                //listIdentificador1[count] =cursor.getString(0);
                listaAsignaturas1 = cursorAsignaturas.getString(0);

            }while (cursorAsignaturas.moveToNext());
        }
        cursorAsignaturas.close();
        db.close();
        this.setListaAsignaturas(listaAsignaturas1);
    }


    public void setListIdentificador(String listIdentificador){ this.listIdentificador = listIdentificador; }
    public void setListDni(String listDni){
        this.listDni = listDni;
    }
    public void setListNombre(String listNombre){
        this.listNombre = listNombre;
    }
    public void setListaAsignaturas(String listaAsignaturas){
        this.listaAsignaturas = listaAsignaturas;
    }




    public String getListIdentificador() {
        return this.listIdentificador;
    }
    public String getListDni(){
        return this.listDni;
    }
    public String getListNombre(){
        return this.listNombre;
    }
    public String getListaAsignaturas(){
        return listaAsignaturas;

    }


}
