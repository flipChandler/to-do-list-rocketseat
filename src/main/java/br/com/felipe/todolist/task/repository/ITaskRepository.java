package br.com.felipe.todolist.task.repository;

import br.com.felipe.todolist.task.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

    List<TaskModel> findAllByUserId(UUID userId);

    Optional<TaskModel> findByIdAndUserId(UUID id, UUID userId);
}
