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
    if(response.code == 0) {
      alert("ログイン完了しました。 Arzeitへようこそ！")
      var redirect_url = "main.html" + location.search;
      if (document.referrer) {
        var referrer = "referrer=" + encodeURIComponent(document.referrer);
        redirect_url = redirect_url + (location.search ? '&' : '?') + referrer;
      }
      location.href = redirect_url;
    }else {
      document.getElementById("errormessage").innerHTML = getErrorMessage(response.code);
    }
  }
}
//
//
// function sendWithGetMethod(){
//   var url = "";
//   xmlHttpRequest = new XMLHttpRequest();
//   xmlHttpRequest.onreadystatechange = getResponse;
//   xmlHttpRequest.open("GET",url,true);
//   xmlHttpRequest.send(null);
//
// }
//
// function getResponse(){
//   var response = JSON.parse(xmlHttpRequest.responseText);
//
// 	var idElement = document.getElementById("id");
// 	idElement.innerHTML = response.id;
//
// 	var passElement = document.getElementById("pass");
// 	passElement.innerHTML = response.pass;
//
// }

window.addEventListener("load", function() {
  idElements = document.getElementById("id");
  passElements = document.getElementById("pass");
  submitElement = document.getElementById("submit")
  submitElement.addEventListener("click", sendLoginMethod, true);
}, false);
