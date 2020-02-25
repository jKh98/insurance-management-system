package test;

import java.time.Instant;

public class TestValues {

    // Travel policy 1
    public static final long TRAVEL_1_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long TRAVEL_1_EXPIRY = Instant.now().getEpochSecond() + 25 * 86400; // expires after 25 days
    public static final int TRAVEL_1_IS_VALID = 1;
    public static final String TRAVEL_1_DEPARTURE = "Beirut";
    public static final String TRAVEL_1_DESTINATION = "Paris";
    public static final int TRAVEL_1_FAMILY = 0;

    // Travel policy 2
    public static final long TRAVEL_2_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long TRAVEL_2_EXPIRY = Instant.now().getEpochSecond() + 15 * 86400; // expires after 15 days
    public static final int TRAVEL_2_IS_VALID = 0;
    public static final String TRAVEL_2_DEPARTURE = "Beirut";
    public static final String TRAVEL_2_DESTINATION = "Dubai";
    public static final int TRAVEL_2_FAMILY = 1;

    // Travel policy 3
    public static final long TRAVEL_3_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long TRAVEL_3_EXPIRY = Instant.now().getEpochSecond() + 40 * 86400; // expires after 40 days (fails)
    public static final int TRAVEL_3_IS_VALID = 1;
    public static final String TRAVEL_3_DEPARTURE = "Beirut";
    public static final String TRAVEL_3_DESTINATION = "Cairo";
    public static final int TRAVEL_3_FAMILY = 1;
    ;
    // Motor policy 1
    public static final long MOTOR_1_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long MOTOR_1_EXPIRY = Instant.now().getEpochSecond() + 5 * 86400; // expires after 5 days
    public static final int MOTOR_1_IS_VALID = 1;
    public static final double MOTOR_1_VEHICLE_PRICE = 20000.0;

    // Motor policy 2
    public static final long MOTOR_2_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long MOTOR_2_EXPIRY = Instant.now().getEpochSecond() + 10 * 86400; // expires after 10 days
    public static final int MOTOR_2_IS_VALID = 1;
    public static final double MOTOR_2_VEHICLE_PRICE = 10000.0;

    // Medical policy 1
    public static final long MEDICAL_1_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long MEDICAL_1_EXPIRY = Instant.now().getEpochSecond() + 6 * 86400; // expires after 6 days;
    public static final int MEDICAL_1_IS_VALID = 1;
    public static final String MEDICAL_1_NAME = "John Doe";
    public static final String MEDICAL_1_GENDER = "male";
    public static final String MEDICAL_1_RELATION = "self";
    public static final long MEDICAL_1_BIRTH_DATE = 760147200;

    // Medical policy 2
    public static final long MEDICAL_2_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long MEDICAL_2_EXPIRY = Instant.now().getEpochSecond() + 22 * 86400; // expires after 22 days
    public static final int MEDICAL_2_IS_VALID = 0;
    public static final String MEDICAL_2_NAME = "Sarah James";
    public static final String MEDICAL_2_GENDER = "female";
    public static final String MEDICAL_2_RELATION = "spouse";
    public static final long MEDICAL_2_BIRTH_DATE = 817862400;

    // Medical policy 3
    public static final long MEDICAL_3_EFFECTIVE = Instant.now().getEpochSecond();
    public static final long MEDICAL_3_EXPIRY = Instant.now().getEpochSecond() - 10 * 86400; // expiry < effective fails
    public static final int MEDICAL_3_IS_VALID = 1;
    public static final String MEDICAL_3_NAME = "Oliver Smith";
    public static final String MEDICAL_3_GENDER = "male";
    public static final String MEDICAL_3_RELATION = "son";
    public static final long MEDICAL_3_BIRTH_DATE = Instant.now().getEpochSecond();

    // Claim 1
    public static final String CLAIM_1_POLICY_NO = "";
    public static final long CLAIM_1_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_1_CLAIMED_AMOUNT = 0.0;

    // Claim 1
    public static final String CLAIM_2_POLICY_NO = "";
    public static final long CLAIM_2_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_2_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_3_POLICY_NO = "";
    public static final long CLAIM_3_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_3_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_4_POLICY_NO = "";
    public static final long CLAIM_4_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_4_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_5_POLICY_NO = "";
    public static final long CLAIM_5_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_5_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_6_POLICY_NO = "";
    public static final long CLAIM_6_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_6_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_7_POLICY_NO = "";
    public static final long CLAIM_7_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_7_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_8_POLICY_NO = "";
    public static final long CLAIM_8_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_8_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_9_POLICY_NO = "";
    public static final long CLAIM_9_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_9_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_10_POLICY_NO = "";
    public static final long CLAIM_10_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_10_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_11_POLICY_NO = "";
    public static final long CLAIM_11_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_11_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_12_POLICY_NO = "";
    public static final long CLAIM_12_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_12_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_13_POLICY_NO = "";
    public static final long CLAIM_13_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_13_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_14_POLICY_NO = "";
    public static final long CLAIM_14_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_14_CLAIMED_AMOUNT = 0.0;
    // Claim 1
    public static final String CLAIM_15_POLICY_NO = "";
    public static final long CLAIM_15_INCURRED = Instant.now().getEpochSecond();
    public static final double CLAIM_15_CLAIMED_AMOUNT = 0.0;


}
