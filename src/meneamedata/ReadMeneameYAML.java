/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meneamedata;

import java.io.File;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import meneamedata.db.Meneo;
import meneamedata.db.PostgreDButils;

/**
 *
 * @author yonseca
 */
public class ReadMeneameYAML {
    
	
    public static void main(String[] args) {
        
    	TreeSet<Meneo> meneos = new TreeSet<Meneo>();  
        readYaml(meneos);
        PostgreDButils db = new PostgreDButils(); 	
        try {
			db.insertData(meneos);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        
    }

	/**
	 * Read YAML file
	 * @param meneos - A TreeSet including all Meneos
	 */
	private static void readYaml(TreeSet<Meneo> meneos) {
		try {
        	Yaml yaml = new Yaml(); 
        	File datos = new File("data/meneame.yml"); 
        	StringReader reader = new StringReader(FileUtils.readFileToString(datos, "UTF-8"));
        	Iterable<Object> iter = yaml.loadAll(reader); 
        	for (Object node : iter) {
        		if (node != null) {
    				Map<String, Object> m = (Map<String, Object>) node; 
    				Meneo meneo = new Meneo(m); 
    				meneos.add(meneo); 
				}
			}
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
	}
}
