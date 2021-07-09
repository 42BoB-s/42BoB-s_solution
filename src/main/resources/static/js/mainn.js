
var menuSet_kor = {
    '0' : '모두',
    '1' : '한식',
    '2' : '중식',
    '3' : '양식'
}

var locationSet_kor = {
	'1' : '서초',
	'2' : '개포'
};

function menuButtonTimeConvert(time) {
	var sep = time.split(':');
	return (sep[0] + ':' + sep[1]);
}

function calljson() {
	var menus = myMSG;
	var succeedSet = document.getElementById('succeed');
	var activeSet = document.getElementById('active');
	for (var menudata in menus) {
		var newbutton = document.createElement("div");
		newbutton.setAttribute('class', 'col-8 col-sm-5 col-lg-4 col-xl-4 col-md-4 col-xxl-4 mb-5');
		if (menus[menudata].room_status === 'succeed') {
			newbutton.innerHTML = "<div class='card bg-light border-0 h-100'><button disabled class='mymenubutton' value='" + menus[menudata].category_name + "|" + menus[menudata].enter_at + "|" + menus[menudata].room_id + "'></br><h2 class='fs-4 fw-bold'>" + menus[menudata].category_name + " 먹어요!</h2></br><p class='mb-0'>" + menuButtonTimeConvert(menus[menudata].enter_at) + "~</br>" + menus[menudata].location_name + "</br>" + menus[menudata].participants + "</p></br></button></div>";
			succeedSet.appendChild(newbutton);
		} else if (menus[menudata].room_status === 'active') {
			newbutton.innerHTML = "<div class='card bg-light border-0 h-100'><button class='mymenubutton' onclick='cancelRoom(this)' value='" + menus[menudata].category_name + "|" + menus[menudata].enter_at + "|" + menus[menudata].room_id + "'></br><h2 class='fs-4 fw-bold'>" + menus[menudata].category_name + " 먹어요!</h2></br><p class='mb-0'>" + menuButtonTimeConvert(menus[menudata].enter_at) + "~</br>" + menus[menudata].location_name + "</br>" + menus[menudata].participants + "</p></br></button></div>";
			activeSet.appendChild(newbutton);
		}
	}
}

function getFormData() {
	var tt = new Date;
	var timeTo = timeConvert(document.getElementsByName("meetTime")[0].value, tt);
	var menu_html = document.querySelector('input[name="menu"]:checked');
	var menu = menu_html.value;
	var location_html = document.querySelector('input[name="location"]:checked');
	var location = location_html.value;
	var noTime = timeConvert('', tt);
	if (timeTo === noTime) {
		swal({
			title: "시간을 입력해주세요!",
			text: "최소 5분 후 예약부터 가능합니다.",
			icon: "warning",
		});
	} else {
		swal({
			title : "예약하시겠습니까?",
			text : '시간 :' + timeTo + '\n위치 : ' + locationSet_kor[location] + '\n메뉴 : ' + menuSet_kor[menu],
			icon : 'info',
			buttons : ['아니오', '예'],
		}).then(function(isConfirm) {
			if (isConfirm) {
				tt = new Date;
				tt.setMinutes(tt.getMinutes() + 5);
				var curTime = tt.getFullYear()+'-'+fillZero((tt.getMonth() + 1))+'-'+fillZero(tt.getDate())+' '+fillZero(tt.getHours())+':'+fillZero(tt.getMinutes())+':00';
				if (timeTo < curTime) {
					swal({
						title: "방 생성에 실패했습니다.",
						text: "최소 5분 후 예약부터 가능합니다.",
						icon: "error",
					});
				} else {
					var json_data = {
						id: myID,
						menu: menu,
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
							if (data == 1)
								swal({
									title: "방 생성에 실패했습니다.",
									text: "한 시간 단위로 예약해주시길 바랍니다.",
									icon: "error",
								}).then(() => {window.location.replace("http://localhost:8080/main");});
							else if (data == 2)
								swal({
									title: "방 생성에 실패했습니다.",
									text: "같은 시간에 예약이 있습니다.",
									icon: "error",
								}).then(() => {window.location.replace("http://localhost:8080/main");});
							else
								swal({
									title: "방이 생성되었습니다!",
									text: "잠시만 기다리면 밥동료가 찾아옵니다.",
									icon: "success",
								}).then(() => {window.location.replace("http://localhost:8080/main");});
						}
						, error: function (request, status, error) {
							alert('서버에 예상치 못한 에러가 발생하였습니다. 잠시 후 다시 시도해주시길 바랍니다.');
							alert('code:' + request.status+ '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
						}
					})
				}
			}
		});
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
	swal({
		title : "취소하시겠습니까?",
		text : '시간 :' + menuButtonTimeConvert(time) + '\n메뉴 : ' + menu,
		icon : 'info',
		buttons : ['아니오', '예'],
	}).then(function(isConfirm) {
		if (isConfirm) {
			$.ajax({
				url: "cancel"
				, method: "POST"
				, dataType: "text"
				, data: JSON.stringify(json_data)
				, contentType: "application/json; charset=UTF-8"
				, success: function () {
					swal({
						title: "약속이 취소되었습니다!",
						icon: "success",
					}).then(() => {window.location.replace("http://localhost:8080/main");});
				}
				, error: function (request, status, error) {
					alert('서버에 예상치 못한 에러가 발생하였습니다. 잠시 후 다시 시도해주시길 바랍니다.');
					alert('code:' + request.status + '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
				}
			})
		}
	});
}

function fillZero(str) {
	return ('0' + str).slice(-2);
}

function timeConvert(str, tt) {
	str = tt.getFullYear()+'-'+fillZero((tt.getMonth() + 1))+'-'+fillZero(tt.getDate())+' '+str+':00';
	return str;
}
