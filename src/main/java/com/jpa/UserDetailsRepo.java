package com.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails, Integer> {
	@Query(value = "SELECT * FROM incubedbnew.user_details WHERE user_name = :username", nativeQuery = true)
	public Optional<UserDetails> findByUsername(@Param("username") String username);

	@Query(value = "SELECT * FROM incubedbnew.user_details where user_grade= :userGrade", nativeQuery = true)
	public List<UserDetails> findByUserGrade(String userGrade);
}
