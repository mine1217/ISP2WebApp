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
  var sellElement = document.getElementById("SELL_"+dateId);

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

      if(count = 1) { //複数選択では時間設定できないようにする
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

function sendShowRequest() {
  var url = "main";
	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkShowRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send(`day=${year}-${month}-1`);
}

function sendAddRequest() {
  var startH = document.getElementById("apply_starthour");
  var startM = document.getElementById("apply_startmin");
  var endH = document.getElementById("apply_endhour");
  var endM = document.getElementById("apply_endmin");
  var saraly = document.getElementById("apply_saraly");

  var code = checkTimeFormat(startH, startM, endH, endM);
  if(code == 0) {
    
    let message = {
      operator: "add",
      dateList: dateList,
      start: `${( '00' + startH.value ).slice( -2 )}:${( '00' + startM.value ).slice( -2 )}:00`,
      end: `${( '00' + endH.value ).slice( -2 )}:${( '00' + endM.value ).slice( -2 )}:00`,
      saraly: saraly.value
    };

    console.log(message);

  } else {
    alert(getErrorMessage(code));
  }

  var url = "main";
	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkShowRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/json");
	xmlHttpRequest.send(JSON.stringify(message));
}

function sendUpdateRequest() {
  var startH = document.getElementById("edit_starthour");
  var startM = document.getElementById("edit_startmin");
  var endH = document.getElementById("edit_endhour");
  var endM = document.getElementById("edit_endmin");
  var saraly = document.getElementById("edit_saraly");

  var code = checkTimeFormat(startH, startM, endH, endM);
  if(code == 0) {

  } else {
    alert(getErrorMessage(code));
  }
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
  if(startH.value < endH.value) {
    if(startM.value < endM.value) {
     code = 21;
    }
  }
  return code;
}


function checkShowRequest() {
  if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {

		console.log(xmlHttpRequest.responseText);
		var response = JSON.parse(xmlHttpRequest.responseText);
		if (response.code == 0) {
      scheduleList = response.schedule;
      updateView();

		} else {

      alert(getErrorMessage(response.code));
      if(response.code = 30) {//ユーザーデータが無い旨のコード

        var redirect_url = "login.html" + location.search; //loginページへ遷移 loginし直してもらう
        if (document.referrer) {
          var referrer = "referrer=" + encodeURIComponent(document.referrer);
          redirect_url = redirect_url + (location.search ? '&' : '?') + referrer;
        }
        location.href = redirect_url; //リダイレクトする

      }
    }
	}
}

function updateView() {
  let str;
  let ele;
  let start, end;
  let message = "予定";
  scheduleList.forEach((schedule) => {
    start = schedule.start.split(' '); //スタート時間を日付と時間で分ける
    end = schedule.end.split(' ');　//終了時間を日付と時間で分ける
    if(schedule.saraly != "null") message = `<font color=tomato>${schedule.saraly}/h</font>`

    ele = document.getElementById("SCH_"+start[0]); //スタート時間の日付のえれめんと取ってくる
    if(ele.childElementCount <= MAX_SCHEDULE_VIEW) { //スケジュールの最大数を越えるとはみ出すので条件分岐
      ele.innerHTML += `
      <tr class="testTD"  id = "${schedule.s_id}" onclick="getSchedule(this);" style="background-color:rgba(0,0,0,0);"> 
        <td class="scheduleTD"> 
          <p class="testFONT">
            ${start[1].slice(0,5)}-${end[1].slice(0,5)}
            <br>
            ${message}
          </p> 
        </td> 
      </tr>
      `;
    }
  });
}

function addSchedule() {
  sendAddRequest();
}

function deleteSchedule() {
  sendDeleteRequest();
}

function updateSchedule() {
  sendDeleteRequest();
}

window.addEventListener("load", function () {
    sendShowRequest();
}, false);


