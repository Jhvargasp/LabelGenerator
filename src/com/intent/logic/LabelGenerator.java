package com.intent.logic;

import org.apache.commons.lang.StringUtils;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.intent.utils.ConfigReader;

public class LabelGenerator
{
	public static String DATE_FORMAT_FULL="dd/MM/yyyy HH:mm";
    public static void main(final String[] args) throws Exception {
        final LabelGenerator lb = new LabelGenerator();
        System.out.println(lb.GenerateLabel("TEMPLATE1", new String[] {  }, "correspondencia"));
    }
    
    public String GenerateLabel(final String template, final String[] params, final String autoNum) throws Exception {
        System.out.println("---------------");
        
        String templatePattern = ConfigReader.readConfigEntry(template);
        if (templatePattern == null) {
            throw new Exception("Template: " + template + " could not be found.");
        }
        if (templatePattern.indexOf(ConfigReader.readConfigEntry("CURRENT_DATE_VAR")) > -1) {
            templatePattern = StringUtils.replace(templatePattern, String.valueOf(ConfigReader.readConfigEntry("CURRENT_DATE_VAR")) + ConfigReader.readConfigEntry("VAR_VALUE_SEPARATOR") + ConfigReader.readConfigEntry("VAR_SPECIFICATOR"), new SimpleDateFormat(DATE_FORMAT_FULL).format(new Date()));
        }
        if (autoNum != null && templatePattern.indexOf(ConfigReader.readConfigEntry("AUTONUM_VAR")) > -1) {
            templatePattern = StringUtils.replace(templatePattern, String.valueOf(ConfigReader.readConfigEntry("AUTONUM_VAR")) + ConfigReader.readConfigEntry("VAR_VALUE_SEPARATOR") + ConfigReader.readConfigEntry("VAR_SPECIFICATOR"), String.valueOf(Autonums.getAutoNum(autoNum, ConfigReader.bundle)));
        }
        if (params != null && params.length > 0) {
            for (int count = 0; count < params.length; ++count) {
                if (!params[count].equals("")) {
                    final String tmp = params[count];
                    if (tmp.indexOf(ConfigReader.readConfigEntry("PARAM_NAME_VALUE_SEPARATOR")) == -1) {
                        throw new Exception("Parameter: " + tmp + " is invalid. Sintaxt error. Array: " + params);
                    }
                    final String value = tmp.split(ConfigReader.readConfigEntry("PARAM_NAME_VALUE_SEPARATOR"))[1];
                    final String paramName = tmp.split(ConfigReader.readConfigEntry("PARAM_NAME_VALUE_SEPARATOR"))[0];
                    final String paramPattern = String.valueOf(ConfigReader.readConfigEntry("PARAMS_VAR")) + ConfigReader.readConfigEntry("PARAM_NAME_VALUE_SEPARATOR") + paramName;
                    if (templatePattern.indexOf(paramPattern) == -1) {
                        throw new Exception("Param not found: " + paramName);
                    }
                    templatePattern = StringUtils.replace(templatePattern, paramPattern, value);
                }
            }
        }
        final String[] parts = StringUtils.substringsBetween(templatePattern, "{", "}");
        final String[] separators = StringUtils.substringsBetween(templatePattern, "}", "{");
        final StringBuffer sb = new StringBuffer();
        final String modPrefix = ConfigReader.readConfigEntry("MODIFICATOR_PREFIX");
        int count2 = 0;
        for (int x = 0; x < parts.length; ++x) {
            final String part = parts[x];
            sb.append(Modificator.apply(part, modPrefix));
            if (separators == null) {
                break;
            }
            if (count2 <= separators.length - 1) {
                sb.append(separators[count2]);
                ++count2;
            }
        }
        sb.insert(0, StringUtils.substringBefore(templatePattern, "{"));
        sb.insert(sb.length(), StringUtils.substring(templatePattern, templatePattern.lastIndexOf(125) + 1));
        final String result = sb.toString();
        if (result == null || result.length() == 0) {
            throw new Exception("No label could be generated from template: " + template + ". Pattern: " + templatePattern);
        }
        return result;
    }
    
    public void ResetAutonum(final String autoNumName, final int resetValue) throws Exception {
        Autonums.setAutoNum(autoNumName, ConfigReader.bundle, resetValue);
    }
}