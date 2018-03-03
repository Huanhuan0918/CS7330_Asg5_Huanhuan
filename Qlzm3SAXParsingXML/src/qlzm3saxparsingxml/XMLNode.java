/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qlzm3saxparsingxml;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 *
 * @author leiqi
 */
public class XMLNode {
    public String name = "";
    public String content = "";
    
    public LinkedHashMap<String, ArrayList<XMLNode>> properties;
    public LinkedHashMap<String, String> attributes;
    
    public int label = 0;
    private String space = "";

    
    public XMLNode(){}
    
    public XMLNode(String name, String content, LinkedHashMap<String, ArrayList<XMLNode>> properties, LinkedHashMap<String, String> attributes) {
        this.name = name;
        this.content = content;
        this.properties = properties;
        this.attributes = attributes;
    }
    
    public String getSpace(){
        for(int i = 0; i<label; i++){
            space = space + "   ";
        }       
        return space;
    }       
}
