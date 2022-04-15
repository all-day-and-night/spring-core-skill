package me.security.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.security.entity.Authority;
import me.security.entity.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonProperty.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickName;

    private Set<AuthorityDto> authorityDtoSet;

    public static UserDto from(User user){
        if(user == null){
            return null;
        }

        return UserDto.builder()
                .username(user.getUsername())
                .nickName(user.getNickname())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
