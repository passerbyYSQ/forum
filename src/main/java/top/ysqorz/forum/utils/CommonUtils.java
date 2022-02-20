package top.ysqorz.forum.utils;

import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 23:15
 */
public class CommonUtils {

    /**
     * 解析url的参数值
     */
    public static String getUrlParam(String url, String name) {
        try {
            url = HtmlUtils.htmlUnescape(URLDecoder.decode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("(^|\\?|&)" + name + "=([^&]*)(&|$)");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(2);
    }

    /**
     * https://www.cnblogs.com/death00/p/11557305.html
     */
    public static String getIpFromRequest(HttpServletRequest request) {
        List<String> candidateIps = new ArrayList<>();

        String xFor = request.getHeader("X-Forwarded-For");
        if (xFor != null) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            // "abc, sdv,  sddv  ,, sdv ,  ,  dfb "
            candidateIps.add(xFor.split("((\\s*),(\\s*))+")[0]);
        }
        candidateIps.add(request.getHeader("X-Real-IP"));
        candidateIps.add(request.getHeader("Proxy-Client-IP"));
        candidateIps.add(request.getHeader("WL-Proxy-Client-IP"));
        candidateIps.add(request.getHeader("HTTP_CLIENT_IP"));
        candidateIps.add(request.getHeader("HTTP_X_FORWARDED_FOR"));
        candidateIps.add(request.getRemoteAddr());

        Optional<String> finalIp = candidateIps.stream()
                .filter(s -> !ObjectUtils.isEmpty(s) && !"unKnown".equalsIgnoreCase(s))
                .findFirst();
        return finalIp.map(String::trim).orElse("");
    }

    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
