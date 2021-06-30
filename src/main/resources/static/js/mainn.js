
var menuSet = {
    '1' : 'korea',
    '2' : 'china',
    '3' : 'us'
};

var locationSet = {
    '1' : 'gaepo',
    '2' : 'seocho'
};

function calljson() {
    var menus = myMSG;
    var mymenu = document.getElementById('mymenu');
    for (var menudata in menus) {
        var newbutton = document.createElement("button");
        newbutton.setAttribute('class', 'mymenubutton');
        newbutton.setAttribute('onclick', 'cancelRoom(this)');
        newbutton.setAttribute('value', menus[menudata].category_name + "|" + menus[menudata].enter_at + "|" + menus[menudata].room_id);
        newbutton.innerHTML = "<span class='menu'>" + menus[menudata].category_name + " 먹어요!</span></br>" + menus[menudata].enter_at + " ~</br><span class='participate_data'>" + menus[menudata].participants + "</span>";
        mymenu.appendChild(newbutton);
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
    var timeFrom = document.getElementsByName("meetTimeFrom")[0].value;
    var timeTo = document.getElementsByName("meetTimeTo")[0].value;
    var menu_html = document.querySelector('input[name="menu"]:checked');
    var menu = menu_html.value;
    var location_html = document.querySelector('input[name="location"]:checked');
    var location = location_html.value;
    //$(document).ready(function(){
    //  $('submit').click(f)
    //})
    menuSelect(menu_html);
    locationSelect(location_html);

    if (timeFrom > timeTo) {
        alert('시작 시간이 마지막 시간보다 빠릅니다');
    } else if (confirm(timeFrom + "시부터 " + timeTo + "까지" + locationSet[location] + "에서" + menuSet[menu] + "예약을 하시겠습니까?")) {
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
                if (data == "flase")
                    alert("방 생성 및 참여에 실패했습니다");
                else
                    alert("등록이 완료되었습니다.");
                window.location.replace("http://localhost:8080/main");
            }
            , error: function (a, b, err) {
                console.log(err);
            }
        })
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
            , error: function (a, b, err) {
                console.log(err);
            }
        })
    }
}