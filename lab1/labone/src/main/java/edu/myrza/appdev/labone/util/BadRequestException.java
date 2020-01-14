package edu.myrza.appdev.labone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends Exception{

    private BadReqSubcodes subcode;

}
