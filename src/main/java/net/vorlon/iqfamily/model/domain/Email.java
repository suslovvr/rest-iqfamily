package net.vorlon.iqfamily.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
//@ToString(callSuper = true)
@Entity
@Table(	name = "EMAIL_DATA")
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	@NotBlank
	@Size(max = 500)
	private String email;

	public Email( String email){
		this.email=email;
	}
	public Email(User user, String email){
		this.user = user;
		this.email=email;
	}
	@Override
	public int hashCode() {
		return email.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		return Objects.equals(email, other.getEmail());
	}
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder();
		buf.append("email={")
				.append("id=").append(id).append(",")
				.append("user_id=").append(user.getId()).append(",")
				.append("email=").append(email)
				.append("}");
		return buf.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
