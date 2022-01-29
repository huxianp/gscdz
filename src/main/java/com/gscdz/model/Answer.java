package com.gscdz.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Builder
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "longtext")
	private String content;
	@Column(columnDefinition = "longtext")
	private String analysis;
	private Integer pros;
	private Integer cons;

	@Column(columnDefinition = "TIMESTAMP")
	private Date createDate;

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private Problem problem;
	
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private CustomUser user;
	
	
}
