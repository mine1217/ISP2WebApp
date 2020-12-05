/**
 * @author Uenobo
 * loginページにて認証機能を実装
 */

var xmlHttpRequest;
var idElements;
var passElements;
var submitElement;


function sendLoginMethod(){

var url = "login";
xmlHttpRequest = new XMLHttpRequest();
xmlHttpRequest.onreadystatechange = receive;
xmlHttpRequest.open("POST",url,true);
xmlHttpRequest.setRequestHeader("Content-Type",
"application/x-www-form-urlencoded");
xmlHttpRequest.send("id=" + idElements.value + "&pass=" + passElements.value);

}


function receive(){
  if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
    var response = JSON.parse(xmlHttpRequest.responseText);

    if(response.code == 0) {//成功コードが帰ってきたら　

      alert("ログイン完了しました。 Arzeitへようこそ！");

      var redirect_url = "main.html" + location.search;
      if (document.referrer) {
        var referrer = "referrer=" + encodeURIComponent(document.referrer);
        redirect_url = redirect_url + (location.search ? '&' : '?') + referrer;
      }
      location.href = redirect_url; //リダイレクトする

    }else {//違ったらエラーメッセージ出す
      document.getElementById("errormessage").innerHTML = getErrorMessage(response.code);
    }

  }
}

window.addEventListener("load", function() {
  idElements = document.getElementById("id");
  passElements = document.getElementById("pass");
  submitElement = document.getElementById("submit")
  submitElement.addEventListener("click", sendLoginMethod, true);
}, false);
