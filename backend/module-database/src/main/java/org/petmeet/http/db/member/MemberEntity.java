package org.petmeet.http.db.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.pets.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
@Table(name="member")
@Entity
public class MemberEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Email
	private String email;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String gender;

	@Column(columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false)
	@Embedded
	private Address address;

	@OneToOne(mappedBy = "member")
	private LoginEntity login;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pet> pet = new ArrayList<>();

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	public MemberEntity (Role role, LoginEntity login) {
		this.role = role;
		this.login = login;
	}

	public void setRole() {
		this.role = Role.USER;
	}

	public void onUpdate(String name, Address address, String email, String phoneNumber, String gender) {
		this.name = name;
		this.address = address;
		this.email = email;
		this.phoneNumber =phoneNumber;
		this.gender = gender;
	}

}
