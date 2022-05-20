$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

$.validator.addMethod("regex", function(value, element, regexp) {
	var re = new RegExp(regexp);
	return this.optional(element) || re.test(value);
}, "Please check your input.");

$.fn.simplifyValidate = function(rules, messages) {
	$(this)
			.validate(
					{
						rules : rules,
						messages : messages,
						errorElement : "em",
						errorPlacement : function(error, element) {
							error.addClass("help-block");
							element.parents(".input-group").addClass(
									"has-feedback");

							if (element.prop("type") === "checkbox") {
								error.insertAfter(element.parent("label"));
							} else {
								error.insertAfter(element.parent().parent())
										.wrap("<div class='error-div'></div>");
							}

							if (!element.next("span")[0]) {
								$(
										"<span class='glyphicon glyphicon-remove form-control-feedback'></span>")
										.insertAfter(element);
							}

						},
						success : function(label, element) {
							if (!$(element).next("span")[0]) {
								$(
										"<span class='glyphicon glyphicon-ok form-control-feedback'></span>")
										.insertAfter($(element));
							}
						},
						highlight : function(element, errorClass, validClass) {
							$(element).parents(".input-group").addClass(
									"has-error").removeClass("has-success");
							$(element).next("span")
									.addClass("glyphicon-remove").removeClass(
											"glyphicon-ok");
						},
						unhighlight : function(element, errorClass, validClass) {
							$(element).parents(".input-group").addClass(
									"has-success").removeClass("has-error");
							$(element).next("span").addClass("glyphicon-ok")
									.removeClass("glyphicon-remove");
						}
					});
}

$.fn.simplifyResetForm = function() {
	$(this).validate().resetForm();
	$(this)[0].reset();
	
	$(this).find('.has-error').removeClass("has-error");
	$(this).find('.has-success').removeClass("has-success");
	$(this).find('.form-control-feedback').remove();
}

$.fn.simplifySubmit = function() {
	$(this).unbind("submit");
	$(this).submit(
			function(e) {
				// Abort if no encoding given
				if (typeof $(this).attr("enctype") === 'undefined')
					return;
				else
					e.preventDefault();

				// Check for code
				if ($("input[name*='code']", this).length
						&& editor !== undefined)
					$("input[name*='code']", this).val(editor.getValue());

				// Check Encoding
				let enctype = $(this).attr('enctype');
				// Check Method
				let method = typeof $(this).attr('method') !== 'undefined' ? $(
						this).attr('method') : 'GET';
				// Check Action
				let action = typeof $(this).attr('action') !== 'undefined' ? $(
						this).attr('action') : window.location.pathname;
				// Check Controls
				let control = $(this).attr('data-control');

				if (typeof window[control + '_prefilter'] === 'function')
					window[control + '_prefilter'](this);

				// Handle Data
				let data = enctype === 'multipart/form-data' ? new FormData(
						this) : enctype === 'application/json' ? JSON
						.stringify($(this).serializeObject()) : $(this)
						.serializeObject();
				let processData = enctype === 'multipart/form-data' ? false
						: true;
				let contentType = enctype === 'multipart/form-data' ? false
						: enctype;

				if (typeof window[control + '_validate'] === 'function')
					if (!window[control + '_validate'](data))
						return;

				// Prepare loading (small timeout)
				let loader;

				// Send request
				$.ajax({
					url : action,
					type : method,
					data : data,
					contentType : contentType,
					processData : processData,
					beforeSend : function(xhr) {
						if (typeof window[control + '_header'] === 'function')
							window[control + '_header'](xhr);
						
						xhr.setRequestHeader(_csrf_header, _csrf_token);
						loader = setTimeout(function() {
							$('.loader').addClass('loading');
						}, 100);
					},
					success : function(data, textStatus, jqXHR) {
						if (typeof window[control + '_success'] === 'function')
							window[control + '_success'](data);
					},
					error : function(jqXHR, textStatus, errorThrown) {
						if (typeof window[control + '_error'] === 'function')
							window[control + '_error'](jqXHR.responseJSON);
					},
					complete : function(jqXHR, textStatus) {
						clearTimeout(loader);
						$('.loader').removeClass('loading');
					}
				});

				if (typeof window[control + '_postfilter'] === 'function')
					window[control + '_postfilter'](this);
			});
}
