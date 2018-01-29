package org.lyncc.bazinga.anno.config;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

import java.util.ArrayList;
import java.util.List;

/**
 * config配置
 *
 * @author liguolin
 * @create 2018-01-29 13:10
 **/
public class ConfigSelector extends AdviceModeImportSelector<EnabledBazingaMethodAnnotation> {


    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return getProxyImports();
            case ASPECTJ:
//                return getAspectJImports();
            default:
                return null;
        }
    }

    private String[] getProxyImports() {
        List<String> result = new ArrayList<String>();
        result.add(AutoProxyRegistrar.class.getName());
        result.add(BazingaProxyConfiguration.class.getName());
        return result.toArray(new String[result.size()]);
    }
}
