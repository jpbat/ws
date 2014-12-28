<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
  <link rel="stylesheet" href="stylesheets/custom.css">
  <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Ubuntu:regular,bold&subset=Latin">
  <link rel="stylesheet" type="text/css" href="stylesheets/Spinner.css">

  <script data-main="javascript/main.js" src="javascript/libs/require.js"></script>

</head>
<body>
<button onclick="history.back();" class="nav-button btn btn-warning"><span class="glyphicon glyphicon-arrow-left"></span> Back</button>
<div class="alertContainer">

</div>
<div class="hidden" id="alertContainer">
  <div class="alert alert-danger alert-dismissible fade in">
    <button type="button" class="close" data-dismiss="alert">×</button>
    <h4 class="alert-heading"></h4>
    <p></p>
  </div>
</div>

<div class="hidden" id="SpinnerContainer">
  <div class="spinner">
    <div class="spinner-container container1">
      <div class="circle1"></div>
      <div class="circle2"></div>
      <div class="circle3"></div>
      <div class="circle4"></div>
    </div>
    <div class="spinner-container container2">
      <div class="circle1"></div>
      <div class="circle2"></div>
      <div class="circle3"></div>
      <div class="circle4"></div>
    </div>
    <div class="spinner-container container3">
      <div class="circle1"></div>
      <div class="circle2"></div>
      <div class="circle3"></div>
      <div class="circle4"></div>
    </div>
  </div>
</div>
<div id="BackBoneContainer" class="container"></div>

</body>
</html>
