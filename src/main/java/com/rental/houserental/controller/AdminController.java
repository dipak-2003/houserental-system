package com.rental.houserental.controller;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.*;
import com.rental.houserental.service.AdminService;
import com.rental.houserental.service.CustomUserDetails;
import com.rental.houserental.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CustomUserDetails userDetailsService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PropertyService propertyService;



    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(
            @RequestHeader("Authorization") String authHeader) throws Exception {

        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        System.out.println(user);

        if (user !=null) {
            return ResponseEntity.ok(adminService.getDashboardDetails());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized Access");
    }



    @GetMapping("/tenants")
    public ResponseEntity<List<Tenant>> getAllTenants() throws Exception {
        return ResponseEntity.ok(adminService.getAllTenants());
    }

    @DeleteMapping("/tenant/{id}")
    public ResponseEntity<String> deleteTenant(@PathVariable Long id) throws Exception {
        adminService.deleteTenantByTenantId(id);
        return ResponseEntity.ok("Tenant deleted successfully");
    }



    @GetMapping("/owners")
    public ResponseEntity<List<Owner>> getAllOwners() throws Exception {
        return ResponseEntity.ok(adminService.getAllOwners());
    }

    @DeleteMapping("/owner/{id}")
    public ResponseEntity<String> deleteOwner(@PathVariable Long id) throws Exception {
        adminService.deleteOwnerByOwnerId(id);
        return ResponseEntity.ok("Owner deleted successfully");
    }



    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() throws Exception {
        return ResponseEntity.ok(adminService.getAllBooking());
    }



    @GetMapping("/properties")
    public ResponseEntity<List<Property>> getAllProperties() throws Exception {
        return ResponseEntity.ok(adminService.getAllProperty());
    }

    @DeleteMapping("/property/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(adminService.deleteProperty(id));
    }

    @PutMapping("/property/approve/{id}")
    public ResponseEntity<?> updatePropertyApproveStatus( @RequestHeader("Authorization") String authHeader,
    @PathVariable Long id) throws Exception {
        LoggedUser loggedAdmin=userDetailsService.loadUserByToken(authHeader);
        Property property=propertyService.approveProperty(id,loggedAdmin.getId());
         return new ResponseEntity<>(property,HttpStatus.OK);
        }

    @PutMapping("/property/reject/{id}")
    public ResponseEntity<?> updatePropertyRejectStatus( @RequestHeader("Authorization") String authHeader,
                                                   @PathVariable Long id) throws Exception {
        LoggedUser loggedAdmin=userDetailsService.loadUserByToken(authHeader);
        Property property=propertyService.rejectProperty(id,loggedAdmin.getId());
        return new ResponseEntity<>(property,HttpStatus.OK);
    }

}
