package br.com.habit_tracker_back_springboot.module.habit.entity;

import br.com.habit_tracker_back_springboot.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "habits")
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "user_id")
    private UUID userId;

}
