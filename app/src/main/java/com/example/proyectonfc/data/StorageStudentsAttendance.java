package com.example.proyectonfc.data;

import android.content.Context;
import android.util.Xml;

import com.example.proyectonfc.model.Student;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class StorageStudentsAttendance {

    private static final String TAG = "students_attendance";
    private static final String STUDENT_TAG = "student";
    private static final String ID_TAG = "id";
    private static final String DNI_TAG = "dni";
    private static final String NAME_TAG = "name";

    private static final String EXTENSION_FILE = ".xml";
    private static final String ENCODING = String.valueOf(Xml.Encoding.UTF_8);

    private Context context;
    private String filename;
    private List<Student> list;

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

    public List<Student> getStudentsList() {
        list = new ArrayList<>();
        try {
            readXml(context.openFileInput(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void readXml(InputStream input) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            HandlerXml handlerXml = new HandlerXml();
            reader.setContentHandler(handlerXml);
            reader.parse(new InputSource(input));
        } catch (Exception e) {
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
                serializer.startTag("", STUDENT_TAG);

                serializer.startTag("", ID_TAG);
                serializer.text(student.getId());
                serializer.endTag("", ID_TAG);

                serializer.startTag("", DNI_TAG);
                serializer.text(student.getDni());
                serializer.endTag("", DNI_TAG);

                serializer.startTag("", NAME_TAG);
                serializer.text(student.getName());
                serializer.endTag("", NAME_TAG);

                serializer.endTag("", STUDENT_TAG);
            }

            serializer.endTag("", TAG);
            serializer.endDocument();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class HandlerXml extends DefaultHandler {

        private Student student;
        private String text;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals(STUDENT_TAG)) student = new Student();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            text = String.valueOf(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            switch (localName) {
                case ID_TAG:
                    student.setId(text);
                    break;
                case DNI_TAG:
                    student.setDni(text);
                    break;
                case NAME_TAG:
                    student.setName(text);
                    break;
                case STUDENT_TAG:
                    list.add(student);
                    break;
            }
        }
    }
}
