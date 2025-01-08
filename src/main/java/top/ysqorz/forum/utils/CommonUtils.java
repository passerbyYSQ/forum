package top.ysqorz.forum.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.http.MediaType;
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

    /**
     * 请使用#{@link cn.hutool.core.util.URLUtil#encodeAll(String)}
     *
     * @deprecated
     */
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
            httpResponse.getWriter().print(JsonUtils.objToJson(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否为API请求
     */
    public static boolean isApiRequest(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true; // ajax异步请求，返回json
        }
        String acceptStr = request.getHeader("Accept");
        if (ObjectUtils.isEmpty(acceptStr)) {
            return true; // accept为空则默认返回json
        }
        List<MediaType> acceptList = MediaType.parseMediaTypes(acceptStr);
        MediaType jsonType = null, htmlType = null;
        for (MediaType mediaType : acceptList) {
            if (MediaType.APPLICATION_JSON.equalsTypeAndSubtype(mediaType)) {
                jsonType = mediaType;
            }
            if (MediaType.TEXT_HTML.equalsTypeAndSubtype(mediaType)) {
                htmlType = mediaType;
            }
        }
        if ((ObjectUtils.isEmpty(jsonType) && ObjectUtils.isEmpty(htmlType)) || ObjectUtils.isEmpty(htmlType)) {
            // 同时为空 或者 jsonType不为空且htmlType为空 时返回true
            return true;
        } else if (ObjectUtils.isEmpty(jsonType)) { // json为空，html不为空则返回html
            // jsonType为空且htmlType不为空 时返回false
            return false;
        } else {
            // jsonType和htmlType都不为空时，比较权重。如果jsonType的权重比htmlType的权重更大时，则返回true
            return Double.compare(jsonType.getQualityValue(), htmlType.getQualityValue()) > 0;
        }
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
     * 请使用#{@link cn.hutool.extra.servlet.ServletUtil#getClientIP(HttpServletRequest, String...)}
     *
     * <a href="https://www.cnblogs.com/death00/p/11557305.html">...</a>
     *
     * @deprecated
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

    public static boolean isVirtualInterface(NetworkInterface netInterface) throws SocketException {
        return netInterface.getName().contains("veth") ||
                netInterface.getName().contains("docker") ||
                netInterface.isLoopback() ||
                netInterface.isPointToPoint() ||
                netInterface.isVirtual();
    }

    public static String getLocalHostStr() {
        return getLocalHost().getHostAddress();
    }

    public static InetAddress getLocalHost() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress candidate = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (!netInterface.isUp() || isVirtualInterface(netInterface)) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ipAddr = addresses.nextElement();
                    if (!(ipAddr instanceof Inet4Address)) {
                        continue;
                    }
                    if (!ipAddr.isSiteLocalAddress()) {
                        // 非地区本地地址，指10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255
                        return ipAddr;
                    } if (Objects.isNull(candidate)) {
                        // 取第一个匹配的地址
                        candidate = ipAddr;
                    }
                }
            }
            if (Objects.nonNull(candidate)) {
                return candidate;
            }
            return InetAddress.getLocalHost();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
