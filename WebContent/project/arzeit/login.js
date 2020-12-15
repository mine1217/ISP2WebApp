/**
 * @author Uenobo
 * loginページにて認証機能を実装
 */

var xmlHttpRequest;
var idElements;
var passElements;
var submitElement;

const hashSeed = "nanntoka kantoka unnunn kannunn";


function sendLoginMethod() {
  document.getElementById("errormessage").innerHTML = "<font color=brown>ログインしています...</font>";
  const shaObj = new jsSHA("SHA-256", "TEXT", { encoding: "UTF8" });
  shaObj.update(passElements.value);
  shaObj.update(hashSeed);
  var pass = shaObj.getHash("HEX");

  var url = "login";
  xmlHttpRequest = new XMLHttpRequest();
  xmlHttpRequest.onreadystatechange = receive;
  xmlHttpRequest.open("POST", url, true);
  xmlHttpRequest.setRequestHeader("Content-Type",
    "application/x-www-form-urlencoded");
  xmlHttpRequest.send("id=" + idElements.value + "&pass=" + pass);

}


function receive() {
  if (xmlHttpRequest.readyState == 4) {
    if (xmlHttpRequest.status == 200) {
      var response = JSON.parse(xmlHttpRequest.responseText);

      if (response.code == 0) {//成功コードが帰ってきたら　

        alert("ログイン完了しました。 Arzeitへようこそ！");

        location.href = "main.html"; //リダイレクトする

      } else {//違ったらエラーメッセージ出す
        document.getElementById("errormessage").innerHTML = getErrorMessage(response.code);
      }
    } else {
      document.getElementById("errormessage").innerHTML = getErrorMessage(50);
    }
  } 

}

window.addEventListener("load", function () {
  idElements = document.getElementById("id");
  passElements = document.getElementById("pass");
  submitElement = document.getElementById("submit")
  submitElement.addEventListener("click", sendLoginMethod, true);
}, false);
