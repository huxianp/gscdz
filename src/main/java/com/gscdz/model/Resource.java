package com.gscdz.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

@Entity
@Data
public class Resource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	private byte[] resourceBytes;
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "USER_RESOUCES", 
				joinColumns = @JoinColumn(name = "RESOUCES_ID"), 
				inverseJoinColumns = @JoinColumn(name = "USER_ID")
	)
	Set<CustomUser> users = new HashSet<CustomUser>();
}
