package com.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "instructors")
public class Instructor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "instructor_id")
	private long instructorId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "email_address", nullable = false, unique = true, length = 100)
	private String emailAddress;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "subject_handling", columnDefinition = "TEXT")
	private String subjectHandling;

	public Instructor(long instructorId, String name, String emailAddress, String phoneNumber, String subjectHandling) {
		super();
		this.instructorId = instructorId;
		this.name = name;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.subjectHandling = subjectHandling;
	}

	public Instructor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(long instructorId) {
		this.instructorId = instructorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSubjectHandling() {
		return subjectHandling;
	}

	public void setSubjectHandling(String subjectHandling) {
		this.subjectHandling = subjectHandling;
	}

}