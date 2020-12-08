var xmlHttpRequest;


/**
 * サーバーからデータ取ってきて表示する
 */
function sendShowRequest() {
	var url = "mypage";

	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkShowRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send(null);
}

/**
 * ↑のレスポンス対応
 */
function checkShowRequest() {
	if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
		var response = JSON.parse(xmlHttpRequest.responseText);

		var showElement = document.getElementById("username");
		showElement.innerHTML = response.name;
		var showElement = document.getElementById("userid");
		showElement.innerHTML = response.id;
	}
}

/**
 * サーバーにプロフィールの更新命令出す
 */
function sendUpdateRequest() {
	var url = "mypageUpdate";

	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = receive;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	let message = `
	updateId=${document.getElementById("userid")}
	&updateName=${document.getElementById("username")}`
	xmlHttpRequest.send(message);
}

/**
 * ↑のレスポンス対応
 */
function checkUpdateRequest() {
	if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
		var response = JSON.parse(xmlHttpRequest.responseText);

		if(response.code == 0) { //成功したらそんまま
			alert("変更完了しました");
		} else { //エラー出たらメッセージ出して表示を更新する
			alert(getErrorMessage(code));
			sendShowRequest();
		}
	}
}

window.addEventListener("load", function () {
	sendShowRequest();
	var getButtonElement = document.getElementById("update_button");
	getButtonElement.addEventListener("click", sendUpdateRequest, false);
}, false);
