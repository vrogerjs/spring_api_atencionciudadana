package gob.regionancash.atencionciudadano.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "personas")
@EntityListeners(AuditingEntityListener.class)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String tipoPersona;

    @Size(max = 64)
    @Column(nullable = false, length = 64)
    private String tipoDocumento;

    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String nroDocumento;

    @Size(max = 256)
    @Column(nullable = true, length = 200)
    private String apellidoNombre;

    @Size(max = 512)
    @Column(nullable = true, length = 512)
    private String razonSocial;

    @Size(max = 512)
    @Column(nullable = true, length = 512)
    private String representanteLegal;

    @Size(max = 512)
    @Column(nullable = true, length = 512)
    private String direccion;

    @Size(max = 512)
    @Column(nullable = true, length = 512)
    private String email;

    @Size(max = 512)
    @Column(nullable = true, length = 16)
    private String celular;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer activo=1;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer borrado=0;

    @JsonIgnore
    @OneToMany(mappedBy="persona")
    private Set<Atencion> atencions;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date updatedAt;


}
