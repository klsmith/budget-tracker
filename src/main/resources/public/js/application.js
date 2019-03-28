function handleSubmit(event) {
	event.preventDefault();
	var form = event.target;
	var json = formToJson(form);
	var method = form.getAttribute("method");
	var url = form.getAttribute("action");
	var async = true;
	var refresh = form.getAttribute("refresh");
	var callback = function(status, response) {
		if (refresh) {
			location.reload();
		}
	};
	sendJson(json, method, url, async, callback);
	return false;
}

function formToJson(form) {
	var inputList = form.getElementsByTagName("input");
	var data = {};
	for (var i = 0; i < inputList.length; i++) {
		var input = inputList[i];
		if ("submit" !== input.type) {
			data[input.name] = input.value;
		}
	}
	return JSON.stringify(data);
}

function sendJson(json, method, url, async = true, callback) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState === 4 && callback) {
			callback(request.status, request.responseText);
		}
	};
	request.open(method, url, async);
	request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
	request.send(json);
}
