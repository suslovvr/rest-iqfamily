package net.vorlon.iqfamily.api;

import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.model.web.AuthRequest;
import net.vorlon.iqfamily.model.web.AuthResponse;
import net.vorlon.iqfamily.service.JwtTokenProviderService;
import net.vorlon.iqfamily.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/login")
@Slf4j
public class LoginController {

    @Autowired
    private JwtTokenProviderService providerService;

    @Autowired
    private UserService userDetailsService;

    @PostMapping
    public AuthResponse createJwt(@RequestBody AuthRequest authRequest, HttpServletResponse httpServletResponse){
        UserDetails userDetails =
                userDetailsService.loadUserByUsernameAndPassword(authRequest.getLogin(),authRequest.getPassword());
        String jwt = providerService.generateToken(userDetails);
        String jwtToken = jwt.substring(7);
        httpServletResponse.setHeader("Authorization",jwtToken);
        return new AuthResponse(jwt);
    }
}
