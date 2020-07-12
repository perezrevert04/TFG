package com.example.proyectonfc.data;

import android.content.Context;
import android.util.Xml;

import com.example.proyectonfc.model.Student;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;

public class StorageStudentsAttendance {

    private static final String TAG = "students_attendance";
    private static final String ID_TAG = "id";
    private static final String DNI_TAG = "dni";
    private static final String NAME_TAG = "name";

    private static final String EXTENSION_FILE = ".xml";
    private static final String ENCODING = String.valueOf(Xml.Encoding.UTF_8);

    private Context context;
    private String filename;

    public StorageStudentsAttendance(Context context, String filename) {
        this.context = context;
        this.filename = filename + EXTENSION_FILE;
    }

    public void saveStudentsList(List<Student> studentsList) {
        try {
            OutputStream os = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writeXml(os, studentsList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeXml(OutputStream output, List<Student> studentsList) {
        XmlSerializer serializer = Xml.newSerializer();

        try {

            serializer.setOutput(output, ENCODING);
            serializer.startDocument(ENCODING, true);
            serializer.startTag("", TAG);

            for (Student student : studentsList) {
                serializer.startTag("", ID_TAG);
                serializer.text(student.getId());
                serializer.endTag("", ID_TAG);

                serializer.startTag("", DNI_TAG);
                serializer.text(student.getDni());
                serializer.endTag("", DNI_TAG);

                serializer.startTag("", NAME_TAG);
                serializer.text(student.getName());
                serializer.endTag("", NAME_TAG);
            }

            serializer.endTag("", TAG);
            serializer.endDocument();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
