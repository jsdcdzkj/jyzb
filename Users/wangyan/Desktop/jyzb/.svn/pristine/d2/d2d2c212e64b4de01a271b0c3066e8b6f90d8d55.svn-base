package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.core.base.Base;
import com.jsdc.core.common.utils.SerializableUtils;
import com.jsdc.rfid.App;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebService(targetNamespace = App.PHARMACY_URL, endpointInterface = "com.jsdc.rfid.service.DataService")
public class DataServiceImpl extends Base implements DataService {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public Object getDataBody(String body) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONObject jsonObject = JSONObject.parseObject(body);

        Object controller = applicationContext.getBean(jsonObject.getString("class"));
        String parameterName = jsonObject.getString("parameterName");
        String parameter = jsonObject.getString("parameter");
        String[] split = parameterName.split(",");
        String[] parameters = parameter.split(",");
        Object[] objects = new Object[parameters.length];
        Class[] classes = new Class[split.length];
        if (null != parameterName) {
            for (int i = 0; i < split.length; i++) {
                try {
                    classes[i] = Class.forName(split[i]);
                    if (null != parameters[i] && "" != parameters[i]) {
                        if (split[i].contains(".model") || split[i].contains(".vo")) {
                            objects[i] = SerializableUtils.toObject(Hex.decodeHex(parameters[i]));
                        } else {
                            if (!"null".equals(parameters[i])) {
                                Class[] paramsClasses = {parameters[i].getClass()};
                                Object[] params = {parameters[i]};
                                Constructor c = classes[i].getConstructor(paramsClasses);
                                objects[i] = c.newInstance(params);
                            } else {
                                objects[i] = null;
                            }
                        }
                    } else {
                        objects[i] = null;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (DecoderException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        Method getdata = controller.getClass().getMethod(jsonObject.getString("method"), classes);
        return getdata.invoke(controller, objects);
    }
}
