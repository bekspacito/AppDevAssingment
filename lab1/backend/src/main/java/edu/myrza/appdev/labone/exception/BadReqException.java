package edu.myrza.appdev.labone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadReqException extends RuntimeException{
    private Object respBody;
}
