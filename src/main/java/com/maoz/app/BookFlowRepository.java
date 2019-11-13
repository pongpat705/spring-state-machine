package com.maoz.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFlowRepository extends JpaRepository<BookFlow, Long> {
}
