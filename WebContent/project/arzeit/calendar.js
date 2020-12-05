const weeks = ['日', '月', '火', '水', '木', '金', '土']
const date = new Date()
let year = date.getFullYear()
let month = date.getMonth() + 1
let onclick = [];
const config = {
    show: 1,
}

function showCalendar(year, month) {
    for ( i = 0; i < config.show; i++) {
        const calendarHtml = createCalendar(year, month)
        const sec = document.createElement('section')
        sec.innerHTML = calendarHtml
        document.querySelector('#calendar').appendChild(sec)

        month++
        if (month > 12) {
            year++
            month = 1
        }
    }
}

function createCalendar(year, month) {
    const startDate = new Date(year, month - 1, 1) // 月の最初の日を取得
    const endDate = new Date(year, month,  0) // 月の最後の日を取得
    const endDayCount = endDate.getDate() // 月の末日
    const lastMonthEndDate = new Date(year, month - 2, 0) // 前月の最後の日の情報
    const lastMonthendDayCount = lastMonthEndDate.getDate() // 前月の末日
    const startDay = startDate.getDay() // 月の最初の日の曜日を取得
    let dayCount = 1 // 日にちのカウント
    let calendarHtml = '' // HTMLを組み立てる変数

    calendarHtml += '<h3>' + year  + '/' + month + '</h3>'
    calendarHtml += '<table class="testTable">'

    // 曜日の行を作成
    for (let i = 0; i < weeks.length; i++) {
        calendarHtml += '<td class="testTR">' + weeks[i] + '</td>'
    }

    for (let w = 0; w < 6; w++) {
        calendarHtml += '<tr class="testTR">'

        for (let d = 0; d < 7; d++) {
            if (w == 0 && d < startDay) {
                // 1行目で1日の曜日の前
                let num = lastMonthendDayCount - startDay + d + 1
                calendarHtml += '<td class="is_disabled">' + num + '</td>'
            } else if (dayCount > endDayCount) {
                // 末尾の日数を超えた
                let num = dayCount - endDayCount
                calendarHtml += '<td class="is_disabled">' + num + '</td>'
                dayCount++
            } else {
                let paddingID = `${( '0000' + year ).slice( -4 )}-${( '00' + month ).slice( -2 )}-${( '00' + dayCount ).slice( -2 )}`
                calendarHtml +=
                `<td class="testSELLTD" id="SELL_${paddingID}">
                    <table class="testInlineTable">
                        <tr class="testTD">
                            <td class="testNUMTD" id="${paddingID}" onclick="getDate(this);"> ${dayCount}<a id=UNUM_${paddingID}><a></td>
                            <td class="testTD">
                                <table class="testTable" id="SCH_${paddingID}">
                                </table>
                            </td>
                        </tr>
                    </table>
                    </li>
                </td>`
                dayCount++
            }
        }
        calendarHtml += '</tr>'
    }
    calendarHtml += '</table>'

    return calendarHtml
}

function moveCalendar(e) {
    document.querySelector('#calendar').innerHTML = ''

    if (e.target.id === 'lastMonth') {
        month--

        if (month < 1) {
            year--
            month = 12
        }
    }

    if (e.target.id === 'nextMonth') {
        month++

        if (month > 12) {
            year++
            month = 1
        }
    }

    showCalendar(year, month);
    sendShowRequest();
}

function reloadCalender() {
    document.querySelector('#calendar').innerHTML = '';
    showCalendar(year, month);
}

document.querySelector('#lastMonth').addEventListener('click', moveCalendar)
document.querySelector('#nextMonth').addEventListener('click', moveCalendar)

showCalendar(year, month)
