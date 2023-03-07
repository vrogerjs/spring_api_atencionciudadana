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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.util.DefaultUriBuilderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ContractService {

    public List getContracts(Integer directory){
        String BASE_URL="https://web.regionancash.gob.pe";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        WebClient client = WebClient.builder()
            //.clientConnector(new ReactorClientHttpConnector(httpClient))
            .uriBuilderFactory(factory)
            .baseUrl(BASE_URL)
            //.defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            //.defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();
            /* Mono<Object[]> response = webClient.get()
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class).log();*/


        Mono<String> response =client.get()
            //.uri("/admin/rh/contract/0/10?idDir="+directory)
            .uri(uriBuilder -> uriBuilder
            .path("admin/rh/api/contract/0/10")
            .queryParam("people",directory)
            .queryParam("active",1)
            .build())
            .retrieve()
            .onStatus( HttpStatus.INTERNAL_SERVER_ERROR::equals,res-> res.bodyToMono(String.class).map(RuntimeException::new)) 
            .onStatus(HttpStatus.BAD_REQUEST::equals,res -> res.bodyToMono(String.class).map(RuntimeException::new))
            ///.doOnSuccess(RuntimeException::new)
            /* .bodyToMono(String.class)
            .block();*/

            // .block()
            .bodyToMono(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try{
            return (List)mapper.readValue(response.block(), Map.class).get("data");
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

}
