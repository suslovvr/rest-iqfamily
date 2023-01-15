package net.vorlon.iqfamily.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
//@ToString(callSuper = true)
@Entity
@Table(	name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name="user_id", nullable=false)
	private Long userId;

	@NotBlank
	private BigDecimal balance;

	public Account(Long userId, BigDecimal balance){
		this.userId = userId;
		this.balance=balance;
	}
	@Override
	public int hashCode() {
		return  id.hashCode();
	}
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder();
		buf.append("account={")
				.append("id=").append(id).append(",")
				.append("user_id=").append(getUserId()).append(",")
				.append("balance=").append(balance.toPlainString())
				.append("}");
		return buf.toString();
	}
}
