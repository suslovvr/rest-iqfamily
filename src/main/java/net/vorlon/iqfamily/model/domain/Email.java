package net.vorlon.iqfamily.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
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
		return   id.intValue() * email.hashCode() ;
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
}
