package com.gscdz.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "solvedPro_id")
@EqualsAndHashCode(callSuper = true,of = "id")
public class SolvedProblem extends Problem{

	private Integer solvedTimes;
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE},mappedBy = "solvedProblem")
	private List<Tips> tips=new ArrayList<Tips>();

	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(name = "ctjTable",
			joinColumns = @JoinColumn(name="solvedPro_ID"),
			inverseJoinColumns =@JoinColumn(name="CustomUser_ID")
	)
	private Set<CustomUser> customUserSet=new HashSet<>();

	@Builder
	public SolvedProblem(Long id, String subject, String analysis, Boolean isSolved, Integer visitTimes,
			List<Answer> answers, Set<KnowledgePoint> RelevantKps, Integer solvedTimes, List<Tips> tips,Set<CustomUser> customUserSet) {
		super(id, subject, analysis,visitTimes, answers, RelevantKps);
		this.solvedTimes=solvedTimes;
		this.tips = tips;
		this.customUserSet=customUserSet;
	}
}
