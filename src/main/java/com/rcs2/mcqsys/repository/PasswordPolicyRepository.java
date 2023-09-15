package com.rcs2.mcqsys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rcs2.mcqsys.model.PasswordPolicy;

public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Long>{

}
