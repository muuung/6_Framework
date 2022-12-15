<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!-- map에 저장된 값을 꺼내어 각각 변수에 저장 -->
<c:set var="boardList" value="${map.boardList}" />
<c:set var="pagination" value="${map.pagination}" />
<c:set var="boardType" value="${boardTypeList}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${boardType[boardCode-1].BOARD_NAME}</title>

    <link rel="stylesheet" href="/resources/css/main-style.css">
    <link rel="stylesheet" href="/resources/css/board/boardList-style.css">

    <script src="https://kit.fontawesome.com/f7459b8054.js" crossorigin="anonymous"></script>
</head>
<body>
    <main>
        <jsp:include page="/WEB-INF/views/common/header.jsp"/>

        <!-- 검색을 진행한 경우 -->
        <c:if test="${not empty param.key}">
            <%-- /board/1?cp=3&key=t&query=테스트 --%>
            <c:set var="sURL" value="&key=${param.key}&query=${param.query}" />
        </c:if>
        
        <section class="board-list">

            <h1 class="board-name">${boardType[boardCode-1].BOARD_NAME}</h1>

            <!-- <c:choose>
                <c:when test="${boardCode == 1}"><h1 class="board-name">${boardType[0].BOARD_NAME}</h1></c:when>
                <c:when test="${boardCode == 2}"><h1 class="board-name">${boardType[1].BOARD_NAME}</h1></c:when>
                <c:when test="${boardCode == 3}"><h1 class="board-name">${boardType[2].BOARD_NAME}</h1></c:when>
                <c:when test="${boardCode == 4}"><h1 class="board-name">${boardType[3].BOARD_NAME}</h1></c:when>
            </c:choose> -->

            <div class="list-wrapper">
                <table class="list-table">
                    
                    <thead>
                        <tr>
                            <th>글번호</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>조회수</th>
                            <th>좋아요</th>
                        </tr>
                    </thead>

                    <tbody>
                   		<!-- 게시글 목록 조회 결과가 비어있다면 -->
                        <c:choose>
                            <c:when test="${empty boardList}">
                                <tr>
                                    <th colspan="6">게시글이 존재하지 않습니다.</th>
                                </tr>
                            </c:when>

                            <c:otherwise>
                                <c:forEach var="board" items="${boardList}">
                                    <tr>
                                        <td>${board.boardNo}</td>
                                        <td> 
                                            <c:if test="${not empty board.thumbnail}">
                                                <img class="list-thumbnail" src="${board.thumbnail}">
                                            </c:if>
            
                                            <a href="/board/${boardCode}/${board.boardNo}?cp=${pagination.currentPage}${sURL}">${board.boardTitle}</a>
                                            [${board.commentCount}]                        
                                        </td>
                                        <td>${board.memberNickname}</td>
                                        <td>${board.boardCreateDate}</td>
                                        <td>${board.readCount}</td>
                                        <td>${board.likeCount}</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

						<!-- 게시글 목록 조회 결과가 있지 않다면 -->
                    </tbody>
                </table>
            </div>


            <div class="btn-area">

				<!-- 로그인 상태일 경우 글쓰기 버튼 노출 -->
                <c:if test="${not empty loginMember}">
                    <button id="insertBtn">글쓰기</button>                     
                </c:if>

            </div>


            <div class="pagination-area">


                <ul class="pagination">
                
                    <!-- 첫 페이지로 이동 -->
                    <li><a href="/board/${boardCode}?cp=1${sURL}">&lt;&lt;</a></li>

                    <!-- 이전 목록 마지막 번호로 이동 -->
                    <li><a href="/board/${boardCode}?cp=${pagination.prevPage}${sURL}">&lt;</a></li>

					<c:forEach var="i" begin="${pagination.startPage}"
                    end="${pagination.endPage}" step="1">

                        <c:choose>
                            <c:when test="${i == pagination.currentPage}">
                                <!-- 현재 페이지인 경우 -->
                                <li><a class="current">${i}</a></li>
                            </c:when>

                            <c:otherwise>
                                <li><a href="/board/${boardCode}/?cp=${i}${sURL}">${i}</a></li>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>

                    <!-- 특정 페이지로 이동 -->
                    
                    <!-- 현재 보고있는 페이지 -->
                    <!-- <li><a class="current">1</a></li> -->
                    
                    <!-- 현재 페이지를 제외한 나머지 -->
                    <!-- <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li><a href="#">6</a></li>
                    <li><a href="#">7</a></li>
                    <li><a href="#">8</a></li>
                    <li><a href="#">9</a></li>
                    <li><a href="#">10</a></li> -->
                    
                    <!-- 다음 목록 시작 번호로 이동 -->
                    <li><a href="/board/${boardCode}?cp=${pagination.nextPage}${sURL}">&gt;</a></li>

                    <!-- 끝 페이지로 이동 -->
                    <li><a href="/board/${boardCode}?cp=${pagination.maxPage}${sURL}">&gt;&gt;</a></li>

                </ul>
            </div>


			<!-- 검색창 -->
            <form action="${boardCode}" method="get" id="boardSearch" onsubmit="return true">

                <select name="key" id="search-key">
                    <option value="t">제목</option>
                    <option value="c">내용</option>
                    <option value="tc">제목+내용</tion>
                    <option value="w">작성자</option>
                </select>

                <input type="text" name="query"  id="search-query" placeholder="검색어를 입력해주세요.">

                <button>검색</button>
            </form>

        </section>
    </main>
    
    
    <!-- 썸네일 클릭 시 모달창 출력 -->
    <div class="modal">
        <span id="modal-close">&times;</span>
        <img id="modal-image" src="/resources/images/board/20221116105843_00001.gif">
    </div>


    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script>
        const boardCode = "${boardCode}";
    </script>
    <script src="/resources/js/board/boardList.js"></script>
</body>
</html>