package com.gscdz.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "longtext")
	private String subject;
	@Column(columnDefinition = "longtext")
	private String analysis;
	
	private Integer visitTimes;//访问次数
	@OneToMany(cascade = { CascadeType.REMOVE, CascadeType.PERSIST},mappedBy = "problem",fetch = FetchType.EAGER)
	private List<Answer> answers=new ArrayList<Answer>();
	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(
			name = "PROBLEM_KNOWLEDGEPOINT",
			joinColumns = @JoinColumn(name="PROBLEM_ID"),
			inverseJoinColumns =@JoinColumn(name="KNOWLEDGEPOINT_ID")
			)
	private Set<KnowledgePoint> RelevantKps=new HashSet<KnowledgePoint>();
	
}
