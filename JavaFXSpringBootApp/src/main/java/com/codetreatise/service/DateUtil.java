package com.codetreatise.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class DateUtil {
	
	private static String patern = "yyyy-MM-dd";
	
	//Forcer l'affichage  de la date sous le format patern ci deçu
	public static void datePickerFormat(DatePicker datePicker ) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
		     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(patern);

		     {
		    	 datePicker.setPromptText(patern.toLowerCase());
		     }

		     @Override public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 });
	}
	
	/** The date formatter. */
	private static final DateTimeFormatter DATE_FORMATTER =
			DateTimeFormatter.ofPattern(patern);

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DateUtil#DATE_PATTERN} is used.
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
     * to a {@link LocalDate} object.
     *
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    public static LocalDate parse(String dateString) {
        try {
        	return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Checks the String whether it is a valid date.
     *
     * @param dateString
     * @return true if the String is a valid date
     */
    public static boolean validDate(String dateString) {
    	// Try to parse the String.
    	return DateUtil.parse(dateString) != null;
    }
    
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    	 return Instant.ofEpochMilli(dateToConvert.getTime())
    		      .atZone(ZoneId.systemDefault())
    		      .toLocalDate();
    }
	
}
