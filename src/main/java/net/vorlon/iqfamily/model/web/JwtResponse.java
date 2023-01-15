package net.vorlon.iqfamily.model.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
}