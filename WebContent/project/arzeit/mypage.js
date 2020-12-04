var xmlHttpRequest;

function postMethod() {
	var url = "mypage";

	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = receive;
	xmlHttpRequest.open("POST", url, true);
  xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send(null);
}

function receive() {
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
		console.log(xmlHttpRequest.responseText);
		var response = JSON.parse(xmlHttpRequest.responseText);

		var showElement = document.getElementById("username");
		showElement.innerHTML = response.name;
    var showElement = document.getElementById("userid");
    showElement.innerHTML = response.id;
	}
}

window.addEventListener("load",function(){
  postMethod();

  //編集機能追加する時(今はいらん)
  //var getButtonElement = document.getElementById("update_button");
  //getButtonElement.addEventListener("click", postWithGetMethod, false);

},false);
