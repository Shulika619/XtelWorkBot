package dev.shulika.xtelworkbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "app_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    private Long id;
    private String fullName;
    private Integer idDepartment;
    private String tgFirstName;
    private String tgLastName;
    private String tgUserName;
    private String tgPhone;
    @Enumerated(EnumType.STRING)
    private RegStatus regStatus;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    private boolean isRegistered;
}
