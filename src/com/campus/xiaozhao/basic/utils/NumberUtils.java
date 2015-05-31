package com.campus.xiaozhao.basic.utils;

import com.component.logger.Logger;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class NumberUtils {

    public static final String TAG = "NumberUtils";
    private static final String REGION_CODE_CHINA = "CN";
    
    public static boolean isPhoneNumberValid(String number) {
        if (number.trim().length() == 0) {
            return false;
        }
        
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumber = phoneUtil.parse(number, REGION_CODE_CHINA);
            return phoneUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            Logger.e(TAG, "NumberParseException number=" + number + ", msg=" + e.toString());
            return false;
        }
        
    }
}
