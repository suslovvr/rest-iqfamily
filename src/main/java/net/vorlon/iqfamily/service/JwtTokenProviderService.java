package net.vorlon.iqfamily.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.dao.UserRepository;
import net.vorlon.iqfamily.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import net.vorlon.iqfamily.config.SpringSecurityConfig;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class JwtTokenProviderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpringSecurityConfig authModuleConfig;

//    @Autowired
//    private RequestInterceptor requestInterceptor;


    @Value("${token.secret}")
    private String jwtSecret;

    @Value("${token.expiration.minutes}")
    private Integer jwtExpiration;

    @Value("${token.prefix}")
    private String prefix;

    @Value("${token.audience}")
    private String audience;

    @Value("${token.type}")
    private String type;

    @Value("${token.issuer}")
    private String issuer;

    public String generateToken(UserDetails userDetails){
        log.info(userDetails.toString());
//        List<net.vorlon.iqfamily.model.domain.User> userList = userRepository.findByEmail(userDetails.getUsername());
        String encoded123=authModuleConfig.passwordEncoder().encode("12345678");

//        if (userList.size()==0) { throw new UsernameNotFoundException("Пользователь не найден"); }
        String encodedPswd=authModuleConfig.passwordEncoder().encode(userDetails.getPassword());
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        System.out.println(Date.from(now().plusMinutes(jwtExpiration).atZone(ZoneId.systemDefault()).toInstant()));

        return prefix + Jwts.builder()
                .setHeaderParam("typ",type)
                .setSubject(userDetails.getUsername())
                .setIssuer(issuer)
                .setAudience(audience)
                .setExpiration(Date.from(now().plusMinutes(jwtExpiration).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()),SignatureAlgorithm.HS512)
                .claim("rol",userDetails.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .compact();
    }

    public Claims getAll(String jwtToken){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims;
    }


    public String getUsernameFromJwtToken(String jwtToken){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims.getSubject();
    }
    public Integer getId(String jwtToken){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

        return (Integer)claims.get("id");
    }
    public String getOfficeBranch(String jwtToken){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

        return (String)claims.get("officeBranch");
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
        List<GrantedAuthority> roles = ((List<String>)claims.get("rol"))
                .stream().map(rol -> new SimpleGrantedAuthority(rol)).collect(Collectors.toList());
        return roles;
    }
    public String getAuthoritiesAsString(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();


        return (String)claims.get("rol");
    }

    public HashMap<String,String> getCatalogsAsHashMap(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();

        return (HashMap<String,String>)claims.get("cat");
    }



    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateTokenWithEmail(String email){

//            return prefix + Jwts.builder() // C 'Bearer '
            return "token=" +Jwts.builder()
                    .setHeaderParam("typ",type)
                    .setSubject(email)
                    .setIssuer(issuer)
                    .setAudience(audience)
                    .setExpiration(Date.from(now().plusMinutes(jwtExpiration).atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()),SignatureAlgorithm.HS512)
                    .compact();


    }
    public String getUsernameFromJwtTokenWithoutGetBytes(String jwtToken){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims.getSubject();
    }

}
