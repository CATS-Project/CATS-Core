<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Welcome !</title>
    <jsp:include page="head.jsp"/>
    <meta http-equiv="refresh" content="3;${authUrl}"/>
</head>
<body>
<jsp:include page="../pages/navbar.jsp"/>
<div class="container">
    <div class="row">
        <div class="col s12">
            We need to associate your twitter account with your CATS account. So please login to your twitter account.
            You will be redirect in <span id="seconds">3</span> seconds...
            <span id="error"></span>
            <script>
                (function () {
                    var element = document.getElementById('seconds');
                    var error = document.getElementById('error');
                    var num = +element.innerText;
                    var func = function () {
                        num--;
                        if (num >= 0) {
                            element.innerText = '' + num;
                        }
                        if (num < -1){
                            error.innerText = 'Your connection is slow, please wait.';
                        }
                        setTimeout(func, 1000);
                    };
                    setTimeout(func, 1000);
                })();
            </script>
        </div>
    </div>
</div>
</body>
</html>