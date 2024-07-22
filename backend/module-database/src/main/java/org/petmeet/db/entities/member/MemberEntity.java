package org.petmeet.db.entities.member;

import java.time.LocalDateTime;

import org.petmeet.db.entities.login.LoginEntity;
import org.petmeet.db.enums.Role;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@Column(name="member_id")
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

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	public void setRole() {
		this.role = Role.USER;
	}


}
