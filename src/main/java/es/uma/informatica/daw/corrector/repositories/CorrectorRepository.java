package es.uma.informatica.daw.corrector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uma.informatica.daw.corrector.models.Corrector;

@Repository
public interface CorrectorRepository extends JpaRepository<Corrector, Long> {

}
