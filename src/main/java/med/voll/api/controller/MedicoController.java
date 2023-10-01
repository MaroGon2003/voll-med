package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired //no se deria usar
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder){
       Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
       DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(),  medico.getEmail(), medico.getTelefono(), medico.getDocumento(),  medico.getEspecialidad().toString(), new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));

        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();  //URL donde encontrar el medico
        return ResponseEntity.created(url).body(datosRespuestaMedico);  //return 201 Created
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoMedicos>> listadoMedico(@PageableDefault(size = 10) Pageable paginacion){
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedicos::new);   //listar todos los medicos
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedicos::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaMedico> actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(),  medico.getEmail(), medico.getTelefono(), medico.getDocumento(),  medico.getEspecialidad().toString(), new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento())));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornarDatoMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(),  medico.getEmail(), medico.getTelefono(), medico.getDocumento(),  medico.getEspecialidad().toString(), new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }

    /* @DeleteMapping("/{id}")                                    //Metodo para eliminar de la base de datos
    public void eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }*/

}
