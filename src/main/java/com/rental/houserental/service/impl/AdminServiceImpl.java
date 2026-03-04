package com.rental.houserental.service.impl;


import com.rental.houserental.dto.DashboardDto;
import com.rental.houserental.dto.ProfileDto;
import com.rental.houserental.entity.*;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.repository.*;
import com.rental.houserental.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public DashboardDto getDashboardDetails() throws Exception {
        DashboardDto dashboardDto=new DashboardDto();
        dashboardDto.setTotalTenants(tenantRepository.count());
        dashboardDto.setTotalBookings(bookingRepository.count());
        dashboardDto.setTotalOwners(ownerRepository.count());
        dashboardDto.setTotalBookings(bookingRepository.countByStatus(BookingStatus.BOOKED));
        dashboardDto.setBookedProperties(bookingRepository.countByStatus(BookingStatus.BOOKED));
        dashboardDto.setAvailableProperties(bookingRepository.countByStatus(BookingStatus.AVAILABLE));
      return dashboardDto;
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public List<Booking> getAllBooking() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Property> getAllProperty() {
        return propertyRepository.findAll();
    }

    @Override
    public void deleteOwnerByOwnerId(Long ownerId) {
        ownerRepository.deleteById(ownerId);
    }

    @Override
    public void deleteTenantByTenantId(Long tenantId) {
        tenantRepository.deleteById(tenantId);
    }


    @Override
    public String deleteProperty(Long propertyId) {
        propertyRepository.deleteById(propertyId);
        return "Property deleted successfully!";
    }
}
