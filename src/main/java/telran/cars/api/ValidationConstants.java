package telran.cars.api;

public interface ValidationConstants {
	String MISSING_CAR_NUMBER_MESSAGE = "Missing car number";
	String CAR_NUMBER_REGEXP = "(\\d{3}-\\d{2}-\\d{3})|(\\d{2}-\\d{3}-\\d{2})";
	String WRONG_CAR_NUMBER_MESSAGE = "Incorrect Car Number";
	String MISSING_CAR_MODEL_MESSAGE = "Missing car model";
	String MISSING_MODEL_YEAR_MESSAGE = "Missing car model year";
	String MISSING_PERSON_ID_MESSAGE = "Missing person ID";
	long MIN_PERSON_ID_VALUE = 100000l;
	long MAX_PERSON_ID_VALUE = 999999l;
	int MIN_KILOMETERS_VALUE = 0;
	int MAX_KILOMETERS_VALUE = 200000;
	int MIN_YEAR_VALUE = 1950;
	int MAX_YEAR_VALUE = 2024;
	int MIN_ENGINE_POWER_VALUE = 50;
	int MAX_ENGINE_POWER_VALUE = 5000;
	int MIN_ENGINE_CAPACITY_VALUE = 100;
	int MAX_ENGINE_CAPACITY_VALUE = 10000;
	String WRONG_MIN_PERSON_ID_VALUE = "Person ID must be greater or equal " + MIN_PERSON_ID_VALUE;
	String WRONG_MAX_PERSON_ID_VALUE = "Person ID must be less or equal " + MAX_PERSON_ID_VALUE;
	String MISSING_PERSON_NAME_MESSAGE = "Missing person name";
	String MISSING_BIRTH_DATE_MESSAGE = "Missing person's birth date";
	String DATE_REGEXP = "\\d{4}-\\d{2}-\\d{2}";
	String WRONG_DATE_FORMAT = "Wrong date format, must be YYYY-MM-dd";
	String MISSING_PERSON_EMAIL = "Missing email address";
	String WRONG_EMAIL_FORMAT = "Wrong email format";
	String MODEL_YEAR_REGEXP = "\\d{4}";
	String WRONG_MIN_KILOMETERS_VALUE = "Kilometers must be greater or equal" + MIN_KILOMETERS_VALUE;
	String WRONG_MAX_KILOMETERS_VALUE = "Kilometers must be less or equal" + MAX_KILOMETERS_VALUE;
	String WRONG_MIN_YEAR_VALUE = "Year must be greater or equal" + MIN_YEAR_VALUE;
	String WRONG_MAX_YEAR_VALUE = "Year must be less or equal" + MAX_YEAR_VALUE;
	String MISSING_CAR_COLOR_MESSAGE = "Missing car color";
	String MISSING_KILOMETERS_MESSAGE = "Missing kilometers";
	String CAR_STATE_REGEXP = "[OLD]|[NEW]|[GOOD]|[MIDDLE]|[BAD]";
	String WRONG_CAR_STATE_MESSAGE = "Car state must be on of: OLD, NEW, GOOD, MIDDLE, BAD";
	String MISSING_CAR_STATE_MESSAGE = "Missing car state";
	String MISSING_TRADE_DEAL_DATE_MESSAGE = "Missing trade deal date";
	String MISSING_MODEL_COMPANY_MESSAGE = "Missing model company";
	String MISSING_ENGINE_POWER_MESSAGE = "Missing engine power";
	String MISSING_ENGINE_CAPACITY_MESSAGE = "Missing engine power";
	String WRONG_MIN_ENGINE_POWER_VALUE = "Engine power must be greater or equal" + MIN_ENGINE_POWER_VALUE;
	String WRONG_MIN_ENGINE_CAPACITY_VALUE = "Engine power must be less or equal" + MAX_ENGINE_POWER_VALUE;
	String WRONG_MAX_ENGINE_POWER_VALUE = "Engine capacity must be greater or equal" + MIN_ENGINE_CAPACITY_VALUE;
	String WRONG_MAX_ENGINE_CAPACITY_VALUE = "Engine capacity must be less or equal" + MAX_ENGINE_CAPACITY_VALUE;
	
	
}
