package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="members")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Member {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Name is required")
    @Column(nullable=false, length=100)
    private String name;

    @NotBlank @Email
    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(length=15)
    private String phone;

    @Column(columnDefinition="TEXT")
    private String address;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name="membership_type", nullable=false)
    private MembershipType membershipType = MembershipType.Student;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.Active;

    @Column(name="joined_date", nullable=false)
    private LocalDate joinedDate;

    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public enum MembershipType { Student, Faculty, Public }
    public enum Status         { Active, Inactive, Suspended }

    @PrePersist  protected void onCreate() { if(joinedDate==null) joinedDate=LocalDate.now(); createdAt=updatedAt=LocalDateTime.now(); }
    @PreUpdate   protected void onUpdate() { updatedAt=LocalDateTime.now(); }
}
