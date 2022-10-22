package com.dongjji.como.user.service.Impl;

import com.dongjji.como.user.auth.PrincipalDetails;
import com.dongjji.como.user.constant.SecurityConstants;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.type.Gender;
import com.dongjji.como.user.entity.User;
import com.dongjji.como.user.type.UserRole;
import com.dongjji.como.user.type.UserStatus;
import com.dongjji.como.user.exception.LoginFailException;
import com.dongjji.como.user.repository.UserRepository;
import com.dongjji.como.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityConstants securityConstants;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUser = userRepository.findByEmail(username);
        if (!findUser.isPresent()) {
            throw new UsernameNotFoundException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        User user = findUser.get();
        if (user.getStatus().equals(UserStatus.DROP)
            || user.getStatus().equals(UserStatus.BAN)) {
            throw new LoginFailException("사용할 수 없는 계정입니다.");
        }

        if (user.getStatus().equals(UserStatus.NEED_EMAIL_AUTH)) {
            throw new LoginFailException("이메일 인증 후 사용해주세요.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (user.getRole().equals(UserRole.ADMIN)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new PrincipalDetails(user);
    }

    @Override
    public void register(RegisterUserDto registerUserDto) {
        System.out.println(registerUserDto.toString());
        userRepository.save(
                User.builder()
                        .email(registerUserDto.getUsername())
                        .password(BCrypt.hashpw(registerUserDto.getPassword(), BCrypt.gensalt()))
                        .birth(registerUserDto.getBirth())
                        .gender(Gender.valueOf(registerUserDto.getGender()))
                        .emailAuth(false)
                        .role(UserRole.USER)
                        .status(UserStatus.NEED_EMAIL_AUTH)
                        .build()
        );
    }

//    @Override
//    public ResponseEntity<String> login(LoginUserDto loginUserDto) {
//        Optional<User> findUser = userRepository.findByEmail(loginUserDto.getUsername());
//        if (!findUser.isPresent()) {
//            throw new UsernameNotFoundException("아이디 혹은 비밀번호가 일치하지 않습니다.");
//        }
//
//        User user = findUser.get();
//        if (user.getStatus().equals(UserStatus.DROP)
//                || user.getStatus().equals(UserStatus.BAN)) {
//            throw new LoginFailException("사용할 수 없는 계정입니다.");
//        }
//
//        if (user.getStatus().equals(UserStatus.NEED_EMAIL_AUTH)) {
//            throw new LoginFailException("이메일 인증 후 사용해주세요.");
//        }
//
//        if (!BCrypt.checkpw(loginUserDto.getPassword(), user.getPassword())) {
//            throw new LoginFailException("아이디 혹은 비밀번호가 일치하지 않습니다.");
//        }
//
//        String token = generateToken(loginUserDto.getUsername(), user.getRole().toString());
//
//        System.out.println(token);
//        return new ResponseEntity<String>(token, HttpStatus.OK);
//    }

//    private String generateToken(String username, String role) {
//        byte[] signingKey = securityConstants.getJWT_SECRET().getBytes();
//
//        return Jwts.builder()
//                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
//                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
//                .setExpiration(new Date(System.currentTimeMillis() + 864))
//                .claim("uid", username)
//                .claim("rol", role)
//                .compact();
//
//    }
}
