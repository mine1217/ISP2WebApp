let isSelectDate = false;
let isSelectSchedule = false;

var scheduleList = [];
var dateList = [];

var xmlHttpRequest;

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
 * スケジュールクリック処理
 * @param {*} dateId 
 */
function toggleSchedule(dateId) {
  var isInclude = false;
  var sellElement = document.getElementById(dateId);

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

function sendShowRequest() {
  var url = "main";
	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkSendRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send("message=" + encodeURIComponent(messageElement.value));
}



window.addEventListener("load", function () {
  //document.getElementById("apply_button").addEventListener("click", sendSendRequest, false);
}, false);


