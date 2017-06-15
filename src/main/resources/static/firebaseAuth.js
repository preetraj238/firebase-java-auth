var app = angular.module('firebaseAuth', ['ui.bootstrap']);

app.controller('ctrl', function($scope) {

    var config = {
        apiKey: "AIzaSyA1xU9VIYWSSiqs7lwid-BZtk74jTotBkI",
        authDomain: "userauth-a35f8.firebaseapp.com",
        databaseURL: "https://userauth-a35f8.firebaseio.com",
        projectId: "userauth-a35f8",
        storageBucket: "userauth-a35f8.appspot.com",
        messagingSenderId: "657104007725"
    };
    firebase.initializeApp(config);

    $scope.login = function(email,password){
    console.log("hello");
        firebase.auth().signInWithEmailAndPassword(email, password).then(function(result) {
          var token = result.refreshToken;
          var message = "Successfully logged in with token: " + token;
           $scope.$apply(function () {
              $scope.message = message;
              $scope.email='';
              $scope.password='';
              $scope.loginError=false;
           });
          console.log(result);
        }).catch(function(error) {
          var errorCode = error.code;
          var errorMessage = error.message;
          var message = "Error occurred while logging in: " + errorMessage;
          $scope.$apply(function () {
               $scope.message = message;
               $scope.loginError=true;
            });
          console.log(errorMessage);
          var email = error.email;
          var credential = error.credential;
          console.log(credential);
        });
    };

    $scope.clear = function(){
        $scope.message = null;
        $scope.user = null;
    };

    $scope.toggleOption = function(option){
        $scope.optionSelected=option;
        if(!option){
            $scope.clear();
        }
    };

    $scope.loginUsingFaceBook = function(){
        var provider = new firebase.auth.FacebookAuthProvider();
        firebase.auth().signInWithPopup(provider).then(function(result) {
          var token = result.credential.accessToken;
          $scope.$apply(function () {
             $scope.user = result.user;
         });
          console.log($scope.user);
        }).catch(function(error) {
          var errorCode = error.code;
          var errorMessage = error.message;
          console.log(errorMessage);
          var email = error.email;
          var credential = error.credential;
          console.log(credential);
          $scope.$apply(function () {
            $scope.user = "Error while logging in";
           });
        });
    };

    $scope.loginUsingGoogle = function(){
        var provider = new firebase.auth.GoogleAuthProvider();
        firebase.auth().signInWithPopup(provider).then(function(result) {
          var token = result.credential.accessToken;
          $scope.$apply(function () {
             $scope.user = result.user;
         });
          console.log($scope.user);
        }).catch(function(error) {
          var errorCode = error.code;
          var errorMessage = error.message;
          console.log(errorMessage);
          var email = error.email;
          var credential = error.credential;
          console.log(credential);
          $scope.$apply(function () {
            $scope.user = "Error while logging in";
           });
        });
        }

});