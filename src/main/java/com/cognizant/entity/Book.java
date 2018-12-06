package com.cognizant.entity;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//bookId longtitle Stringprice doublevolume integer publishDate LocalDate
	private long bookId;
	private String title;
	private double price;
	private Integer volume;
	private Date publishDate;
	/**
	 * @return the bookId
	 */
	public long getBookId() {
		return bookId;
	}
	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the volume
	 */
	public Integer getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	/**
	 * @return the publishDate
	 */
	public Date getPublishDate() {
		return publishDate;
	}
	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Book Details\r\n\tbookId=" + bookId + "\r\n\ttitle=" + title + "\r\n\tprice=" + price + "\r\n\tvolume=" + volume
				+ "\r\n\tpublishDate=" + publishDate;
	}
}