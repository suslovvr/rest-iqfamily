package net.vorlon.iqfamily.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(	name = "PHONE_DATA",
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "phone")
		})
public class Phone {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	@NotBlank
	@Size(max = 13)
	private String phone;

	public Phone(User user, String phone){
		this.user = user;
		this.phone = phone;
	}
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder();
		buf.append("phone={")
				.append("id=").append(id).append(",")
				.append("user_id=").append(user.getId()).append(",")
				.append("phone=").append(phone)
				.append("}");
		return buf.toString();
	}
}
