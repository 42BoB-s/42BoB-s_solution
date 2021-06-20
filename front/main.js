function calljson() {
	var x = new XMLHttpRequest();
	x.open('GET', 'https://s-um.github.io/bobs_test/test.json');

	x.onload = () => {
		var menus = JSON.parse(x.response);
		var mymenu = document.getElementById('mymenu');
		for (var menudata in menus) {
			var newbutton = document.createElement("button");
			newbutton.setAttribute('class', 'mymenubutton');
			newbutton.setAttribute('onclick', 'console.log("click")');

			newbutton.innerHTML = "<span class='menu'>" + menus[menudata].menu + " 먹어요!</span></br>" + menus[menudata].time + " ~";
			mymenu.appendChild(newbutton);
		}
	}

	x.send();
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
	console.log("time : " + time);
	console.log("menu : " + menu);
}
