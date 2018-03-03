/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qlzm3saxparsingxml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author leiqi
 */
public class XMLParser {

    public static XMLNode load(File xmlCourseFile) throws Exception {
        XMLNode root = new XMLNode();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler;
            handler = new DefaultHandler() {
                
                Stack<XMLNode> stack = new Stack<>();
                XMLNode currentNode = null;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                    XMLNode node = new XMLNode("", "", new LinkedHashMap<>(), new LinkedHashMap<>());
                    node.name = qName;
                    node.label = stack.size()-1;
                    int length = attributes.getLength();
                    for(int i = 0; i< length; i++){
                        node.attributes.put(attributes.getQName(i),attributes.getValue(i));                       
                    }
                    stack.push(node);
                    
                    if (currentNode != null) {
                        if (currentNode.properties.isEmpty()) {
                            currentNode.properties.put(qName, new ArrayList<>(Arrays.asList(node)));
                        } else if (!currentNode.properties.containsKey(qName)) {
                            currentNode.properties.put(qName, new ArrayList<>(Arrays.asList(node)));
                        } else if (currentNode.properties.containsKey(qName)) {
                            ArrayList<XMLNode> currentList;
                            currentList = currentNode.properties.get(qName);
                            currentList.add(node);
                            currentNode.properties.put(qName, currentList);
                        }
                    }
                    currentNode = node;
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {

                    XMLNode tempNode = new XMLNode();
                    tempNode = stack.pop();
                    if (stack.isEmpty()) {
                        root.name = tempNode.name;
                        root.content = tempNode.content;
                        root.attributes = tempNode.attributes;
                        root.properties = tempNode.properties;
                        currentNode = null;
                    }
                    else    
                        currentNode = stack.peek();
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {                    
                    String content = new String(ch, start, length);
                    currentNode.content = content.trim();
                }
            };

            saxParser.parse(xmlCourseFile.getAbsoluteFile(), handler);

        } catch (Exception e) {
            throw e;
        }

        return root;
    }
}
