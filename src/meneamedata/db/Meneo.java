/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meneamedata.db;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author yonsecaS
 */
public class Meneo implements Comparable<Meneo> {

	private int id;
	private String author;
	private String body;
	private int comments;
	private int karma;
	private long published;
	private long sent;
	private String story;
	private String sub;
	private String tags;
	private String title;
	private String url;
	private LinkedHashMap<String, Integer> votes;

	public Meneo(Map<String, Object> node) {
		this.id = (int) node.get("id");
		this.author = (String) node.get("author");
		this.body = (String) node.get("body");
		this.comments = (int) node.get("comments");
		this.karma = (int) node.get("karma");
		this.published = NumberUtils.toLong(node.get("published").toString());
		this.sent = NumberUtils.toLong(node.get("sent").toString());
		this.story = (String) node.get("story");
		this.sub = (String) node.get("sub");
		this.tags = (String) node.get("tags");
		this.title = (String) node.get("title");
		this.url = (String) node.get("url");
		this.votes = (LinkedHashMap<String, Integer>) node.get("votes");
	}

	public Meneo(int id, String author, String body, int comments, int karma, long published, long sent, String story,
			String sub, String tags, String title, String url, LinkedHashMap<String, Integer> votes) {
		this.id = id;
		this.author = author;
		this.body = body;
		this.comments = comments;
		this.karma = karma;
		this.published = published;
		this.sent = sent;
		this.story = story;
		this.sub = sub;
		this.tags = tags;
		this.title = title;
		this.url = url;
		this.votes = votes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getKarma() {
		return karma;
	}

	public void setKarma(int karma) {
		this.karma = karma;
	}

	public long getPublished() {
		return published;
	}

	public void setPublished(long published) {
		this.published = published;
	}

	public long getSent() {
		return sent;
	}

	public void setSent(long sent) {
		this.sent = sent;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LinkedHashMap<String, Integer> getVotes() {
		return votes;
	}

	public void setVotes(LinkedHashMap<String, Integer> votes) {
		this.votes = votes;
	}

	@Override
	public int compareTo(Meneo o) {
		return id - o.getId();
	}

}
