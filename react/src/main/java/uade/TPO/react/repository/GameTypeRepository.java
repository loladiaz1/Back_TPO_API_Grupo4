package uade.TPO.react.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uade.TPO.react.entity.GameType;

@Repository
public interface GameTypeRepository extends JpaRepository<GameType, Long> {
}
