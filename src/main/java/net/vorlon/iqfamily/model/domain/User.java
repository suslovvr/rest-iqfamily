package net.vorlon.iqfamily.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import net.vorlon.iqfamily.model.domain.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
//@ToString
@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "name")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 500)
	private String name;
	@Column(name="DATE_OF_BIRTH")
	private LocalDate birthDate;

	private String password;

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="user_id")
	private Set<Email> emails = new HashSet<>();

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="user_id")
	private Set<Phone> phones = new HashSet<>();

	public User(String username, LocalDate birthDate, String password) {
		this.name = username;
		this.birthDate = birthDate;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public int hashCode() {
		return  id.intValue() * name.hashCode() * birthDate.hashCode();
	}
}
