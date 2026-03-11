package br.com.habit_tracker_back_springboot.module.habit.repository;

import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HabitRepository extends JpaRepository<HabitEntity, UUID> {
}
