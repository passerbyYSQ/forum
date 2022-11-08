package top.ysqorz.forum.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.common.ResultModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 23:15
 */
public class CommonUtils {

    private static final Safelist safeList = Safelist.basicWithImages();
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    static {
        // 富文本编辑时一些样式是使用style来进行实现的 比如红色字体 所以需要给所有标签添加style属性
        safeList.addAttributes(":all", "style", "class");
    }

    public static String cleanXSS(String html) {
        return Jsoup.clean(html, "", safeList, outputSettings);
    }

    public static String cleanRichText(String html) {
        return cleanRichText(html, "<code>", "</code>");
    }

    public static String cleanRichText(String html, String leftDelimiter, String rightDelimiter) {
        StringBuilder oldSbd = new StringBuilder(html);
        StringBuilder newSbd = new StringBuilder();
        int p = 0;
        while (true) {
            int leftIdx = oldSbd.indexOf(leftDelimiter, p);
            int rightIdx = oldSbd.indexOf(rightDelimiter, leftIdx + 1);
            if (leftIdx != -1 && rightIdx != -1) { // 成对出现
//                String cleanText = cleanXSS(oldSbd.substring(p, leftIdx)); // 清除敏感标签
                newSbd.append(oldSbd.substring(p, leftIdx));

                int replaceStart = leftIdx + leftDelimiter.length();
                String escapeText = HtmlUtils.htmlEscape(oldSbd.substring(replaceStart, rightIdx)); // 转义
                newSbd.append(leftDelimiter).append(escapeText).append(rightDelimiter);

                p = rightIdx + rightDelimiter.length();
            } else {
                break;
            }
        }
        newSbd.append(oldSbd.substring(p, oldSbd.length()));
        return cleanXSS(newSbd.toString());
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<Integer> splitIdStr(String idStr) {
        String[] ids = idStr.split(",");
        Set<Integer> idSet = new HashSet<>();
        for (String id : ids) {
            String temp = id.trim();
            if (temp.isEmpty() || Integer.parseInt(temp) <= 0) {
                continue;
            }
            idSet.add(Integer.valueOf(temp));
        }
        return idSet;
    }

    public static void writeJson(HttpServletResponse httpResponse, ResultModel<?> result) {
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        try {
            httpResponse.getWriter().print(JsonUtils.objectToJson(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否为API请求
     */
    public static boolean isApiRequest(HttpServletRequest httpRequest) {
        return httpRequest.getHeader("Accept") == null ||
                !httpRequest.getHeader("Accept").contains("text/html");
    }

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
     * <a href="https://www.cnblogs.com/death00/p/11557305.html">...</a>
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
