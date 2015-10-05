<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Please Login</title>
        <link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="../css/bootstrap-theme.min.css" />
        <style type="text/css">
            body { margin-top: 25vh; background-color:#eee; }
            .form-signin .form-control {
                position: relative;
                height: auto;
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                box-sizing: border-box;
                padding: 10px;
                font-size: 16px;
            }
            .form-signin input[type="email"] {
                margin-bottom: -1px;
                border-bottom-right-radius: 0;
                border-bottom-left-radius: 0;
            }
            .form-signin input[type="password"] {
                margin-bottom: 10px;
                border-top-left-radius: 0;
                border-top-right-radius: 0;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
                <c:if test="${not empty error}" >
                    <div class="row">
                        <div class="col-xs-12 col-md-4 col-md-push-4 alert alert-danger" role="alert">Error: Wrong Username/Password</div>
                    </div>
                </c:if>
                <c:if test="${not empty msg}">
                    <div class="row">
                        <div class="col-xs-12 col-md-4 col-md-push-4 alert alert-success" role="alert">Successfully Logged Out</div>
                    </div>
                </c:if>
            <div class="row">
                <div class="col-xs-12 col-md-4 col-md-push-4 panel panel-default">
                    <div class="panel-body">
                        <form action="/login" method="POST" class="form-signin">
                            <h2 class="form-signin-heading">Please Sign In</h2>
                            <label for="username" class="sr-only">Email address</label>
                            <input type="text" id="username" name="username" class="form-control" placeholder="Email address" required autofocus>
                            <label for="password" class="sr-only">Password</label>
                            <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" value="remember-me"> Remember me
                                </label>
                            </div>
                            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript" src="../js/jquery-1.11.3.min.js"></script>
        <script type="text/javascript" src="../js/bootstrap.min.js"></script>
    </body>
</html>