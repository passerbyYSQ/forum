package top.ysqorz.forum;

import org.junit.Test;
import top.ysqorz.forum.utils.CommonUtils;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/1/9
 */
public class CommonUtilsTest {

    @Test
    public void testGetLocalHost() {
        String localHostStr = CommonUtils.getLocalHostStr();
        System.out.println(localHostStr);
    }

}
