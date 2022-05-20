function christmas_workshop_success(response) {
	default_challenge_success(response);
}

function christmas_workshop_error(response) {
	default_challenge_error(response);
}

function christmas_workshop_postfilter(form) {
	var content = $("#xssmas-input").val();
	$(".chat-room").append(`
			<div class="row">
				<div class="col-sm-12 text-right">
					<p class="txt-msg me">${content}</p>
				</div>
			</div>
	`);
	
	form.reset();
}
