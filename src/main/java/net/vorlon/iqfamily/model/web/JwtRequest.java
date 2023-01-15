package net.vorlon.iqfamily.model.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class JwtRequest implements Serializable {
	private static final long serialVersionUID = 5926468583005150707L;
	@ApiModelProperty(example = "user")
	private String username;
	@ApiModelProperty(example = "password")
	private String password;
}