var editor;

if ($("#editor").length) {
	var language = $("#editor").hasClass("editor-javascript") ? "javascript" : "java";
	
	editor = ace.edit("editor");
	editor.setOptions({
		theme : "ace/theme/eclipse",
		mode : `ace/mode/${language}`,
		enableBasicAutocompletion : true,
		enableLiveAutocompletion : true,
		enableSnippets : true,
		fontSize : "10pt"
	});
}
