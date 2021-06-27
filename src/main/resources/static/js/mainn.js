function calljson() {
    var menus = myMSG;
    console.log(menus);
    var mymenu = document.getElementById('mymenu');
    for (var menudata in menus) {
        var newbutton = document.createElement("button");
        newbutton.setAttribute('class', 'mymenubutton');
        newbutton.setAttribute('onclick', 'cancelRoom(this)');
        newbutton.setAttribute('value', menus[menudata].category_name + "|" + menus[menudata].enter_at);
        newbutton.innerHTML = "<span class='menu'>" + menus[menudata].category_name + " 먹어요!</span></br>" + menus[menudata].enter_at + " ~";
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
    document.getElementById(self.value).style.backgroundColor = "rgb(171,227,228)";
}

function getFormData() {
    var time = document.getElementsByName("meetTime")[0].value;
    var menu = document.querySelector('input[name="menu"]:checked').value;
    //$(document).ready(function(){
      //  $('submit').click(f)
    //})
    console.log("time : " + time);
    console.log("menu : " + menu);

    if (confirm(time + "시에 있는 " + menu + "예약을 하시겠습니까?")) {
        var json_data = {
            id: myID,
            menu: menu,
            time: time
        }
        $.ajax({
            url: "enter"
            , method: "POST"
            , dataType: "text"
            , data: JSON.stringify(json_data)
            , contentType: "application/json; charset=UTF-8"
            , success: function () {
                alert("등록이 완료되었습니다.");
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
    var json_data = {
        id : myID,
        menu : menu,
        time : time
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
            }
            , error: function (a, b, err) {
                console.log(err);
            }
        })
    }
}
