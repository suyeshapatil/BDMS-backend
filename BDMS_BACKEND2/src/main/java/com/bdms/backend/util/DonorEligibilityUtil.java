package com.bdms.backend.util;

import com.bdms.backend.entity.Donor;

import java.time.LocalDate;

/**
 * Implements SRS 6.5.5 "Donation Eligibility Notifications": a donor becomes eligible
 * again only after {@link Constants#DONOR_ELIGIBILITY_GAP_DAYS} days have passed since
 * their last recorded donation. Donors who have never donated are always eligible.
 */
public final class DonorEligibilityUtil {

    private DonorEligibilityUtil() {
    }

    public static boolean isEligible(Donor donor) {
        if (donor == null) {
            return false;
        }
        LocalDate lastDonation = donor.getLastDonationDate();
        if (lastDonation == null) {
            return true;
        }
        LocalDate nextEligibleDate = lastDonation.plusDays(Constants.DONOR_ELIGIBILITY_GAP_DAYS);
        return !nextEligibleDate.isAfter(LocalDate.now());
    }
}
