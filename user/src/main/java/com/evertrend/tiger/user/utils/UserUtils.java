package com.evertrend.tiger.user.utils;

import com.evertrend.tiger.common.utils.general.LogUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class UserUtils {

    public static boolean isPhoneNumberValid(String phoneNumber, String countryCode) {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        try{
            Phonenumber.PhoneNumber numberProto = util.parse(phoneNumber, countryCode);
            return util.isValidNumber(numberProto);
        } catch (NumberParseException e){
            LogUtil.e("TAG","isPhoneNumberValid NumberParseException was thrown: " + e.toString());
        }
        return false;
    }
}
