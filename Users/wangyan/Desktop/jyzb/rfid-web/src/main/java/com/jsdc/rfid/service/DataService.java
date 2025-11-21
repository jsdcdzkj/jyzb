package com.jsdc.rfid.service;

import com.jsdc.rfid.App;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.lang.reflect.InvocationTargetException;

@WebService(targetNamespace = App.PHARMACY_URL, name = "DataService")
public interface DataService {

    @WebMethod(operationName = "getDataBody")
    Object getDataBody(@WebParam(name = "body") String body) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException;

}