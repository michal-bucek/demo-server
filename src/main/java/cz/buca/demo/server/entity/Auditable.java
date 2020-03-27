package cz.buca.demo.server.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedBy
	@Column(updatable = false)
	private String creator;

	@CreatedDate
	@Column(updatable = false)
	private Date created;

	@LastModifiedBy
	@Column(updatable = false)
	private String modifier;

	@LastModifiedDate
	@Column(updatable = false)
	private Date modified;
}