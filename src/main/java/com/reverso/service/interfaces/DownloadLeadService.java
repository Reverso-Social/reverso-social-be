package com.reverso.service.interfaces;

import com.reverso.dto.request.DownloadLeadRequest;
import com.reverso.dto.response.DownloadLeadResponse;

import java.util.List;
import java.util.UUID;

public interface DownloadLeadService {
    
    DownloadLeadResponse createLead(DownloadLeadRequest request);
    
    List<DownloadLeadResponse> getAllLeads();
    
    List<DownloadLeadResponse> getLeadsByResource(UUID resourceId);
    
    DownloadLeadResponse getLeadById(UUID id);
    
    void deleteLead(UUID id);
}