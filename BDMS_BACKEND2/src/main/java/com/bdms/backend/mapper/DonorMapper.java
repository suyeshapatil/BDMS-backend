package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.DonorResponseDto;
import com.bdms.backend.entity.Donor;

public final class DonorMapper {

    private DonorMapper() {
    }

    public static DonorResponseDto toDto(Donor donor) {
        if (donor == null) {
            return null;
        }
        DonorResponseDto.DonorResponseDtoBuilder builder = DonorResponseDto.builder()
                .donorId(donor.getDonorId())
                .age(donor.getAge())
                .gender(donor.getGender())
                .bloodGroup(donor.getBloodGroup())
                .mobile(donor.getMobile())
                .city(donor.getCity())
                .address(donor.getAddress())
                .availability(donor.getAvailability())
                .lastDonationDate(donor.getLastDonationDate())
                .totalDonations(donor.getTotalDonations());

        if (donor.getUser() != null) {
            builder.userId(donor.getUser().getUserId())
                    .fullName(donor.getUser().getFullName())
                    .email(donor.getUser().getEmail());
        }
        return builder.build();
    }
}
