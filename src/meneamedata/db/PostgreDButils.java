/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meneamedata.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.util.HostSpec;

/**
 *
 * @author yonseca
 */
public class PostgreDButils {

	private static final int BATCH_MAX = 5000;
	private Connection connection;
	private PreparedStatement insert;
	private PreparedStatement upsert;


	/**
	 * Turns up a database connection
	 * 
	 * @throws SQLException
	 *             if boom
	 */
	public void connect() throws SQLException {
		try {
			HostSpec[] hostSpec = { new HostSpec(Tokens.SERVER, Tokens.PORT) };
			Properties prop = new Properties();
			prop.setProperty("password", Tokens.PASSWORD);
			connection = new PgConnection(hostSpec, Tokens.USERNAME, Tokens.DATABASE, prop, "");
			insert = connection.prepareStatement(PostgreQueries.INSERT_MENEO);
			upsert = connection.prepareStatement(PostgreQueries.UPSERT_MENEO);

		} catch (SQLException ex) {
			Logger.getLogger(PostgreDButils.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			Logger.getLogger(PostgreDButils.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * Closes the database connection
	 */
	public void close() {
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
	 * 
	 * @param listaMeneos
	 *            - A set containing Meneos
	 * @return int[] containing the result of each insert.
	 */
	public int[] insertData(Set<Meneo> listaMeneos) throws SQLException {

		int[] resul = new int[0];
		boolean autoCommit = connection.getAutoCommit();
		try {
			if (connection == null || connection.isClosed()) {
				connect();
			}
			connection.setAutoCommit(false);
			for (Meneo meneo : listaMeneos) {
				Timestamp datePublished = new Timestamp(meneo.getPublished() * 1000);
				Timestamp dateSent = new Timestamp(meneo.getSent() * 1000);
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
			connection.setAutoCommit(autoCommit);
			close();
		}

		return resul;
	}
	
	/**
	 * 
	 * @param listaMeneos
	 * @return
	 * @throws SQLException
	 */
	public int[] upsertMeneos(Set<Meneo> listaMeneos) throws SQLException {
		int[] resul = new int[0];
		boolean autoCommit = false; 
		int batches = 0; 
		try {
			if (connection == null || connection.isClosed()) {
				connect();
			}
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			for (Meneo meneo : listaMeneos) {
				Timestamp datePublished = new Timestamp(meneo.getPublished() * 1000);
				Timestamp dateSent = new Timestamp(meneo.getSent() * 1000);
				Integer votesAnonymous = meneo.getVotes().get("anonymous");
				Integer votesNegative = meneo.getVotes().get("negative");
				Integer votesUsers = meneo.getVotes().get("users");
				int column = 1; 
				//insert into
				upsert.setInt(column, meneo.getId()); ++column;
				upsert.setString(column, meneo.getAuthor()); ++column;
				upsert.setString(column, meneo.getBody()); ++column;
				upsert.setInt(column, meneo.getComments()); ++column;
				upsert.setInt(column, meneo.getKarma()); ++column;
				upsert.setTimestamp(column, datePublished); ++column;
				upsert.setTimestamp(column, dateSent); ++column;
				upsert.setString(column, meneo.getStory()); ++column;
				upsert.setString(column, meneo.getSub()); ++column;
				upsert.setString(column, meneo.getTags()); ++column; 
				upsert.setString(column, meneo.getTitle()); ++column;
				upsert.setString(column, meneo.getUrl()); ++column;
				upsert.setInt(column, votesAnonymous); ++column;
				upsert.setInt(column, votesNegative); ++column;
				upsert.setInt(column, votesUsers); ++column;
				
				// update
				upsert.setString(column, meneo.getAuthor());++column;
				upsert.setString(column, meneo.getBody());++column;
				upsert.setInt(column, meneo.getComments());++column;
				upsert.setInt(column, meneo.getKarma());++column;
				upsert.setTimestamp(column, datePublished);++column;
				upsert.setTimestamp(column, dateSent);++column;
				upsert.setString(column, meneo.getStory());++column;
				upsert.setString(column, meneo.getSub());++column;
				upsert.setString(column, meneo.getTags());++column;
				upsert.setString(column, meneo.getTitle());++column;
				upsert.setString(column, meneo.getUrl());++column;
				upsert.setInt(column, votesAnonymous);++column;
				upsert.setInt(column, votesNegative);++column;
				upsert.setInt(column, votesUsers); 
				upsert.addBatch();
				batches++;
				if(batches == BATCH_MAX) {
					resul = ArrayUtils.addAll(resul, upsert.executeBatch()); 
					batches = 0; 
				}
			}
			System.out.println(PostgreQueries.UPSERT_MENEO);
			resul = ArrayUtils.addAll(resul, upsert.executeBatch()); 
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
			connection.setAutoCommit(autoCommit);
			close();
		}
		
		return resul; 
	}

}
