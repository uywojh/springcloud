package me.javaroad.mcloud.oauth.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.javaroad.data.entity.TemporalEntity;

/**
 * @author heyx
 */
@NamedEntityGraph(name = "all", attributeNodes = {
    @NamedAttributeNode("authorities")
})
@Getter
@Setter
@Entity
@Table(name = "oauth_user")
public class User extends TemporalEntity {
    @Column(unique = true)
    private String username;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String email;
    private String phone;
    private String password;
    private String nickName;
    private String avatar;

    @ManyToMany
    @JoinTable(name = "user_authority",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    public enum UserType {
        USER, DEVELOPER, ADMIN
    }
}
