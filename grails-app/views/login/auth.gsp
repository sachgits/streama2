<%@ page import="grails.converters.JSON" %>
<%@ page import="streama.Settings" %>
<!doctype html>
<html lang="en" class="no-js">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<title>${Settings.findByName('title').value}</title>
	<meta name="viewport" content="width=device-width, initial-scale=1"/>

	<style type="text/css">
	[ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
		display: none !important;
	}
	</style>

	<asset:stylesheet src="vendor.css"/>
	<asset:stylesheet src="application.css"/>

  <g:linkRelIconSetting setting="${Settings.findByName('favicon').value}"></g:linkRelIconSetting>

	<script type="text/javascript">
		window.contextPath = "${request.contextPath}";
	</script>
</head>

<body >
  <g:cssBackgroundSetting selector=".login-page" setting="${Settings.findByName('loginBackground').value}"></g:cssBackgroundSetting>
	<div class="page-container login-page">
    <div id='login' ng-app="streama.auth" class="ng-cloak" ng-controller="authController">
            
      <g:imgSetting class="auth-logo"  setting="${Settings.findByName('logo').value}" alt="${streama.Settings.findByName('title').value} Logo"></g:imgSetting>
			<div class='inner'>

      <g:if test='${flash.message}'>
			  <div class='login_message'>${flash.message}</div>
		</g:if>
        <p class="info__description">
            Watch all your favorite movies,tv shows/series and music straight to your mobile phone
            or smart tv
        </p>
      <p class="box__description">
          Pay using your Mpesa to paybill 234480,your mobile as account No.
      </p>

        <form action='${postUrl}' method='POST' id='loginForm' class='cssform form-horizontal' autocomplete='off'>

          <div class="form-group">
            <div class="col-lg-12">
              <input type="text" name="username" class="form-control" placeholder="Saf Phonenumber">
            </div>
          </div>

          <div class="form-group">
            <div class="col-lg-12">
              <input type="password" name='password' class="form-control" placeholder="{{'LOGIN.PASSWORD' | translate}}">
            </div>
          </div>
          <span>
            <g:if test="${streama.Settings.findBySettingsKey('First Time Login Info')?.value == 'true'}">
              {{'LOGIN.FIRST_TIME_HINT' | translate}}
            </g:if>
            <input style="display: none;" type='checkbox' name='remember_me' id='remember_me' checked='checked'/>
            <button class="btn btn-primary pull-left" ><a href="http://192.168.1.100:3000/register" >Sign Up</a> </button>
            <button class="btn btn-primary pull-right">{{'LOGIN.SUBMIT' | translate}} &nbsp; <i class="ion-chevron-right"></i></button></span>
        </form>
      </div>
    </div>
    <div class="page-container-push"></div>
  </div>

  <g:render template="/templates/footer"></g:render>


	<asset:javascript src="vendor.js" />
	<asset:javascript src="/streama/streama.translations.js" />

  <script type='text/javascript'>
    <!--
    (function() {
      document.forms['loginForm'].elements['username'].focus();
    })();

    angular.module('streama.auth', ['streama.translations', 'LocalStorageModule']);

    angular.module('streama.auth').controller('authController', function ($translate, localStorageService) {

      localStorageService.remove('currentProfile');
      
      var sessionExpired = ${params.sessionExpired?"true":"false"};
      if(sessionExpired){
        alertify.log($translate.instant('LOGIN.SESSION_EXPIRED'));
      }
      function registerLink(){
          location.href = "http://" + location.hostname + ":3000/register";
      }
    })
    // -->
  </script>


  <g:googleAnalytics/>

</body>
</html>
