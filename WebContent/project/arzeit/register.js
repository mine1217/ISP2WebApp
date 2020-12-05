/**
 * @author Uenobo
 * registerページにて登録機能を実装
 */

var xmlHttpRequest;
var idElements;
var pass1Elements;
var pass2Elements;
var nameElements;
var submitElement;

const idRegulex = /^[A-Za-z0-9_]{6,100}$/i;
const passRegulex = /^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\d)[A-Za-z0-9_\d]{8,100}$/; //正規表現

function register() {
  var code = 0//
  if (idRegulex.test(idElements.value)) {
    if (pass1Elements.value == pass2Elements.value) {
      if (passRegulex.test(pass1Elements.value)) {
        document.getElementById("errormessage").innerHTML = "<fonr color=yellow>登録処理実行中...</font>";
        sendWithPostMethod();
      }
      else {
        code = 11;
      }
    } else {
      code = 12;
    }
  }
  else {
    code = 2;
  }
  if (code != 0) {
    document.getElementById("errormessage").innerHTML = getErrorMessage(code);
  }

}

function sendWithPostMethod() {
  //passを暗号化
  //     var CryptoJS =  require('crypto-js');
  //
  //     var pwd =  "erHt4Mb8s";
  //
  //     var encryptedPass = CryptoJS.AES.encrypt(pass1Elements,pwd).toString();
  //
  //     var url = "echo";
  //     xmlHttpRequest = new XMLHttpRequest();
  //     xmlHttpRequest.onreadystatechange = receive;
  //     xmlHttpRequest.open("POST",url,true);
  //     xmlHttpRequest.setRequestHeader("Content-Type",
  //     "application/x-www-form-urlencoded");
  //     xmlHttpRequest.send("id=" + idElements.value + "&pass=" + encryptedPass
  //       + "&name=" + nameElements.value);

  var url = "register";
  xmlHttpRequest = new XMLHttpRequest();
  xmlHttpRequest.onreadystatechange = receive;
  xmlHttpRequest.open("POST", url, true);
  xmlHttpRequest.setRequestHeader("Content-Type",
    "application/x-www-form-urlencoded");
  xmlHttpRequest.send("id=" + idElements.value + "&pass=" + pass1Elements.value+ "&name=" + nameElements.value);

}

function receive() {
  if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
    var response = JSON.parse(xmlHttpRequest.responseText);
    if(response.code == 0) {
      document.getElementById("errormessage").innerHTML = "<font color=green>登録完了</font>";

      alert("登録成功しました！ アカウント登録ありがとうございます!");

      location.href = "login.html"; //リダイレクトする

    }else {
      document.getElementById("errormessage").innerHTML = getErrorMessage(response.code);
    }
  }
}

window.addEventListener("load", function () {
  idElements = document.getElementById("id");
  pass1Elements = document.getElementById("pass1");
  pass2Elements = document.getElementById("pass2");
  nameElements = document.getElementById("name");
  submitElement = document.getElementById("register_button")
  submitElement.addEventListener("click", register, false);
}, false);
