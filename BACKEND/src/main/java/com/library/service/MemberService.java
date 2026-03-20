package com.library.service;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repo;

    public List<Member> getAll()                       { return repo.findAll(); }
    public List<Member> search(String kw)              { return repo.search(kw); }

    public Member getById(@NonNull Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
    }

    public Member create(Member m) {
        if (repo.existsByEmail(m.getEmail()))
            throw new RuntimeException("Email already exists: " + m.getEmail());
        return repo.save(m);
    }

    public Member update(@NonNull Long id, Member u) {
        Member m = getById(id);
        m.setName(u.getName());
        m.setEmail(u.getEmail());
        m.setPhone(u.getPhone());
        m.setAddress(u.getAddress());
        m.setMembershipType(u.getMembershipType());
        m.setStatus(u.getStatus());
        m.setJoinedDate(u.getJoinedDate());
        return repo.save(m);
    }

    public void delete(@NonNull Long id) {
        repo.deleteById(Objects.requireNonNull(id, "ID must not be null"));
    }
}
