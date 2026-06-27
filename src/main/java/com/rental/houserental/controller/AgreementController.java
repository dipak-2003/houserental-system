package com.rental.houserental.controller;

import com.rental.houserental.dto.AgreementReq;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.Agreement;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.repository.AgreementRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agreement")
public class AgreementController {

    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.name}")
    private String adminName;

    @Autowired
    private  AgreementRepository agreementRepository;

    @Autowired
    private CustomUserDetails userDetails;
    @Autowired
    private OwnerRepository ownerRepository;



    // CREATE AGREEMENT (SAVE TERMS + OWNER + AGREED)
    @PostMapping("/save")
    public Agreement saveAgreement(@RequestBody AgreementReq agreementReq,@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user=userDetails.loadUserByToken(authHeader);
        Optional<Owner> owner=ownerRepository.findById(user.getId());
        Agreement agreement1=agreementRepository.findByOwner(owner.get());

        if(agreement1!=null) {
            return null;
        }

            Agreement agreement = new Agreement();
            agreement.setOwner(owner.get());
            agreement.setTerms(agreementReq.getTerms());
            agreement.setOwnerSign(agreementReq.isOwnerSign());
            agreement.setAgreed(true);
            agreement.setAdminEmail(adminEmail);
            agreement.setAdminName(adminName);
            return agreementRepository.save(agreement);


    }

    //GET ALL AGREEMENTS
    @GetMapping("/all")
    public List<Agreement> getAllAgreements() {
        return agreementRepository.findAll();
    }

    // GET AGREEMENT BY ID
    @GetMapping("/my-agreement")
    public Agreement getMyAgreement(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user=userDetails.loadUserByToken(authHeader);
        Owner owner=ownerRepository.findById(user.getId()).get();
        return agreementRepository.findByOwner(owner);
    }



}