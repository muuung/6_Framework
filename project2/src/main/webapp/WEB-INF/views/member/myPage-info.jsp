<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 문자열 관련 메서드를 제공하는 JSTL(EL형식) --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/resources/css/main-style.css">
    <link rel="stylesheet" href="/resources/css/myPage-style.css">

    <!--  fontawesome 사이트 아이콘 이용   -->
    <script src="https://kit.fontawesome.com/f7459b8054.js" crossorigin="anonymous"></script>
</head>
<body>
    <main>
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        
        <section class="myPage-content">
            <jsp:include page="/WEB-INF/views/member/sideMenu.jsp" />

            <section class="myPage-main">

                <h1 class="myPage-title">내 정보</h1>
                <span class="myPage-subject">원하는 회원 정보를 수정할 수 있습니다.</span>
            
                <%-- 절대경로 : /member/myPage/info --%>
                <%-- 상대경로 : info --%>
                <form action="info" method="POST" name="myPage-frm">

                    <div class="myPage-row">
                        <label>닉네임</label>
                        <input type="text" name="memberNickname" value="${loginMember.memberNickname}" maxlength="10">
                    </div>

                    <div class="myPage-row">
                        <label>전화번호</label>
                        <input type="text" name="memberTel" value="${loginMember.memberTel}" maxlength="11">
                    </div>

                    <%-- split(문자열, 구분자) : 문자열을 구분자로 쪼개서 배열로 반환 --%>
                    <c:set var="addr" value="${fn:split(loginMember.memberAddress, ',,')}" />

                    <div class="myPage-row info-title">
                        <span>주소</span>
                    </div>

                    <div class="myPage-row info-address">
                        <input type="text" name="memberAddress" 
                        id="sample6_postcode" placeholder="우편번호" maxlength="6" value="${addr[0]}">
                        <button type="button" onclick="sample6_execDaumPostcode()">검색</button>
                    </div>

                    <div class="myPage-row info-address">
                        <input type="text" name="memberAddress"
                        id="sample6_address" placeholder="도로명/지번 주소" value="${addr[1]}">
                    </div>

                    <div class="myPage-row info-address">
                        <input type="text" name="memberAddress"
                        id="sample6_detailAddress" placeholder="상세 주소" value="${addr[2]}">
                    </div>

                    <button class="myPage-submit">수정하기</button>
                </form>
            </section>
        </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    
    <script>
        function sample6_execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var addr = ''; // 주소 변수
                    var extraAddr = ''; // 참고항목 변수

                    //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        addr = data.roadAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        addr = data.jibunAddress;
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('sample6_postcode').value = data.zonecode;
                    document.getElementById("sample6_address").value = addr;

                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById("sample6_detailAddress").focus();
                }
            }).open();
        }
    </script>

    <!-- myPage.js external 방식으로 추가 -->
    <script src="/resources/js/member/myPage.js"></script>
</body>
</html>