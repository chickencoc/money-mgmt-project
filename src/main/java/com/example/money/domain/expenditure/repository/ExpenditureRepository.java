package com.example.money.domain.expenditure.repository;

import com.example.money.domain.expenditure.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
}
