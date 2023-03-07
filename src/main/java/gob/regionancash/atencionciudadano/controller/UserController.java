package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.User;
import gob.regionancash.atencionciudadano.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("token")
    public ResponseEntity getToken(@RequestBody String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity request = new HttpEntity(code);
        Map foo = restTemplate.postForObject("http://web.regionancash.gob.pe/api/auth/token", request, Map.class);
        return ResponseEntity.ok(foo);
    }


    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetalles)
            throws ResourceNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + userId));

        if (userDetalles.getNApellidoNombre() != null)
            user.setNApellidoNombre(userDetalles.getNApellidoNombre());

        if (userDetalles.getName() != null)
            user.setName(userDetalles.getName());

        if (userDetalles.getEmail() != null)
            user.setEmail(userDetalles.getEmail());

        user.setUpdatedAt(new Date());
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
