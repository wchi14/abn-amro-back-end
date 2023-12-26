package abn.amro100.exception;

import abn.amro100.constant.Constant;

public class InvalidSourceFileException extends Exception {
    public InvalidSourceFileException() {
        super(Constant.ERROR_MESSAGE_INVALID_SOURCE_FILE);
    }
}
