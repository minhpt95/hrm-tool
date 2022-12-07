package com.vatek.hrmtool.util;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import java.util.Locale;


public class CommonUtil {


    public static Pageable buildPageable(int pageIndex, int pageSize) {
        return PageRequest.of(pageIndex, pageSize);
    }

    public static Pageable buildPageable(int pageIndex, int pageSize,Direction direction,String... properties) {
        return PageRequest.of(pageIndex, pageSize,direction,properties);
    }

    public static String convertMoneyToText(String input) {
        String output = "";
        try {
            NumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(new Locale("vi", "VN"), RuleBasedNumberFormat.SPELLOUT);
            output = ruleBasedNumberFormat.format(Long.parseLong(input)) + " Đồng";
        } catch (Exception e) {
            output = "Không đồng";
        }
        return output.toUpperCase();
    }
}
