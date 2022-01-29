package com.gscdz.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class KnowledgePoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "longtext")
	private String materials;
	@ManyToOne(cascade = CascadeType.DETACH)
	private Chapter chapter;

	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(
			name = "PROBLEM_KNOWLEDGEPOINT",
			joinColumns = @JoinColumn(name="KNOWLEDGEPOINT_ID"),
			inverseJoinColumns =@JoinColumn(name="PROBLEM_ID")
			)
	private Set<Problem> RelevantPros=new HashSet<Problem>();
}

