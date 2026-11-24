package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.DonorAvailabilityRequest;
import com.bdms.backend.dto.request.DonorUpdateRequest;
import com.bdms.backend.dto.response.DonorResponseDto;
import com.bdms.backend.entity.Donor;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.DonorMapper;
import com.bdms.backend.repository.DonorRepository;
import com.bdms.backend.service.DonorService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;

    @Override
    @Transactional(readOnly = true)
    public DonorResponseDto getDonorByUserId(Long userId) {
        Donor donor = donorRepository.findByUser_UserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor profile not found for user id: " + userId));

        return DonorMapper.toDto(donor);
    }

    @Override
    @Transactional(readOnly = true)
    public DonorResponseDto getDonorById(Long donorId) {
        Donor donor = donorRepository.findByIdWithUser(donorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found with id: " + donorId));

        return DonorMapper.toDto(donor);
    }

    @Override
    public List<DonorResponseDto> getAllDonors() {
        return donorRepository.findAll().stream().map(DonorMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonorResponseDto> searchDonors(String bloodGroup, String city) {

        System.out.println("Using custom searchDonors()");

        List<Donor> donors;

        if (StringUtils.hasText(bloodGroup) && StringUtils.hasText(city)) {
            System.out.println("findByBloodGroupAndCityAndAvailabilityTrue");
            donors = donorRepository.findByBloodGroupAndCityAndAvailabilityTrue(bloodGroup, city);

        } else if (StringUtils.hasText(bloodGroup)) {
            System.out.println("findByBloodGroupAndAvailabilityTrue");
            donors = donorRepository.findByBloodGroupAndAvailabilityTrue(bloodGroup);

        } else if (StringUtils.hasText(city)) {
            System.out.println("findByCity");
            donors = donorRepository.findByCity(city);

        } else {
            System.out.println("findAll");
            donors = donorRepository.findAll();
        }

        return donors.stream().map(DonorMapper::toDto).toList();
    }

    @Override
    @Transactional
    public DonorResponseDto updateDonorProfile(Long donorId, DonorUpdateRequest request) {
        Donor donor = findDonor(donorId);

        if (request.getAge() != null) donor.setAge(request.getAge());
        if (request.getGender() != null) donor.setGender(request.getGender());
        if (request.getBloodGroup() != null) donor.setBloodGroup(request.getBloodGroup());
        if (request.getMobile() != null) donor.setMobile(request.getMobile());
        if (request.getCity() != null) donor.setCity(request.getCity());
        if (request.getAddress() != null) donor.setAddress(request.getAddress());

        return DonorMapper.toDto(donorRepository.save(donor));
    }

    @Override
    @Transactional
    public DonorResponseDto updateAvailability(Long donorId, DonorAvailabilityRequest request) {
        Donor donor = findDonor(donorId);
        donor.setAvailability(request.getAvailability());
        return DonorMapper.toDto(donorRepository.save(donor));
    }

    @Override
    @Transactional
    public void deleteDonor(Long donorId) {
        if (!donorRepository.existsById(donorId)) {
            throw new ResourceNotFoundException("Donor not found with id: " + donorId);
        }
        donorRepository.deleteById(donorId);
    }

    private Donor findDonor(Long donorId) {
        return donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + donorId));
    }
}
