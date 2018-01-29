package org.lyncc.bazinga.service.impl;

import org.lyncc.bazinga.service.BazingaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * bazinga服务类
 *
 * @author liguolin
 * @create 2018-01-29 11:48
 **/
@Service
public class BazingaServiceImpl implements BazingaService {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String bazingaService001() {
        logger.info("hello bazingaService001");
        return "bazingaService001";
    }
}
