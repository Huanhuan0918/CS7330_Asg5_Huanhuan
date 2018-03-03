/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classroom;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author dale
 * http://www.saxproject.org/apidoc/org/xml/sax/helpers/DefaultHandler.html
 * https://docs.oracle.com/javase/8/docs/api/org/xml/sax/SAXException.html#SAXException--
 */
public class CourseXMLLoader {
    
    public static Course load(File xmlCourseFile) throws Exception {
        Course course = new Course();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler() {
                Student student = null;
                boolean pawprintFlag = false;
                boolean firstNameFlag = false;
                boolean lastNameFlag = false;
                boolean gradeFlag = false;
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    
                    if (qName.equalsIgnoreCase("student")) {
                        student = new Student();
                        String idString = attributes.getValue("id");
                        if (idString != null) {
                            int id = 0;
                            try {
                                id = Integer.parseInt(idString);
                            } catch (NumberFormatException e) {
                                throw new SAXException("student id in xml could not be converted to an int");
                            }
                            student.setId(id);
                        }
                        
                    }
                    if (qName.equalsIgnoreCase("pawprint")) {
                        pawprintFlag = true;
                    }
                    if (qName.equalsIgnoreCase("firstname")) {
                        firstNameFlag = true;
                    }
                    if (qName.equalsIgnoreCase("lastname")) {
                        lastNameFlag = true;
                    }
                    if (qName.equalsIgnoreCase("grade")) {
                        gradeFlag = true;
                    }
                }
                
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("student")) {
                        course.addStudent(student);
                        student = null;
                    }
                    if (qName.equalsIgnoreCase("pawprint")) {
                        pawprintFlag = false;
                    }
                    if (qName.equalsIgnoreCase("firstname")) {
                        firstNameFlag = false;
                    }
                    if (qName.equalsIgnoreCase("lastname")) {
                        lastNameFlag = false;
                    }
                    if (qName.equalsIgnoreCase("grade")) {
                        gradeFlag = false;
                    }
                }
                
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (pawprintFlag) {
                        if (student != null) {
                            student.setPawprint(new String(ch, start, length));
                        }
                    }
                    if (firstNameFlag) {
                        if (student != null) {
                            student.setFirstName(new String(ch, start, length));
                        }
                    }
                    if (lastNameFlag) {
                        if (student != null) {
                            student.setLastName(new String(ch, start, length));
                        }
                    }
                    if (gradeFlag) {
                        if (student != null) {
                            String gradeString = new String(ch, start, length);
                            double grade = 0.0;
                            try {
                                grade = Double.parseDouble(gradeString);
                            } catch (NumberFormatException e) {
                                throw new SAXException("grade in xml could not be converted to a double");
                            }
                            
                            student.setGrade(grade);
                        }
                    }                    
                }
            };
            
            saxParser.parse(xmlCourseFile.getAbsoluteFile(), handler);
            
        } catch (Exception e) {
            throw e;
        }
        
      return course; 
    }
}
