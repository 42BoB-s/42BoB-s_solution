
var menuSet = {
    '0' : 'all',
    '1' : 'korea',
    '2' : 'china',
    '3' : 'us'
};

var menuSet_kor = {
    '0' : '모두',
    '1' : '한식',
    '2' : '중식',
    '3' : '양식'
}

var locationSet = {
	'1' : 'gaepo',
	'2' : 'seocho'
};

var locationSet_kor = {
	'1' : '개포',
	'2' : '서초'
};


function calljson() {
	var menus = myMSG;
	var succeedSet = document.getElementById('succeed');
	var activeSet = document.getElementById('active');
	for (var menudata in menus) {
		var newbutton = document.createElement("div");
		newbutton.setAttribute('class', 'col-8 col-sm-5 col-lg-4 col-xl-4 col-md-4 col-xxl-4 mb-5');
		if (menus[menudata].room_status === 'succeed') {
			newbutton.innerHTML = "<div class='card bg-light border-0 h-100'><button class='mymenubutton'' value='" + menus[menudata].category_name + "|" + menus[menudata].enter_at + "|" + menus[menudata].room_id + "'></br><h2 class='fs-4 fw-bold'>" + menus[menudata].category_name + " 먹어요!</h2></br><p class='mb-0'>" + menus[menudata].enter_at + "~</br>" + menus[menudata].participants + "</p></br></button></div>";
			succeedSet.appendChild(newbutton);
		} else if (menus[menudata].room_status === 'active') {
			newbutton.innerHTML = "<div class='card bg-light border-0 h-100'><button class='mymenubutton' onclick='cancelRoom(this)' value='" + menus[menudata].category_name + "|" + menus[menudata].enter_at + "|" + menus[menudata].room_id + "'></br><h2 class='fs-4 fw-bold'>" + menus[menudata].category_name + " 먹어요!</h2></br><p class='mb-0'>" + "~</br>" + menus[menudata].enter_at + menus[menudata].participants + "</p></br></button></div>";
			activeSet.appendChild(newbutton);
		}
	}
}

function menuSelect(self) {
	var selectors = document.getElementsByClassName("menuselector");
	var i = 0;
	while (i < selectors.length) {
		if (selectors[i].value != self.value)
			selectors[i].style.backgroundColor = "rgb(249, 249, 249)";
		++i;
	}
	document.getElementById(menuSet[self.value]).style.backgroundColor = "rgb(171,227,228)";
}

function locationSelect(self) {
	var selectors = document.getElementsByClassName("locationselector");
	var i = 0;
	while (i < selectors.length) {
		if (selectors[i].value != self.value)
			selectors[i].style.backgroundColor = "rgb(249, 249, 249)";
		++i;
	}
	document.getElementById(locationSet[self.value]).style.backgroundColor = "rgb(171,227,228)";
}

function getFormData() {
	var tt = new Date;
	var curTime = tt.getFullYear()+'-'+fillZero((tt.getMonth() + 1))+'-'+fillZero(tt.getDate())+' '+fillZero(tt.getHours())+':'+fillZero(tt.getMinutes())+':00';
	var timeFrom = timeConvert(document.getElementsByName("meetTimeFrom")[0].value, tt);
	var timeTo = timeConvert(document.getElementsByName("meetTimeTo")[0].value, tt);
	var menu_html = document.querySelector('input[name="menu"]:checked');
	var menu = menu_html.value;
	var location_html = document.querySelector('input[name="location"]:checked');
	var location = location_html.value;
	var tt = new Date;
	var curTime = tt.getFullYear()+'-'+fillZero((tt.getMonth() + 1))+'-'+fillZero(tt.getDate())+' '+fillZero(tt.getHours())+':'+fillZero(tt.getMinutes())+':00';
	//$(document).ready(function(){
		//  $('submit').click(f)
		//})
	var noTime = timeConvert('', tt);
	if (timeFrom === noTime) {
		alert('시작 시간을 선택해주세요');
	} else if (timeTo === noTime) {
		alert('마지막 시간을 선택해주세요');
	} else if (timeTo < timeFrom) {
		alert('마지막 시간이 시작 시간보다 빠릅니다');
	} else if (confirm(timeFrom + "시부터 " + timeTo + "까지 " + locationSet_kor[location] + "에서 " + menuSet_kor[menu] + " 예약을 하시겠습니까?")) {
		if (timeFrom < curTime) {
			alert('시작 시간이 현재 시간보다 이릅니다');
		} else {
			var json_data = {
				id: myID,
				menu: menu,
				timeFrom: timeFrom,
				timeTo: timeTo,
				location: location
			}
			$.ajax({
				url: "enter"
				, method: "POST"
				, dataType: "text"
				, data: JSON.stringify(json_data)
				, contentType: "application/json; charset=UTF-8"
				, success: function (data) {
					if (data == "false")
						alert("방 생성 및 참여에 실패했습니다");
					else
						alert("등록이 완료되었습니다.");
					window.location.replace("http://localhost:8080/main");
				}
				, error: function (request, status, error) {
					alert('서버에 예상치 못한 에러가 발생하였습니다. 잠시 후 다시 시도해주시길 바랍니다.');
					alert('code:' + request.status+ '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
				}
			})
		}
	}
}

function cancelRoom(self) {
	var value = self.value;
	var sep = value.split('|');
	var menu = sep[0];
	var time = sep[1];
	var room_id = sep[2];
	var json_data = {
		id : myID,
		menu : menu,
		time : time,
		room_id : room_id
	}

	if (confirm(time + "시에 있는 " + menu + " 예약을 취소하시겠습니까?")) {
		$.ajax({
			url: "cancel"
			, method: "POST"
			, dataType: "text"
			, data: JSON.stringify(json_data)
			, contentType: "application/json; charset=UTF-8"
			, success: function () {
				alert("약속이 취소되었습니다.");
				window.location.replace("http://localhost:8080/main");
			}
			, error: function (request, status, error) {
				alert('서버에 예상치 못한 에러가 발생하였습니다. 잠시 후 다시 시도해주시길 바랍니다.');
				alert('code:' + request.status+ '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
			}
		})
	}
}

function fillZero(str) {
	return ('0' + str).slice(-2);
}

function timeConvert(str, tt) {
	str = tt.getFullYear()+'-'+fillZero((tt.getMonth() + 1))+'-'+fillZero(tt.getDate())+' '+str+':00';
	return str;
}

