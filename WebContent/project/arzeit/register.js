var xmlHttpRequest;
var idElements;
var pass1Elements;
var pass2Elements;
var nameElements;
var submitElement;

function register(){

  if(6<=idElements.value.length<=24&&idElements.value.match(/^[A-Za-z_0-9]+$/)){

    if(pass1Elements.value===pass2Elements.value){

      if(6<=pass1Elements.value.length<=24&&pass1Elements.value.match(/^[A-Za-z_0-9]+$/)){
        document.getElementById("errormessage").innerHTML = "";
      }
      else {

        //エラー文表示
        document.getElementById("errormessage").innerHTML = "passwordのフォーマットが合っていません";
        console.log("passwordのフォーマットが合っていません");

      }

    }else {
      document.getElementById("errormessage").innerHTML = "passwordが一致しません";
      console.log("passwordが一致しません");
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

var url = "../../../src/project/arzeit/controller/RegisterServlet.java";
xmlHttpRequest = new XMLHttpRequest();
xmlHttpRequest.onreadystatechange = receive;
xmlHttpRequest.open("POST",url,true);
xmlHttpRequest.setRequestHeader("Content-Type",
"application/x-www-form-urlencoded");
xmlHttpRequest.send("id=" + idElements.value + "&pass=" + pass1Elements.value
  + "&name=" + nameElements.value);

}

function receive(){
  if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
    var response = JSON.parse(xmlHttpRequest.responseText);

    var echoMessageElement = document.getElementById("echo_id");
    echoMessageElement.innerHTML = response.id + response.pass +response.name;
  }
}

window.addEventListener("load", function() {
  idElements = document.getElementById("id");
  pass1Elements = document.getElementById("pass1");
  pass2Elements = document.getElementById("pass2");
  nameElements = document.getElementById("name");
  submitElement = document.getElementById("submit")
	submitElement.addEventListener("click", register, false);
}, false);
