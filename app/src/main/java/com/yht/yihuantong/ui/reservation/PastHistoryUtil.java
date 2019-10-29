package com.yht.yihuantong.ui.reservation;

import android.content.Context;
import android.text.TextUtils;

import com.yht.yihuantong.R;

/**
 * @author 顿顿
 * @date 19/10/29 10:13
 * @description
 */
public class PastHistoryUtil {
    /**
     * 既往病史
     */
    public static String getPastMedical(Context context, String pastMedical) {
        return verifyPastMedical(context, pastMedical)
               ? pastMedical
               : context.getString(R.string.txt_past_medical_his_not);
    }

    static boolean verifyPastMedical(Context context, String pastMedical) {
        return !TextUtils.isEmpty(pastMedical) &&
               !TextUtils.equals(pastMedical, context.getString(R.string.txt_past_medical_his_not)) &&
               !TextUtils.equals(pastMedical, context.getString(R.string.txt_past_medical_his_not_p));
    }

    /**
     * 家族病史
     */
    public static String getFamilyMedical(Context context, String familyMedical) {
        return verifyFamilyMedical(context, familyMedical)
               ? familyMedical
               : context.getString(R.string.txt_family_medical_his_not);
    }

    static boolean verifyFamilyMedical(Context context, String familyMedical) {
        return !TextUtils.isEmpty(familyMedical) &&
               !TextUtils.equals(familyMedical, context.getString(R.string.txt_family_medical_his_not)) &&
               !TextUtils.equals(familyMedical, context.getString(R.string.txt_family_medical_his_not_p));
    }

    /**
     * 过敏史
     */
    public static String getAllergies(Context context, String allergies) {
        return verifyAllergies(context, allergies) ? allergies : context.getString(R.string.txt_allergies_not);
    }

    static boolean verifyAllergies(Context context, String allergies) {
        return !TextUtils.isEmpty(allergies) &&
               !TextUtils.equals(allergies, context.getString(R.string.txt_allergies_not)) &&
               !TextUtils.equals(allergies, context.getString(R.string.txt_allergies_not_p));
    }
}
