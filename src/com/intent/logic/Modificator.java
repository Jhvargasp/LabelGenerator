package com.intent.logic;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

public class Modificator
{
    public static void main(final String[] args) throws Exception {
    }
    
    public static String apply(String text, final String modPrefix) throws Exception {
        String value = null;
        String mod = null;
        if (text.indexOf(modPrefix) == -1) {
            return text;
        }
        value = text.substring(text.lastIndexOf("[") + 1, text.indexOf("]"));
        mod = text.substring(text.lastIndexOf(modPrefix) + 1, text.lastIndexOf("["));
        text = StringUtils.replace(text, String.valueOf(modPrefix) + mod + '[' + value + ']', getResult(mod.toUpperCase(), value));
        return apply(text, modPrefix);
    }
    
    private static String getResult(final String mod, final String value) throws Exception {
        try {
            if (mod.equals("CAPITALIZE")) {
                return capitalize(value);
            }
            if (mod.equals("CHOMP")) {
                final String[] vals = value.split(",");
                return chomp(vals[0], vals[1]);
            }
            if (mod.equals("DIFFERECE")) {
                final String[] val = value.split(",");
                return difference(val[0], val[1]);
            }
            if (mod.equals("INSERT")) {
                final String[] val = value.split(",");
                return insert(val[0], val[1], Integer.parseInt(val[2]));
            }
            if (mod.equals("LEFT")) {
                final String[] val = value.split(",");
                return left(val[0], Integer.parseInt(val[1]));
            }
            if (mod.equals("LOWERCASE")) {
                return toLowercase(value);
            }
            if (mod.equals("PAD")) {
                final String[] val = value.split(",");
                return pad(val[0], val[1], Integer.parseInt(val[2]), val[3]);
            }
            if (mod.equals("REMOVE")) {
                final String[] val = value.split(",");
                return remove(val[0], val[1]);
            }
            if (mod.equals("RIGHT")) {
                final String[] val = value.split(",");
                return right(val[0], Integer.parseInt(val[1]));
            }
            if (mod.equals("SWAP")) {
                return swapCase(value);
            }
            if (mod.equals("TRIM")) {
                return trim(value);
            }
            if (mod.equals("UPPERCASE")) {
                return toUppercase(value);
            }
            if (mod.equals("FORMATDATE")) {
                final String[] val = value.split(",");
                return formatDate(val[0], val[1]);
            }
            if (mod.equals("REPLACE")) {
                final String[] val = value.split(",");
                return replace(val[0], val[1], val[2]);
            }
            return null;
        }
        catch (Exception ex) {
            throw new Exception("There was a problem trying to apply modificator: " + mod + ", with values " + value);
        }
    }
    
    private static String pad(final String text, final String fillWith, final int lenght, final String side) {
        return side.trim().toUpperCase().equals("R") ? padLeft(text, fillWith, lenght) : padRight(text, fillWith, lenght);
    }
    
    private static String padRight(final String text, final String fillWith, final int lenght) {
        final StringBuffer sb = new StringBuffer(text);
        for (int x = text.length(); x < lenght; ++x) {
            sb.append(fillWith);
        }
        return sb.toString();
    }
    
    private static String padLeft(final String text, final String fillWith, final int lenght) {
        final StringBuffer sb = new StringBuffer(text);
        for (int x = text.length(); x < lenght; ++x) {
            sb.insert(0, fillWith);
        }
        return sb.toString();
    }
    
    private static String trim(final String text) {
        return text.trim();
    }
    
    private static String toUppercase(final String text) {
        return text.toUpperCase();
    }
    
    private static String toLowercase(final String text) {
        return text.toLowerCase();
    }
    
    private static String capitalize(final String text) {
        return StringUtils.capitalize(text);
    }
    
    private static String remove(final String text, final String textToRemove) {
        return StringUtils.remove(text, textToRemove);
    }
    
    private static String replace(final String text, final String textToReplace, final String textToReplaceWith) {
        return StringUtils.replace(text, textToReplace, textToReplaceWith);
    }
    
    private static String chomp(final String text, final String separator) {
        return StringUtils.chomp(text, separator);
    }
    
    private static String insert(final String text, final String textToInsert, final int index) {
        return new StringBuffer(text).insert(index, textToInsert).toString();
    }
    
    private static String difference(final String text, final String textToCompareWith) {
        return StringUtils.difference(text, textToCompareWith);
    }
    
    private static String left(final String text, final int numberOfCharacters) {
        return StringUtils.left(text, numberOfCharacters);
    }
    
    private static String right(final String text, final int numberOfCharacters) {
        return StringUtils.right(text, numberOfCharacters);
    }
    
    private static String swapCase(final String text) {
        return StringUtils.swapCase(text);
    }
    
    private static String formatDate(final String text, final String format) throws ParseException {
        return new SimpleDateFormat(format).format(new SimpleDateFormat(LabelGenerator.DATE_FORMAT_FULL).parse(text));
    }
}