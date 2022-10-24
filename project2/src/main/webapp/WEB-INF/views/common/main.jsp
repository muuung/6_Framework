<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>수업용 프로젝트</title>
    <link rel="stylesheet" href="/resources/css/main-style.css">
    <script src="https://kit.fontawesome.com/f7459b8054.js" crossorigin="anonymous"></script>
</head>
<body>
    <!-- 
        기존 영역 분할에 사용한 div, span 태그는
        태그의 이름만 봤을 때 나눈다는 것 이외의 의미를 파악할 수 없음
        id 또는 class 속성을 필수적으로 추가해야하는 번거로움이 있음

        이러한 문제점을 해결하고자
        태그의 이름만으로 어느정도 어떤 역할을 하는 지 알 수 있고
        추가적으로 웹 접근성 향상에 도움이 되는
        시맨틱 태그(Semantic Tag, 의미있는 태그)가 HTML5에서 추가됨

        시맨틱 태그는 div 태그의 이름만 바뀐 것으로 생각하는게 좋음

        시맨틱 태그 종류
        <main> : 현재 문서의 주된 콘텐츠를 작성하는 영역
        <header> : 문서의 제목, 머리말 영역
        <footer> : 문선의 하단 부분, 꼬리말, 정보 작성 영역
        <nav> : 나침반 역할(다른 ㅔㅍ이지, 사이트 이동)의 링크 작성 영역
        <aside> : 사이드바, 광고 등을 표시하는 양쪽 영역
        <section> : 구역을 구분하기 위한 영역
        <article> : 본문과 독립된 콘텐츠를 작성하는 영역
     -->

     <main>
        <%-- header.jsp 추가(포함) --%>
        <%--
            jsp 액션 태그 중 include
            해당 위치에 page 속성으로 지정된 jsp 파일의 내용이 포함됨
            jsp 파일의 경로는 /webapp 폴더를 기준으로 작성
        --%>
        <jsp:include page="/WEB-INF/views/common/header.jsp"/>
        
        <section class="content">
            <section class="content-1">${loginMember}</section>
            <section class="content-2">
                <%-- 로그인 여부에 따라 출력 화면 변경 --%>
                <c:choose>

                    <%-- 로그인 X인 경우 --%>
                    <c:when test="${empty sessionScope.loginMember}">
                        <form action="/member/login" name="login-frm" method="POST">
                        
                            <%-- 아이디, 비밀번호, 로그인 버튼 --%>
                            <fieldset id="id-pw-area">
                                <section>
                                    <%-- autocomplete="off" : 자동완성 사용 X --%>                          <%-- 쿠키 중 saveId에 저장된 값 --%>
                                    <input type="text" name="inputEmail" placeholder="이메일" autocomplete="off" value="${cookie.saveId.value}">
                                    <input type="password" name="inputPw" placeholder="비밀번호">
                                </section>

                                <section>
                                    <!-- type="submin"이 기본값 -->
                                    <button>로그인</button>
                                </section>
                            </fieldset>
                
                            <%-- 쿠키에 saveId가 있을 경우 --%>
                            <c:if test="${!empty cookie.saveId.value}">
                                <%-- temp 변수 선언 --%>
                                <c:set var="temp" value="checked"/>
                                <%-- page scope == page 어디서든 사용 가능 
                                                == if문 나가도 쓸 수 있음 --%>
                            </c:if>

                            <!-- label 태그 내부에 input 태그를 작성하면 자동 연결 -->
                            <label>
                                <input type="checkbox" name="saveId" ${temp}> 아이디 저장
                            </label>
                
                            <!-- 회원가입/ID/PW 찾기 -->
                            <article id="signUp-find-area">
                                <a href="/member/signUp">회원가입</a>
                                <span>|</span>
                                <a href="#">ID/PW찾기</a>
                            </article>
                        </form>
                    </c:when>

                    <%-- 로그인 O인 경우 --%>
                    <c:otherwise>
                        <article class="login-area">

                            <%-- 회원 프로필 이미지 --%>
                            <a href="#">
                                <img id = "member-profile" src="/resources/images/ribbit.png">
                            </a>
                            
                            <%-- 회원 정보 + 로그아웃 버튼 --%>
                            <div class="my-info">
                                <div>
                                    <a href="#" id="nickname">${loginMember.memberNickname}</a>
                                    <a href="/member/logout" id="logout-btn">로그아웃</a>
                                </div>
                                
                                <p>${loginMember.memberEmail}</p>

                            </div>
                        </article>
                    </c:otherwise>
                </c:choose>
            </section>
        </section>
     </main>

    <%-- footer.jsp 포함 --%>
    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>