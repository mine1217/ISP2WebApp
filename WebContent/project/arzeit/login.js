var xmlHttpRequest;
var idElements;
var passElements;
var submitElement;

function login(){
  if(6<=idElements.value.length<=24&&idElements.value.match(/^[A-Za-z_0-9]+$/)){

    if(6<=passElements.value.length<=24&&passElements.value.match(/^[A-Za-z_0-9]+$/)){
      document.getElementById("errormessage").innerHTML = "";
    }
    else {
      //エラー文表示
      document.getElementById("errormessage").innerHTML = "passwordのフォーマットが合っていません";
      console.log("passwordのフォーマットが合っていません");
    }

  }
  else {
    //エラー文表示
    document.getElementById("errormessage").innerHTML = "idのフォーマットが合っていません";
    console.log("idのフォーマットが合っていません");
  }
  sendWithPostMethod();

}

function sendWithPostMethod(){
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

var url = "echo";
xmlHttpRequest = new XMLHttpRequest();
xmlHttpRequest.onreadystatechange = receive;
xmlHttpRequest.open("POST",url,true);
xmlHttpRequest.setRequestHeader("Content-Type",
"application/x-www-form-urlencoded");
xmlHttpRequest.send("id=" + idElements.value + "&pass=" + passElements.value);

}


function receive(){
  if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
    var response = JSON.parse(xmlHttpRequest.responseText);

    var echoMessageElement = document.getElementById("echo_id");
    echoMessageElement.innerHTML = response.id + response.pass ;
  }
}


function sendWithGetMethod(){
  var url = "";
  xmlHttpRequest = new XMLHttpRequest();
  xmlHttpRequest.onreadystatechange = getResponse;
  xmlHttpRequest.open("GET",url,true);
  xmlHttpRequest.send(null);

}

function getResponse(){
  var response = JSON.parse(xmlHttpRequest.responseText);

	var idElement = document.getElementById("id");
	idElement.innerHTML = response.id;

	var passElement = document.getElementById("pass");
	passElement.innerHTML = response.pass;

}

window.addEventListener("load", function() {
  idElements = document.getElementById("id");
  passElements = document.getElementById("pass");
  submitElement = document.getElementById("submit")
	submitElement.addEventListener("click", login, false);
}, false);
