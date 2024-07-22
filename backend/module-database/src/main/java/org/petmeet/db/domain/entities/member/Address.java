package org.petmeet.db.domain.entities.member;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Embeddable
public class Address {

	private String zipCode;
	private String address;
	private String detail_Address;

	public Address (String zipCode, String address, String detail_Address) {
		this.zipCode = zipCode;
		this.address = address;
		this.detail_Address = detail_Address;
	}

	public Address() {

	}

}
