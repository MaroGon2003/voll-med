package med.voll.api.domain.paciente;

import med.voll.api.domain.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("""
    select p.activo from Paciente p
    where p.id =:idPaciente
            """)
    Boolean findActivoById(Long idPaciente);
}