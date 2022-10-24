<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ERROR</title>
    
    <style>
        body{
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .error-container{
            width: 800px;
            height: 300px;
            text-align: center;
        }

        .error-container > h1{
            margin-bottom: 50px;
        }

        .error-container-title{
            text-align: left;
            font-weight: bold;
        }

        .btn-area{
            text-align: center;
        }

    </style>
</head>
<body>
    <div class="error-container">
        <h1>404 NOT FOUND(또 오타났지? 고쳐라라는 뜻)</h1>

        <h3>해당 요청에 대한 페이지가 존재하지 않습니다.</h3>

        <img src="/resources/images/error.png">

        <div class="btn-area">
            <a href="/">메인 페이지</a>
        </div>
    </div>
</body>
</html>