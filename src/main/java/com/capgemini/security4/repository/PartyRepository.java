package com.capgemini.security4.repository;

import com.capgemini.security4.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
    Optional<Party> findByPartyName(String partyName);
}
