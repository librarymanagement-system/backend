package com.lib.libmansys.entity;

import com.lib.libmansys.entity.Enum.LoanPeriodStatus;
import com.lib.libmansys.entity.Enum.MembershipStatus;
import com.lib.libmansys.entity.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // ADMIN, MEMBER

    @OneToMany(mappedBy = "user")
    private List<Loan> loans = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    @Enumerated(EnumType.STRING)
    private LoanPeriodStatus loanPeriodStatus = LoanPeriodStatus.NORMAL;

}
