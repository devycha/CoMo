package com.dongjji.como.user.service.Impl;

import com.dongjji.como.common.mail.GoogleMailSender;
import com.dongjji.como.user.auth.PrincipalDetails;
import com.dongjji.como.user.constant.SecurityConstants;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.exception.EmailAuthKeyExpiredException;
import com.dongjji.como.user.exception.UserAlreadyExistException;
import com.dongjji.como.user.type.Gender;
import com.dongjji.como.user.entity.User;
import com.dongjji.como.user.type.UserRole;
import com.dongjji.como.user.type.UserStatus;
import com.dongjji.como.user.exception.LoginFailException;
import com.dongjji.como.user.repository.UserRepository;
import com.dongjji.como.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GoogleMailSender googleMailSender;
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
        String email = registerUserDto.getUsername();
        Optional<User> findUser = userRepository.findByEmail(email);

        if (findUser.isPresent()) {
            throw new UserAlreadyExistException("이미 존재하는 계정입니다.");
        }

        String uuid = UUID.randomUUID().toString();

        googleMailSender.sendEmailAuthMail(email, uuid);
        userRepository.save(
                User.builder()
                        .email(email)
                        .password(BCrypt.hashpw(registerUserDto.getPassword(), BCrypt.gensalt()))
                        .birth(registerUserDto.getBirth())
                        .gender(Gender.valueOf(registerUserDto.getGender()))
                        .emailAuth(false)
                        .emailAuthKey(uuid)
                        .role(UserRole.USER)
                        .status(UserStatus.NEED_EMAIL_AUTH)
                        .emailAuthValidationDt(LocalDateTime.now().plusDays(1))
                        .build()
        );
    }

    @Override
    public boolean authorizeEmail(String authKey) {
        Optional<User> findUser = userRepository.findByEmailAuthKey(authKey);
        if (!findUser.isPresent()) {
            return false;
        }

        User user = findUser.get();

        if (!LocalDateTime.now().isBefore(user.getEmailAuthValidationDt())) {
            throw new EmailAuthKeyExpiredException("기간이 만료된 인증키입니다.");
        }

        user.setEmailAuth(true);
        user.setEmailAuthKey(null);
        user.setEmailAuthValidationDt(null);
        user.setStatus(UserStatus.AVAILABLE);

        userRepository.save(user);
        return true;
    }
    
    @ExceptionHandler(EmailAuthKeyExpiredException.class)
    private ResponseEntity<?> emailAuthKeyExpiredExceptionHandler(EmailAuthKeyExpiredException e) {
        // TODO: 이메일 인증키 만료시 다시 발급받을 수 있는 링크를 제공해주는 로직 작성
        return null;
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
