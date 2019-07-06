package com.lzh.web.convertes;

import com.lzh.common.utils.UtilFuns;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter implements Converter<String,Date> {

    private String pattern;

    public void setPattern(String pattern){
        this.pattern = pattern;
    }


    @Override
    public Date convert(String source) {
        try{
            //给pattern注入默认值
            if (UtilFuns.isEmpty(pattern)){
                pattern = "yyyy-MM-dd";
            }
            DateFormat format = new SimpleDateFormat(pattern);
            Date date = format.parse(source);
            return date;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
