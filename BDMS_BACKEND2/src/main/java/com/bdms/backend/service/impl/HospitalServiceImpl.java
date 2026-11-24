package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.HospitalUpdateRequest;
import com.bdms.backend.dto.response.HospitalDto;
import com.bdms.backend.entity.Hospital;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.HospitalMapper;
import com.bdms.backend.repository.HospitalRepository;
import com.bdms.backend.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional(readOnly = true)
    public HospitalDto getHospitalById(Long hospitalId) {
        Hospital hospital = hospitalRepository.findByIdWithUser(hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital not found with id: " + hospitalId));

        return HospitalMapper.toDto(hospital);
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalDto getHospitalByUserId(Long userId) {
        Hospital hospital = hospitalRepository.findByUser_UserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital profile not found for user id: " + userId));

        return HospitalMapper.toDto(hospital);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalDto> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(HospitalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalDto> getVerifiedHospitals() {
        return hospitalRepository.findByVerified(true)
                .stream()
                .map(HospitalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public HospitalDto updateHospital(Long hospitalId, HospitalUpdateRequest request) {

        Hospital hospital = findHospital(hospitalId);

        if (request.getHospitalName() != null) {
            hospital.setHospitalName(request.getHospitalName());
        }

        if (request.getRegistrationNo() != null) {
            hospital.setRegistrationNo(request.getRegistrationNo());
        }

        if (request.getContactNo() != null) {
            hospital.setContactNo(request.getContactNo());
        }

        if (request.getCity() != null) {
            hospital.setCity(request.getCity());
        }

        if (request.getAddress() != null) {
            hospital.setAddress(request.getAddress());
        }

        return HospitalMapper.toDto(hospitalRepository.save(hospital));
    }

    @Override
    @Transactional
    public void deleteHospital(Long hospitalId) {

        if (!hospitalRepository.existsById(hospitalId)) {
            throw new ResourceNotFoundException("Hospital not found with id: " + hospitalId);
        }

        hospitalRepository.deleteById(hospitalId);
    }

    @Transactional(readOnly = true)
    private Hospital findHospital(Long hospitalId) {

        return hospitalRepository.findByIdWithUser(hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
    }
}