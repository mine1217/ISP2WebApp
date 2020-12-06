let isSelectDate = false; //日付を選択しているか
let isSelectSchedule = false; //スケジュールを選択しているか

var scheduleList = []; //現在表示しているスケジュールのリスト
var dateList = []; //選択中の日付のリスト
var s_idList = []; //選択中のスケジュールのリスト

var xmlHttpRequest;

const MAX_SCHEDULE_VIEW = 3;

/**
 * スケジュールオブジェクト
 * @param {*} s_id スケジュールのid　一意
 * @param {*} start 開始時間
 * @param {*} end 終了時間
 * @param {*} saraly お給料
 */
function schedule(s_id, start, end, saraly) {
  this.s_id = s_id;
  this.start = start;
  this.end = end;
  this.saraly = saraly;
}

/**
 * 日付クリックしたときに呼び出される
 * @param {*} sell
 */
function getDate(sell) {
  var sell_id = sell.id;

  if (!isSelectSchedule) {

    if (toggleDate(sell.id) == 0) {
      isSelectDate = false;
      document.getElementById("set_form").style.display = 'none';
    } else {
      isSelectDate = true;
      document.getElementById("set_form").style.display = 'inline';
    }

  }
}

/**
 * 日付クリック処理
 * @param {*} dateId
 */
function toggleDate(dateId) {
  var isInclude = false;
  var sellElement = document.getElementById("SELL_" + dateId);

  var count = 0;
  dateList.forEach(element => {
    if (element == dateId) {
      dateList.splice(count, 1);
      sellElement.style.backgroundColor = "#48d1cc";
      console.log("削除");
      isInclude = true;
    };
    count++;
  })

  if (!isInclude) {
    console.log("追加");
    dateList.push(dateId);
    sellElement.style.backgroundColor = "#37a4bf";
  }
  return dateList.length;
}

/**
 * スケジュール押したときに呼び出される
 * @param {*}} sell
 */
function getSchedule(sell) {
  var sell_id = sell.id;

  if (!isSelectDate) {

    var count = toggleSchedule(sell.id)
    if (count == 0) {
      isSelectSchedule = false;
      document.getElementById("update_form").style.display = 'none';
    } else {
      isSelectSchedule = true;
      document.getElementById("update_form").style.display = 'inline';
      var startH = document.getElementById("edit_starthour");
      var startM = document.getElementById("edit_startmin");
      var endH = document.getElementById("edit_endhour");
      var endM = document.getElementById("edit_endmin");

      if (count = 1) { //複数選択では時間設定できないようにする
        startH.disabled = false;
        startM.disabled = false;
        endH.disabled = false;
        endM.disabled = false;
      } else {
        startH.disabled = true;
        startM.disabled = true;
        endH.disabled = true;
        endM.disabled = true;
      }
    }

  }
}

/**
 * スケジュールクリック処理
 * @param {*} dateId
 */
function toggleSchedule(s_Id) {
  var isInclude = false;
  var sellElement = document.getElementById(s_Id);

  var count = 0;
  s_idList.forEach(element => {
    if (element == s_Id) {
      s_idList.splice(count, 1);
      sellElement.style.backgroundColor = "rgba(0,0,0,0)";
      console.log("削除");
      isInclude = true;
    };
    count++;
  })

  if (!isInclude) {
    console.log("追加");
    s_idList.push(s_Id);
    sellElement.style.backgroundColor = "#37a4bf";
  }
  return s_idList.length;
}

function addSchedule() {
  var startH = document.getElementById("apply_starthour");
  var startM = document.getElementById("apply_startmin");
  var endH = document.getElementById("apply_endhour");
  var endM = document.getElementById("apply_endmin");
  var saralyValue = document.getElementById("apply_saraly").value;

  if (saralyValue == "") {
    saralyValue = "0";
  }

  var code = checkTimeFormat(startH, startM, endH, endM);
  if (code == 0) {

    let message = {
      operation: "add",
      dateList: dateList,
      start: `${('00' + startH.value).slice(-2)}:${('00' + startM.value).slice(-2)}:00`,
      end: `${('00' + endH.value).slice(-2)}:${('00' + endM.value).slice(-2)}:00`,
      saraly: saralyValue
    };

    sendUpdateRequest(message);

  } else {
    alert(getErrorMessage(code));
  }

}

function updateSchedule() {
  var startH = document.getElementById("edit_starthour");
  var startM = document.getElementById("edit_startmin");
  var endH = document.getElementById("edit_endhour");
  var endM = document.getElementById("edit_endmin");
  var saralyValue = document.getElementById("edit_saraly").value;

  if (saralyValue == "") {
    saralyValue = "0";
  }

  var code = checkTimeFormat(startH, startM, endH, endM);
  if (code == 0) {
    var dateList = [];

    s_idList.forEach(id => {
      dateList.push(document.getElementById(id).parentElement.parentElement.id.slice(4));
    }
    );

    let message = {
      operation: "update",
      s_idList: s_idList,
      dateList: dateList,
      start: `${('00' + startH.value).slice(-2)}:${('00' + startM.value).slice(-2)}:00`,
      end: `${('00' + endH.value).slice(-2)}:${('00' + endM.value).slice(-2)}:00`,
      saraly: saralyValue
    };

    sendUpdateRequest(message);
  } else {
    alert(getErrorMessage(code));
  }
}

function deleteSchedule() {
  let message = {
    operation: "delete",
    s_idList: s_idList
  };

  sendUpdateRequest(message);
}

