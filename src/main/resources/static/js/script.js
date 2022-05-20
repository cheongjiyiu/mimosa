$(document).ready(function() {
	$("#side-menu").metisMenu();
	$(".data-table").DataTable({
		"pageLength" : 25
	});

	$('ul.nav a').filter(function() {
		return this.href == window.location;
	}).addClass('active').parents('#side-menu li').addClass('active');

	$("form").simplifySubmit();
});
