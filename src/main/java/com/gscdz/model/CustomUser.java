package com.gscdz.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "CustomUser_id")
@Table(name = "CustomUser")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true,of = "id")
public class CustomUser extends User{

	private Integer solvedProblemNum;
	
	@OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "user",fetch = FetchType.EAGER)
	private List<Answer> answers=new ArrayList<Answer>();
	
	@OneToMany(cascade = { CascadeType.REMOVE,CascadeType.MERGE},fetch = FetchType.EAGER,mappedBy = "customUser")
	private Set<UnsolvedProblem> unsolvedProblems=new HashSet<UnsolvedProblem>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_RESOUCES",
				joinColumns = @JoinColumn(name="CustomUser_ID"),
				inverseJoinColumns =@JoinColumn(name="RESOUCES_ID")
	)
	private List<Resource> resoucesList=new ArrayList<Resource>();

	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(name = "ctjTable",
			joinColumns = @JoinColumn(name="CustomUser_ID"),
			inverseJoinColumns =@JoinColumn(name="solvedPro_ID")
	)
	private Set<SolvedProblem> ctjSet=new HashSet<>();
	
	@Builder
	public CustomUser(Long userId, @NonNull String username, String nickName, @NonNull String password,
			@NonNull String role, byte[] avatar, Integer solvedProblemNum, List<Answer> answers,
			Set<UnsolvedProblem> unsolvedProblems, List<Resource> resoucesList,Set<SolvedProblem> ctjSet) {
		super(userId, username, nickName, password, role, avatar);
		this.answers = answers;
		this.solvedProblemNum=solvedProblemNum;
		this.unsolvedProblems = unsolvedProblems;
		this.resoucesList = resoucesList;
		this.ctjSet=ctjSet;
	}

	
	
}
