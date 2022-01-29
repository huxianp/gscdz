package com.gscdz.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@NonNull
	private String username;
	private String nickName;
	@NonNull
	private String password;
	@NonNull
	private String role;
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name="Avatar", columnDefinition="longblob")
	private byte[] avatar;//头像

}
