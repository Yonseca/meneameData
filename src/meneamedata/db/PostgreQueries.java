package meneamedata.db;

public class PostgreQueries {
	public static final String INSERT_MENEO = "INSERT INTO public.meneos "
			+ "(id, author, body, comments, karma, published, sent, story, sub, tags, title, url, votes_anonymous, votes_negative, votes_users) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "; // There are 15 '?'
	
	public static final String UPSERT_MENEO = PostgreQueries.INSERT_MENEO + 
			"ON CONFLICT (id) " + 
			"DO UPDATE SET " + 
			    "author=?, " + 
			    "body=?, " + 
			    "comments=?, " + 
			    "karma=?, " + // 5
			    "published=?, " + 
			    "sent=?, " + 
			    "story=?," + 
			    "sub=?, " + 
			    "tags=?, " + // 10
			    "title=?, " + 
			    "url=?, " + 
			    "votes_anonymous=?, " + 
			    "votes_negative=?, " + 
			    "votes_users=? "; //15
}
