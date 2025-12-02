package com.reverso.service.impl;

import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;
import com.reverso.mapper.ResourceDownloadMapper;
import com.reverso.model.Resource;
import com.reverso.model.ResourceDownload;
import com.reverso.model.User;
import com.reverso.repository.ResourceDownloadRepository;
import com.reverso.repository.ResourceRepository;
import com.reverso.repository.UserRepository;
import com.reverso.service.interfaces.ResourceDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceDownloadServiceImpl implements ResourceDownloadService {

    private final ResourceDownloadRepository downloadRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceDownloadMapper mapper;

    @Override
    public ResourceDownloadResponse createDownload(ResourceDownloadRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + request.getResourceId()));

        boolean alreadyDownloaded = downloadRepository.existsByUserIdAndResourceId(
                request.getUserId(), request.getResourceId());
        
        if (alreadyDownloaded) {
            throw new RuntimeException("User has already downloaded this resource");
        }

        ResourceDownload download = ResourceDownload.builder()
                .user(user)
                .resource(resource)
                .build();

        ResourceDownload savedDownload = downloadRepository.save(download);
        return mapper.toResponse(savedDownload);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceDownloadResponse> getAllDownloads() {
        return downloadRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceDownloadResponse> getDownloadsByUser(UUID userId) {
        return downloadRepository.findByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceDownloadResponse> getDownloadsByResource(UUID resourceId) {
        return downloadRepository.findByResourceId(resourceId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceDownloadResponse getDownloadById(UUID id) {
        ResourceDownload download = downloadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Download record not found with id: " + id));
        return mapper.toResponse(download);
    }

    @Override
    @Transactional(readOnly = true)
    public long countDownloadsByResource(UUID resourceId) {
        return downloadRepository.countByResourceId(resourceId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserDownloadedResource(UUID userId, UUID resourceId) {
        return downloadRepository.existsByUserIdAndResourceId(userId, resourceId);
    }

    @Override
    public void deleteDownload(UUID id) {
        if (!downloadRepository.existsById(id)) {
            throw new RuntimeException("Download record not found with id: " + id);
        }
        downloadRepository.deleteById(id);
    }
}