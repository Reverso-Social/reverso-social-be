package com.reverso.service.interfaces;

import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;

import java.util.List;
import java.util.UUID;

public interface ResourceDownloadService {
    
    ResourceDownloadResponse createDownload(ResourceDownloadRequest request);
    
    List<ResourceDownloadResponse> getAllDownloads();
    
    List<ResourceDownloadResponse> getDownloadsByUser(UUID userId);
    
    List<ResourceDownloadResponse> getDownloadsByResource(UUID resourceId);
    
    ResourceDownloadResponse getDownloadById(UUID id);
    
    long countDownloadsByResource(UUID resourceId);
    
    boolean hasUserDownloadedResource(UUID userId, UUID resourceId);
    
    void deleteDownload(UUID id);
}