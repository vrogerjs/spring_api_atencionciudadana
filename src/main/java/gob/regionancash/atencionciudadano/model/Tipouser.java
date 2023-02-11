package gob.regionancash.atencionciudadano.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tipousers")
@EntityListeners(AuditingEntityListener.class)
public class Tipouser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 128)
    @Column(nullable = false, length = 128)
    private String tipo;

    @Column(nullable = false, length = 1)
    private Integer activo=1;

    @Column(nullable = false, length = 1)
    private Integer borrado=0;

    @JsonIgnore
    @OneToMany(mappedBy="tipouser")
    private Set<User> users;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
}
