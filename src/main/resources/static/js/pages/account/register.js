$(document)
		.ready(
				function() {
					$('#reg-form')
							.simplifyValidate(
									{
										username : {
											required : true,
											minlength : 3
										},
										password : {
											required : true,
											minlength : 8
										},
										confirmPassword : {
											equalTo : "#password-input"
										},
										email : {
											required : true,
											email : true
										},
										adminNo : {
											required : true,
											regex : /[a-zA-Z][0-9]{7}/,
										},
										firstName : {
											required : true,
										},
										lastName : {
											required : true,
										},
										batchId : {
											required : true,
										},
									},
									{
										username : {
											required : "Please enter a username",
											minlength : "Your username must consist of at least 3 characters"
										},
										password : {
											required : "Please provide a password",
											minlength : "Your password must be at least 8 characters long"
										},
										confirmPassword : {
											equalTo : "Your passwords do not match"
										},
										adminNo : {
											required : "Please provide your admin number.",
											regex : "Please ensure your admin number follows an appropriate format (e.g. p1430996)"
										},
										email : {
											required : "Please enter your email.",
											email : "Please enter a valid email address."
										},
										firstName : {
											required : "Please enter your first name."
										},
										lastName : {
											required : "Please enter your last name."
										},
										batchId : {
											required : "Please select your class.",
										},
									});
				});

function register_validate(data) {
	return $("#reg-form").valid();
}

function register_success(response) {
	$("#reg-form").simplifyResetForm();

	$
			.notify(
					{
						icon : "fa fa-check",
						title : "User created!",
						message : "The user with the username of '"
								+ response.username
								+ "' has been created! An email has been sent to you to authenticate your account."
					}, {
						type : "success",
						allow_dismiss : false
					});
}

function register_error(response) {
	$.notify({
		icon : "fa fa-times",
		title : "Failed to register user!",
		message : "Invalid input!"
	}, {
		type : "danger",
		allow_dismiss : false
	});
}
