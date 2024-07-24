package org.petmeet.http.db.login;

import java.time.LocalDateTime;

import org.petmeet.http.db.member.MemberEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "login")
@Entity
public class LoginEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private MemberEntity member;

	@CreatedDate
	private LocalDateTime lastLoginTime;

	@LastModifiedDate
	private LocalDateTime lastUpdateTime;

	@Builder
	public LoginEntity (Long id, String username, String password, MemberEntity member, LocalDateTime lastLoginTime,
		LocalDateTime lastUpdateTime) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.member = member;
		this.lastLoginTime = lastLoginTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public LoginEntity (String username, String password) {
		this.username = username;
		this.password = password;
	}

	public LoginEntity () {
	}

	public void associateMember(MemberEntity member) {
		this.member = member;
		if(member == null) {
			throw new RuntimeException("Member is Null");
		}
	}

	@PrePersist
	protected void onCreate() {
		lastLoginTime = LocalDateTime.now();
	}

}
