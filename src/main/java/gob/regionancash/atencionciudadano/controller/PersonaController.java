package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.Persona;
import gob.regionancash.atencionciudadano.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/persona")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("")
    public List<Persona> getAllPersonas() throws Exception {
        return personaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonasById(@PathVariable(value = "id") Long personaId) throws ResourceNotFoundException {
        Persona persona = personaRepository.findById(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada :" + personaId));
        return ResponseEntity.ok().body(persona);
    }

    @GetMapping("/nrodoc/{id}")
    public ResponseEntity<Persona> getPersonasByNroDocumento(@PathVariable(value = "id") String personaId) throws ResourceNotFoundException {
        Persona persona = personaRepository.findByNroDocumento(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada :" + personaId));
        return ResponseEntity.ok().body(persona);
    }

    @PostMapping("")
    public Persona createPersona(@Valid @RequestBody Persona persona) {
        return personaRepository.save(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable(value = "id") Long personaId, @Valid @RequestBody Persona personaData) throws ResourceNotFoundException {

        Persona persona = personaRepository.findById(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada: " + personaId));

        if (personaData.getTipoPersona() != null)
            persona.setTipoPersona(personaData.getTipoPersona());

        if (personaData.getTipoDocumento() != null)
            persona.setTipoDocumento(personaData.getTipoDocumento());

        if (personaData.getNroDocumento() != null)
            persona.setNroDocumento(personaData.getNroDocumento());

        if (personaData.getApellidoNombre() != null)
            persona.setApellidoNombre(personaData.getApellidoNombre());

        if (personaData.getRazonSocial() != null)
            persona.setRazonSocial(personaData.getRazonSocial());

        if (personaData.getRepresentanteLegal() != null)
            persona.setRepresentanteLegal(personaData.getRepresentanteLegal());

        if (personaData.getDireccion() != null)
            persona.setDireccion(personaData.getDireccion());

        if (personaData.getCelular() != null)
            persona.setCelular(personaData.getCelular());

        persona.setUpdatedAt(new Date());
        Persona updatedPersona = personaRepository.save(persona);
        return ResponseEntity.ok(updatedPersona);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deletePersona(@PathVariable(value = "id") Long personaId) throws Exception {
        Persona persona = personaRepository.findById(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada: " + personaId));

        personaRepository.delete(persona);
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", Boolean.TRUE);
        return response;
    }
}
