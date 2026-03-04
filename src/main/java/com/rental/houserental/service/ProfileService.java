package com.rental.houserental.service;

import com.rental.houserental.dto.ProfileDto;
import com.rental.houserental.entity.Profile;
import com.rental.houserental.enums.Role;

public interface ProfileService {

    public Profile getProfile(Long userId, Role role) throws Exception;
    public Profile updateProfile (Long userId, Role role, ProfileDto profileDto) throws Exception;
    public void deleteProfile(Long userId, Role role) throws Exception;
}
