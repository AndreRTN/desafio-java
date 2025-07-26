<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciamento de Portfólio</title>
    <link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/styles.css" rel="stylesheet">
    <script src="webjars/jquery/1.9.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="/js/utils.js"></script>
    <script src="/js/projeto.js"></script>
    <script src="/js/projeto-membro.js"></script>
</head>
<body>
<div class="container">
    <div id="toast" class="toast">
        <span id="toast-message"></span>
        <button type="button" class="close" onclick="hideToast()" style="margin-left: 10px;">&times;</button>
    </div>

    <div class="page-header">
        <h1>Sistema de Gerenciamento de Portfólio</h1>
    </div>
</div>