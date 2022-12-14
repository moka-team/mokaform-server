package com.mokaform.mokaformserver.user.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.UserErrorCode;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 320)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(name = "age_group", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(name = "gender", nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "job", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private Job job;

    @Column(name = "profile_image", length = 300)
    private String profileImage;

    @Column(name = "is_withdraw", nullable = false)
    private Boolean isWithdraw;

    @Column(name = "withdraw_at")
    private LocalDateTime withdrawAt;

    @ManyToMany(fetch = FetchType.EAGER)        // TODO: EAGER ?????? ?????? ?????? ??????
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @Builder
    public User(String email, String password,
                String nickname, AgeGroup ageGroup,
                Gender gender, Job job,
                String profileImage, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.job = job;
        this.profileImage = profileImage;
        this.isWithdraw = false;
        this.roles = roles;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, getPassword()))
            throw new ApiException(UserErrorCode.INVALID_ACCOUNT_REQUEST);
    }

    public List<GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(toList());
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void withdraw() {
        this.isWithdraw = true;
        this.withdrawAt = LocalDateTime.now();
    }
}
