package com.gscdz.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "unsolvedPro_id")
@EqualsAndHashCode(callSuper = true,of = "id")
public class UnsolvedProblem extends Problem{

	private Integer answerNum;
	
	@Column(columnDefinition = "TIMESTAMP")
	private Date createDate;
	
	@Column(columnDefinition = "TIMESTAMP")
	private Date firstSolvedDate;

	@ManyToOne(cascade = CascadeType.DETACH)
	private CustomUser customUser;

	@Builder
	public UnsolvedProblem(Long id, String subject, String analysis, Boolean isSolved, Integer visitTimes,
			List<Answer> answers, Set<KnowledgePoint> RelevantKps, Integer answerNum, Date createDate,
			Date firstSovledDate, CustomUser customUser) {
		super(id, subject, analysis,visitTimes, answers, RelevantKps);
		this.answerNum = answerNum;
		this.createDate = createDate;
		this.firstSolvedDate = firstSovledDate;
		this.customUser = customUser;
	}

	
	
	
}
