<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
    <h1>/memo/list PAGE</h1>

    <div class="page_info">
        <div>"현재 페이지 번호 : " ${page.number} </div>
        <div>"한페이지에 표시할 건수 : " ${page.size} </div>
        <div>"총게시물 개수 : " ${page.totalElements} </div>
        <div>"총페이지 개수 : " ${page.totalPages} </div>
        <div>"첫번째 페이지인지 여부 : " ${page.isFirst()} </div>
        <div>"다음페이지가 있는지 여부 : " ${page.hasNext()} </div>
        <div>"이전페이지가 있는지 여부 : " ${page.hasPrevious()} </div>
    </div>
    <hr/>
    <div>
        <c:forEach var="memo"   items="${list}">
            <div>
                ${memo}
            </div>
        </c:forEach>
    </div>

    <hr/>
    <!-- 페이징 처리 블럭 -->
    <div>
        <!-- 이전 페이지 버튼 표시 여부 -->
        <c:if test="${pageBlock.prev}" >
            <a href="/memo/list?pageNo=${pageBlock.startPage-1}" > ◀ </a>
        </c:if>

        <!-- 페이징 블럭 내 표시 숫자 반복 -->
        <c:forEach var="i" begin="${pageBlock.startPage}" end="${pageBlock.endPage}">
            <a href="/memo/list?pageNo=${i}">${i}</a>_
        </c:forEach>

        <!-- 다음 페이지 버튼 표시 여부 -->
        <c:if test="${pageBlock.next}" >
            <a href="/memo/list?pageNo=${pageBlock.endPage+1}" > ▶ </a>
        </c:if>
    </div>
</body>
</html>
