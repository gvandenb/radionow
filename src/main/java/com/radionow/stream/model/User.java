package com.radionow.stream.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "users_statistics", 
      joinColumns = 
        { @JoinColumn(name = "users_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "statistics_id", referencedColumnName = "id") })
	private Statistic statistic;
	
	@JsonIgnore
	@Column(name = "devices")
	private List<String> devices = new ArrayList<String>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userId", cascade = CascadeType.ALL)	
	private List<UserView> views = new ArrayList<UserView>();
	
	@Column(name = "searchTerms")
	private List<String> searchTerms = new ArrayList<String>();
	
	public User() {}

	public User(String deviceId, String firstName, String lastName, String email, String phoneNumber, Statistic statistic, List<String> devices) {
		super();
		this.deviceId = deviceId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.statistic = statistic;
		this.devices = devices;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getDevices() {
		return devices;
	}

	public void setDevices(List<String> devices) {
		this.devices = devices;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Statistic getStatistic() {
		return statistic;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}

	public List<UserView> getViews() {
		return views;
	}

	public void setViews(List<UserView> views) {
		this.views = views;
	}

	public List<String> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(List<String> searchTerms) {
		this.searchTerms = searchTerms;
	}
	
	
	
}
