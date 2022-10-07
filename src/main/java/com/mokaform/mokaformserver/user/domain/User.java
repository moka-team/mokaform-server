package com.mokaform.mokaformserver.user.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String profile_image;

    @Column(name = "is_withdraw", nullable = false)
    private Boolean isWithdraw;

    @Column(name = "withdraw_at")
    private LocalDateTime withdrawAt;

    @Builder
    public User(String email, String password,
                String nickname, AgeGroup ageGroup,
                Gender gender, Job job,
                String profile_image, Boolean isWithdraw,
                LocalDateTime withdrawAt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.job = job;
        this.profile_image = profile_image;
        this.isWithdraw = isWithdraw;
        this.withdrawAt = withdrawAt;
    }
}
