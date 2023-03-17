package gob.regionancash.atencionciudadano.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "dependencias")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Dependencia {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256, unique = true)
    private String name;

    @Column(length = 32)
    private String abreviatura;

    @Column(length = 256)
    private String apellidoNombreResponsable;

    @Column(length = 256)
    private String cargoResponsable;

    @Column(length = 256)
    private String emailResponsable;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer activo=1;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer borrado=0;

    @JsonIgnore
    @OneToMany(mappedBy="dependencia")
    private Set<Cronograma> cronogramas;

    @JsonIgnore
    @OneToMany(mappedBy="dependencia")
    private Set<Atencion> atencions;

    @JsonIgnore
    @OneToMany(mappedBy="dependencia")
    private Set<User> users;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date updatedAt;

}
