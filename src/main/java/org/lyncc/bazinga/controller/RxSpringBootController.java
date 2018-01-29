package org.lyncc.bazinga.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lyncc.bazinga.anno.config.EnabledBazingaAnnotation;
import org.lyncc.bazinga.service.BazingaService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * web 控制器
 *
 * @author liguolin
 * @create 2018-01-29 10:59
 **/
@RestController
@RequestMapping(value="/rx/bazinga")
@Api(description = "测试API")
public class RxSpringBootController {

    @Autowired
    private BazingaService bazingaService;


    @ApiOperation(value="测试00")
    @PostMapping(value="test00")
    public String test00(){
        return "success";
    }

    @ApiOperation(value="测试01")
    @PostMapping(value="test01")
    public String test01(){
        return "success" + bazingaService.bazingaService001();
    }


}
