package org.choongang.member.service;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.constants.MemberType;
import org.choongang.member.controller.JoinValidator;
import org.choongang.member.controller.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;


@Service
@RequiredArgsConstructor
public class MemberJoinService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JoinValidator validator ;    // 회원가입 유효성 검사

    public void save(RequestJoin form, Errors errors) {
        validator.validate(form, errors);  // 유효성 검사 실행
        if (errors.hasErrors()) {
            return;  // 유효성 검사 통과 X ==> 메서드 즉시 종료
        }

        save(form);
    }

    public void save(RequestJoin form) {
        String password = passwordEncoder.encode(form.password());
        Member member = Member.builder()
                .name(form.name())
                .email(form.email())
                .password(password)
                .mobile(form.mobile())
                .type(MemberType.USER)
                .build();
        save(member);
    }

    public void save(Member member) {

        repository.saveAndFlush(member);
    }
}
