package com.cognizant.entity;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;


public class Subject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long subjectId = 0;
	private String subTitle = "";
	private int durationInHours = 0;
	private Set<Book> reference = new LinkedHashSet<Book>();
	
	
	public Subject() {
		reference.add(new Book());
	}
	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}
	/**
	 * @param subTitle the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	/**
	 * @return the durationInHours
	 */
	public int getDurationInHours() {
		return durationInHours;
	}
	/**
	 * @param durationInHours the durationInHours to set
	 */
	public void setDurationInHours(int durationInHours) {
		this.durationInHours = durationInHours;
	}
	/**
	 * @return the reference
	 */
	public Set<Book> getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setReference(Set<Book> reference) {
		this.reference = reference;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Subject Details -> \r\n\tId = " + subjectId + "\r\n\t Title = " + subTitle + "\r\n\t Duration (InHours) = " + durationInHours
				+ "\r\n\t reference = " + reference;
	}
}