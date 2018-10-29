package me.javaroad.mcloud.oauth.service;

import java.util.Objects;
import me.javaroad.common.exception.DataConflictException;
import me.javaroad.common.exception.DataNotFoundException;
import me.javaroad.mcloud.oauth.dto.request.DeveloperInfoSearchRequest;
import me.javaroad.mcloud.oauth.dto.response.DeveloperInfoResponse;
import me.javaroad.mcloud.oauth.entity.DeveloperInfo;
import me.javaroad.mcloud.oauth.entity.Status;
import me.javaroad.mcloud.oauth.entity.User;
import me.javaroad.mcloud.oauth.dto.request.DeveloperInfoRequest;
import me.javaroad.mcloud.oauth.mapper.DeveloperInfoMapper;
import me.javaroad.mcloud.oauth.repository.DeveloperInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DeveloperInfoService {

    private final DeveloperInfoRepository developerInfoRepository;
    private final DeveloperInfoMapper developerInfoMapper;

    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public DeveloperInfoService(DeveloperInfoRepository developerInfoRepository,
        DeveloperInfoMapper developerInfoMapper, UserService userService, ClientService clientService) {
        this.developerInfoRepository = developerInfoRepository;
        this.developerInfoMapper = developerInfoMapper;
        this.userService = userService;
        this.clientService = clientService;
    }

    public DeveloperInfoResponse getResponse(String username) {
        return developerInfoMapper.mapEntityToResponse(getEntityByUsername(username));
    }

    public DeveloperInfoResponse getResponse(Long developerInfoId) {
        return developerInfoMapper.mapEntityToResponse(getEntity(developerInfoId));
    }

    private DeveloperInfo getEntityByUsername(String username) {
        return developerInfoRepository.findByUserUsername(username);
    }

    @Transactional
    public DeveloperInfoResponse create(String username, DeveloperInfoRequest developerInfoRequest) {
        DeveloperInfo developerInfo = developerInfoRepository.findByUserUsername(username);
        if (Objects.nonNull(developerInfo)) {
            throw new DataConflictException("User information has already existed");
        }
        developerInfo = developerInfoMapper.mapRequestToEntity(developerInfoRequest);
        User user = userService.getEntity(username);
        developerInfo.setUser(user);
        developerInfo.setStatus(Status.PENDING);
        developerInfo = developerInfoRepository.save(developerInfo);
        return developerInfoMapper.mapEntityToResponse(developerInfo);
    }

    @Transactional
    public DeveloperInfoResponse modify(Long developerInfoId, DeveloperInfoRequest developerInfoRequest) {
        DeveloperInfo developerInfo = getNotNullEntity(developerInfoId);
        developerInfoMapper.updateEntityFromRequest(developerInfoRequest, developerInfo);
        developerInfo = developerInfoRepository.save(developerInfo);
        return developerInfoMapper.mapEntityToResponse(developerInfo);
    }

    @Transactional
    public void delete(Long developerInfoId) {
        DeveloperInfo developerInfo = getNotNullEntity(developerInfoId);
        developerInfo.setStatus(Status.DISABLED);
        developerInfoRepository.save(developerInfo);
    }

    private DeveloperInfo getEntity(Long developerInfoId) {
        return developerInfoRepository.findOne(developerInfoId);
    }

    private DeveloperInfo getNotNullEntity(Long developerInfoId) {
        DeveloperInfo developerInfo = getEntity(developerInfoId);
        if (Objects.isNull(developerInfo)) {
            throw new DataNotFoundException("developerInfo[id=%s] not found", developerInfoId);
        }
        return developerInfo;
    }

    public Page<DeveloperInfoResponse> search(DeveloperInfoSearchRequest searchRequest, Pageable pageable) {
        Page<DeveloperInfo> developerInfoPage;
        if (Objects.nonNull(searchRequest) && Objects.nonNull(searchRequest.getStatus())) {
            developerInfoPage = developerInfoRepository.findByStatus(searchRequest.getStatus(), pageable);
        } else {
            developerInfoPage = developerInfoRepository.findAll(pageable);
        }
        return developerInfoPage.map(developerInfoMapper::mapEntityToResponse);
    }

    @Transactional
    public void review(Long developerInfoId, Status status) {
        DeveloperInfo developerInfo = getEntity(developerInfoId);
        if (Objects.isNull(developerInfo)) {
            throw new DataNotFoundException("DeveloperInfo[id=%s] not found", developerInfoId);
        }
        if (!Status.PENDING.equals(developerInfo.getStatus())
            || !(Status.DENY.equals(status) || Status.NORMAL.equals(status))) {
            throw new IllegalStateException("invalid status: " + developerInfo.getStatus());
        }
        developerInfo.setStatus(status);
        developerInfoRepository.save(developerInfo);
    }
}
