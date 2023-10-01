package med.voll.api.domain.paciente;

import med.voll.api.domain.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}