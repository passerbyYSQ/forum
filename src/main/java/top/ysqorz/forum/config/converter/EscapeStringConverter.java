package top.ysqorz.forum.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * @author passerbyYSQ
 * @create 2021-02-22 23:15
 */
@Component
public class EscapeStringConverter implements Converter<String, String> {

    @Override
    public String convert(String source) {
        return ObjectUtils.isEmpty(source) ? source : HtmlUtils.htmlEscape(source);
    }

}
