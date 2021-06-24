function calljson() {
    var menus = myMSG;
    console.log(menus);
    var mymenu = document.getElementById('mymenu');
    for (var menudata in menus) {
        var newbutton = document.createElement("button");
        newbutton.setAttribute('class', 'mymenubutton');
        newbutton.setAttribute('onclick', 'console.log("click")');
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
}
