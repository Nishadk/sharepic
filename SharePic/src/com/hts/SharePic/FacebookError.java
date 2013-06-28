package com.hts.SharePic;

/**
 * Encapsulation of a Facebook Error: a Facebook request that could not be
 * fulfilled.
 *
 * @author ssoneff@facebook.com
 */
public class FacebookError extends Throwable {

    private static final long serialVersionUID = 1L;

    private int mErrorCode = 0;
    private String mErrorType;

    public FacebookError(String message) {
        super(message);
    }

    public FacebookError(String message, String type, int code) {
        super(message);
        mErrorType = type;
        mErrorCode = code;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorType() {
        return mErrorType;
    }

}
