/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meneamedata.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.attribute.standard.Finishings;
import javax.sql.DataSource;

import org.apache.commons.lang3.math.NumberUtils;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;
import org.postgresql.util.HostSpec;

/**
 *
 * @author yonseca
 */
public class PostgreDButils {
    
    private Connection connection;
    private PreparedStatement insert;
    
    /**
     * Turns up a database connection
     * @throws SQLException if boom
     */
    public void connect() throws SQLException{
        try {
            HostSpec[] hostSpec = {new HostSpec(Tokens.SERVER , Tokens.PORT)}; 
            Properties prop = new Properties(); 
            prop.setProperty("password", Tokens.PASSWORD); 
            connection = new PgConnection(hostSpec, Tokens.USERNAME, Tokens.DATABASE, prop, "");
            insert = connection.prepareStatement(Tokens.INSERT_MENEO); 
        } catch (SQLException ex) {
            Logger.getLogger(PostgreDButils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e){
            Logger.getLogger(PostgreDButils.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Closes the database connection
     */
    public void close(){
    	try {
    		if (connection != null && !connection.isClosed()) {
    			connection.close();				
			}
		} catch (SQLException e) {
			System.out.println("Close database connection failed. ");
		}
    }
    
    /**
     * Inserts some Meneos into the database 
     * @param listaMeneos - A set containing Meneos
     * @return int[] containing the result of each insert. 
     */
    public int[] insertData(Set<Meneo> listaMeneos) throws SQLException{

    	int[] resul = new int[0]; 
    	try {
			if (connection == null || connection.isClosed()) {
				connect();
			}
    		connection.setAutoCommit(false);
        	for (Meneo meneo : listaMeneos) {
        		Timestamp datePublished = new Timestamp(meneo.getPublished()*1000);
        		Timestamp dateSent = new Timestamp(meneo.getSent()*1000);
        		Integer votesAnonymous = meneo.getVotes().get("anonymous"); 
        		Integer votesNegative = meneo.getVotes().get("negative"); 
        		Integer votesUsers = meneo.getVotes().get("users"); 
        		
    			insert.setInt(1, meneo.getId());
    			insert.setString(2, meneo.getAuthor());
    			insert.setString(3, meneo.getBody());
    			insert.setInt(4, meneo.getComments());
    			insert.setInt(5, meneo.getKarma());
    			insert.setTimestamp(6, datePublished);
    			insert.setTimestamp(7, dateSent);
    			insert.setString(8, meneo.getStory());
    			insert.setString(9, meneo.getSub());
    			insert.setString(10, meneo.getTags());
    			insert.setString(11, meneo.getTitle());
    			insert.setString(12, meneo.getUrl());
    			insert.setInt(13, votesAnonymous);
    			insert.setInt(14, votesNegative);
    			insert.setInt(15, votesUsers);
    			insert.addBatch();
    		}
        	System.out.println("Inserting...");
        	resul = insert.executeBatch();
        	connection.commit();
		} catch (SQLException e) {
			System.out.println(e.getNextException());
			System.out.println("## INSERT FAIL: Doing rollback! ## ");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println("Rollback failed :(");
				System.out.println(e1.getMessage());
			}
		} finally {
			close();
		}

    	return resul; 
    }
    
    
    
}