/**
 * 時間設定が不正でないかチェック
 * @param {*} startH
 * @param {*} startM
 * @param {*} endH
 * @param {*} endM
 */
function checkTimeFormat(startH, startM, endH, endM) {
  code = 0;
  if (Number(startH.value) > Number(endH.value)) {//start:6:00,end:2:00
    code = 21;
  }
  else if (Number(startH.value) == Number(endH.value) && Number(startM.value) > Number(endM.value)) {//start:6:30,end:6:00
    code = 21;
  }
  else if (Number(startH.value) == Number(endH.value) && Number(startM.value) == Number(endM.value)) {//start:6:00,end:6:00
    code = 21;
  }
  
  return code;
}

/**
 * スケジュールを読み込むときに送る
 */
function sendShowRequest() {
  var url = "main";
  xmlHttpRequest = new XMLHttpRequest();
  xmlHttpRequest.onreadystatechange = checkShowRequest;
  xmlHttpRequest.open("POST", url, true);
  xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  xmlHttpRequest.send(`day=${year}-${month}-1`);
}

/**
 * スケジュールを更新するときに送る
 * @param {}} message 送るメッセージ
 */
function sendUpdateRequest(message) {
  var url = "mainUpdate";
  xmlHttpRequest = new XMLHttpRequest();
  xmlHttpRequest.onreadystatechange = checkUpdateRequest;
  xmlHttpRequest.open("POST", url, true);
  xmlHttpRequest.setRequestHeader("Content-Type", "application/json");
  xmlHttpRequest.send(JSON.stringify(message));
}


/**
 * sednShowRequestに対するレスポンス対応メソッド
 */
function checkShowRequest() {
  if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {

    console.log(xmlHttpRequest.responseText);
    var response = JSON.parse(xmlHttpRequest.responseText);
    if (response.code == 0) {
      scheduleList = response.schedule;
      updateView();

    } else {

      alert(getErrorMessage(response.code));
      if (response.code = 30) {//ユーザーデータが無い旨のコード

        location.href = "login.html"; //リダイレクトしてログインからやり直してもらう

      }
    }
  }
}

/**
 * sendUpdateRequestに対するレスポンス対応メソッド
 */
function checkUpdateRequest() {
  if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {

    console.log(xmlHttpRequest.responseText);
    var response = JSON.parse(xmlHttpRequest.responseText);
    if (response.code == 0) {
      sendShowRequest(); //更新が成功したらまたスケジュールを取ってきて更新する
    } else {
      alert(getErrorMessage(response.code));
    }
  }
}

/**
 * 月収を表示する
 */
function showMonthlyIncome() {
  var sum = 0;
  var workMin = 0;
  scheduleList.forEach(element => {
    workMin = calcWorkTime(element.start.split(" ")[1].slice(0, 5), element.end.split(" ")[1].slice(0, 5));
    new Date(month)
    sum += Number(element.saraly) * workMin;
  })
  document.getElementById("income").innerHTML = sum;
}

/**
 * 時間差を計算して返す
 * @param {*} from 開始時間
 * @param {*} to 終了時間
 */
function calcWorkTime(from, to) {

  var Y = new Date().getFullYear()
  var M = new Date().getMonth() + 1
  var D = new Date().getDate()

  var ymd = Y + "/" + M + "/" + D + "/"

  var fromTime = new Date(ymd + " " + from).getTime()
  var toTime = new Date(ymd + ' ' + to).getTime()
  var Ms = toTime - fromTime

  var h = ''
  var m = ''

  h = Ms / 3600000
  m = (Ms - h * 3600000) / 6000
  return h + m;
}

/**
 * カレンダーを更新する
 */
function updateView() {
  //日付やスケジュールの選択状況をリセットする
  isSelectDate = false;
  isSelectSchedule = false;
  dateList = [];
  s_idList = [];
  document.getElementById("update_form").style.display = 'none';
  document.getElementById("set_form").style.display = 'none';
  //月収表示
  showMonthlyIncome();

  //こっからスケジュール表示
  let str;
  let ele;
  let start, end;
  let message = "予定";
  reloadCalender();
  scheduleList.forEach((schedule) => {
    start = schedule.start.split(' '); //スタート時間を日付と時間で分ける
    end = schedule.end.split(' ');　//終了時間を日付と時間で分ける
    if (schedule.saraly != "null") message = `<font color=tomato>${schedule.saraly}/h</font>`

    ele = document.getElementById("SCH_" + start[0]); //スタート時間の日付のえれめんと取ってくる

    var html = `
    <tr class="testTD"  id = "${schedule.s_id}" onclick="getSchedule(this);" style="background-color:rgba(0,0,0,0);">
      <td class="scheduleTD">
        <p class="testFONT">
          ${start[1].slice(0, 5)}-${end[1].slice(0, 5)}
          <br>
          ${message}
        </p>
      </td>
    </tr>
    `; //スケジュールのボタンhtml

    if (ele.childElementCount != 0) { //スケジュールの最大数を越えるとはみ出すので条件分岐
      if (ele.childElementCount >= MAX_SCHEDULE_VIEW) {
        document.getElementById("UNUM_"+start[0]).innerHTML = `<font size=1 color=black><br>+more</font>`;
      } else {
        ele.innerHTML += html;
        document.getElementById("UNUM_"+start[0]).innerHTML = "";
      }
    } else {
      ele.innerHTML += html;
      document.getElementById("UNUM_"+start[0]).innerHTML = "";
    }
  });
}

window.addEventListener("load", function () {
  sendShowRequest();
}, false);
