package dev.shulika.xtelworkbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "tg_visitors")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Visitor {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    @CreationTimestamp
    private Timestamp registeredAt;
}
