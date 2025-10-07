package uade.TPO.react.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uade.TPO.react.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}