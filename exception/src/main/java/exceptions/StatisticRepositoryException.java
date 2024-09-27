package exceptions;

import org.springframework.dao.DataAccessException;

public class StatisticRepositoryException extends RuntimeException {

    public StatisticRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
