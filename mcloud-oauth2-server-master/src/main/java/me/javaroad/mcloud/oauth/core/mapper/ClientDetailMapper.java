package me.javaroad.mcloud.oauth.core.mapper;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import me.javaroad.mcloud.oauth.dto.response.ApprovalResponse;
import me.javaroad.mcloud.oauth.dto.response.ClientResponse;
import me.javaroad.mcloud.oauth.dto.response.ScopeResponse;
import me.javaroad.mcloud.oauth.dto.response.AuthorityResponse;
import me.javaroad.mcloud.oauth.entity.GrantType;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * @author heyx
 */
@Mapper(componentModel = "spring")
public abstract class ClientDetailMapper {

    public static ClientDetailMapper INSTANCE = Mappers.getMapper(ClientDetailMapper.class);

    @Mappings({
        @Mapping(source = "grantTypes", target = "authorizedGrantTypes"),
        @Mapping(source = "accessTokenValidity", target = "accessTokenValiditySeconds"),
        @Mapping(source = "refreshTokenValidity", target = "refreshTokenValiditySeconds"),
        @Mapping(source = "autoApprove", target = "autoApproveScopes"),
        @Mapping(source = "redirectUri", target = "registeredRedirectUri")
    })
    public abstract BaseClientDetails mapClientToDetails(ClientResponse client);

    Set<String> mapScopeToStringSet(Set<ScopeResponse> scopes) {
        return Optional.ofNullable(scopes).orElse(Collections.emptySet()).stream()
            .map(ScopeResponse::getName)
            .collect(Collectors.toSet());
    }

    Set<String> mapApprovalToStringSet(Set<ApprovalResponse> approvals) {
        return Optional.ofNullable(approvals).orElse(Collections.emptySet()).stream()
            .map(ApprovalResponse::getName)
            .collect(Collectors.toSet());
    }

    List<GrantedAuthority> mapAuthorityToGrantedAuthority(Set<AuthorityResponse> authorities) {
        return Optional.ofNullable(authorities).orElse(Collections.emptySet()).stream().map(authority ->
            new SimpleGrantedAuthority(authority.getName())
        ).collect(Collectors.toList());
    }

    Set<String> mapGrantTypeToString(Set<GrantType> grantTypes) {
        return Optional.ofNullable(grantTypes).orElse(Collections.emptySet()).stream()
            .map(GrantType::getCode)
            .collect(Collectors.toSet());
    }

    Map<String, Object> mapAdditionalInformationToMap(String additionalInformation) {
        if (StringUtils.isNotBlank(additionalInformation)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(additionalInformation, Map.class);
            } catch (IOException e) {
                return Maps.newHashMap();
            }
        }
        return Maps.newHashMap();
    }

}
