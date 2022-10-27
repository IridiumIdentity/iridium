package software.iridium.api.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class UuidIdentifiableAndAuditable extends Auditable {

	private static final long serialVersionUID = 6767432032732072868L;
	@Id
	@GeneratedValue(generator="UUID")
	@GenericGenerator(name ="UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name="id", length=36, nullable=false)
	private String id;

	@Version
	@Column(name="version", nullable=false, columnDefinition="BIGINT(20) NOT NULL DEFAULT 0")
	private Long version = 0L;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
