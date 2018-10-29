package me.javaroad.mcloud.oauth.mapper;

import me.javaroad.mcloud.oauth.dto.response.ClientResponse;
import me.javaroad.mcloud.oauth.dto.request.CreateClientRequest;
import me.javaroad.mcloud.oauth.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author heyx
 */
@Mapper(componentModel = "spring", uses = {
    ResourceMapper.class,
    ApprovalMapper.class,
    ScopeMapper.class,
    AuthorityMapper.class
})
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientResponse mapEntityToResponse(Client client);

    Client mapRequestToEntity(CreateClientRequest clientRequest);
}
