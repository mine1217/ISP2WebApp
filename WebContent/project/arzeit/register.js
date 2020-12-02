var xmlHttpRequest;
var idElements = document.getElementsByClassName("id");
var pass1Elements = document.getElementsByClassName("pass1");
var pass2Elements = document.getElementsByClassName("pass2");
var nameElements = document.getElementsByClassName("name");


function register(){
  if(0<idElements.length<=12){

  }
  else {
    //エラー文表示
    //document.write("idのフォーマットが合っていません");
  }

  if(pass1Elements===pass2Elements){
    if(6<=pass1Elements.length<=24&&pass1Elements.match(/^[A-Za-z_0-9]+$/)){

    }
    else {
      //エラー文表示
      //document.write("passwordのフォーマットが合っていません");
    }
  }
  else{
    //エラー文表示
    //document.write("passwordが一致しません");
  }

  if(6<=nameElements.length<=24&&nameElements.match(/^[A-Za-z_0-9]+$/)){

  }
  else{
    //エラー文表示
  }

  sendWithGetMethod();

}

function sendWithPostMethod(){
  //passを暗号化
    var CryptoJS =  require('crypto-js');

    var pwd =  "erHt4Mb8s";

    var encryptedPass = CryptoJS.AES.encrypt(pass1Elements,pwd).toString();

    var url = "echo";
    xmlHttpRequest = new xmlHttpRequest();
    xmlHttpRequest.onreadystatechange = receive;
    xmlHttpRequest.open("POST",url,true);
    xmlHttpRequest.setRequestHeader("Content-Type",
    "application/x-www-form-urlencoded");
    xmlHttpRequest.send("id=" + idElements.value + "&pass=" + encryptedPass
      + "&name=" + nameElements.value);
}

window.addEventListener("load", function() {
	var submitElement = document.getElementById("submit");
	submitElement.addEventListener("click", register, false);
}, false);
