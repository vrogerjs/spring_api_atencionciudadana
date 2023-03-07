package gob.regionancash.atencionciudadano.service;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.*;
import gob.regionancash.atencionciudadano.repository.AtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import javax.validation.Valid;
import java.util.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.util.DefaultUriBuilderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccessService {

    private static String BASE_URL="https://web.regionancash.gob.pe";

    public Map can(String[] perms, String token){

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        WebClient client = WebClient.builder()
            .uriBuilderFactory(factory)
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .build();

        Mono<String> response =client.post()
            .uri(uriBuilder -> uriBuilder
            .path("api/auth/can")
            .build())
            .headers(h -> h.setBearerAuth(token))
            .body(Mono.just(Arrays.asList(perms)), List.class)
            .retrieve()
            .onStatus( HttpStatus.INTERNAL_SERVER_ERROR::equals,res-> res.bodyToMono(String.class).map(RuntimeException::new)) 
            .onStatus(HttpStatus.BAD_REQUEST::equals,res -> res.bodyToMono(String.class).map(RuntimeException::new))
            .bodyToMono(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.readValue(response.block(), Map.class);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    public List<String> perms(String token){
        
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        WebClient client = WebClient.builder()
            .uriBuilderFactory(factory)
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .build();

        Mono<String> response =client.post()
            .uri(uriBuilder -> uriBuilder
            .path("api/auth/perms")
            .build())
            .headers(h -> h.setBearerAuth(token))
            .retrieve()
            .onStatus( HttpStatus.INTERNAL_SERVER_ERROR::equals,res-> res.bodyToMono(String.class).map(RuntimeException::new)) 
            .onStatus(HttpStatus.BAD_REQUEST::equals,res -> res.bodyToMono(String.class).map(RuntimeException::new))
            .bodyToMono(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try{
            return (List)mapper.readValue(response.block(), List.class);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }


}
