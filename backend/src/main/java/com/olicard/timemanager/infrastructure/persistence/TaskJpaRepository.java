package com.olicard.timemanager.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
}
