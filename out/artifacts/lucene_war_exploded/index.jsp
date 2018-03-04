<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>千度一下，你就知道</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
<body>
<form action="/index.do" method="post">
    <input name="num" value="1" type="hidden"/>
    <input name="keywords" maxlength="50"/>
    <input type="submit" value="千度一下"/>
</form>
<c:if test="${!empty page.list}">
<br>
<br>
千度为您找到相关结果约${page.rowCount}个
<br>
<br>
<c:forEach items="${page.list}" var="hb">
    <a href="${hb.url}">${hb.title}</a>
    <br>
    <p>${hb.content}</p>
    <br>
    ${hb.url}
    <br>
    <br>
</c:forEach>
    <c:if test="${page.hasPrevious}" >
        <a href="index.do?num=${page.previousPageNum}&keywords=${keywords}" >上一页</a>
    </c:if>
    <c:forEach begin="${page.everyPageStart}" end="${page.everyPageEnd}" var="current">
        <c:choose >
            <c:when test="${current eq page.pageNum}">
                <a ><font color="#a52a2a">${current}</font></a>
            </c:when>
            <c:otherwise>
                <a href="index.do?num=${current}&keywords=${keywords}">${current}</a>
            </c:otherwise>
        </c:choose>
        &nbsp;&nbsp;&nbsp;
    </c:forEach>
    <c:if test="${page.hasNext}" >
        <a href="index.do?num=${page.nextPageNum}&keywords=${keywords}">下一页</a>
    </c:if>
</c:if>
</body>
</html>
