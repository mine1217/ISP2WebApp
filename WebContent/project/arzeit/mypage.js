var xmlHttpRequest;
var beforeText = "";


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

		if (response.code == 0) {
			var showElement = document.getElementById("username");
			showElement.value = response.name;
			var showElement = document.getElementById("userid");
			showElement.value = response.id;
		} else {
			alert(getErrorMessage(response.code));
			if (response.code = 30) {//ユーザーデータが無い旨のコード
				location.href = "login.html"; //リダイレクトしてログインからやり直してもらう
			}
		}
	}
}

/**
 * サーバーにプロフィールの更新命令出す
 */
function sendUpdateRequest() {
	var url = "mypageUpdate";

	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkUpdateRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	let message = `updateId=${document.getElementById("userid").value}&updateName=${document.getElementById("username").value}`;
	xmlHttpRequest.send(message);
}

function enableFocus(ele) {
	beforeText = ele.value;
	ele.focus();
}

function unableFocus(ele) {
	if (ele.id == "userid") { //id変更した場合はフォーマット確認する
		const idRegulex = /^[A-Za-z0-9_]{6,100}$/i;
		if (!idRegulex.test(ele.value)) {
			ele.value = beforeText; //だめだったら戻す
			alert(getErrorMessage(2));
		}
	}
	if (ele.value == "") {
		ele.value = beforeText; //ブランクだったら戻す
	}
}

/**
 * ↑のレスポンス対応
 */
function checkUpdateRequest() {
	if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
		var response = JSON.parse(xmlHttpRequest.responseText);

		if (response.code == 0) { //成功したらそんまま
			alert("変更完了しました");
		} else { //エラー出たらメッセージ出して表示を更新する
			alert(getErrorMessage(response.code));
			sendShowRequest();
		}
	}
}

window.addEventListener("load", function () {
	sendShowRequest();
	var getButtonElement = document.getElementById("update_button");
	getButtonElement.addEventListener("click", sendUpdateRequest, false);
}, false);
