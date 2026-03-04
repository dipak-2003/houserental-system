package com.rental.houserental.service;
import com.rental.houserental.dto.DashboardDto;
import com.rental.houserental.entity.*;
import java.util.List;


public interface AdminService {
    public DashboardDto getDashboardDetails() throws Exception;
    public List<Owner> getAllOwners() throws Exception;
    public List<Tenant> getAllTenants() throws Exception;
    public List<Booking> getAllBooking() throws Exception;
    public List<Property> getAllProperty() throws Exception;
    public void deleteOwnerByOwnerId(Long ownerId) throws Exception;
    public void deleteTenantByTenantId(Long tenantId) throws Exception;
    public String deleteProperty(Long propertyId) throws Exception;

}
