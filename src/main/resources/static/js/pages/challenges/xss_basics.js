$(document).ready(function() {
	$(".people-i-like").dataTable({
		// Hijack Datatables, make it a challenge!
		initComplete : function() {
		    var self = this.api();
		    var filter_input = $('.dataTables_filter input').unbind();
		    var search_button = $('<button type="button" class="btn btn-default btn-sm">Search</button>').click(function() {
		        $("#challenge-form :input[name='msg']").val(filter_input.val());
		        $("#challenge-form").submit();
		        
		        self.search(filter_input.val()).draw();
		        $(".dataTables_empty").html('No results found for "' + filter_input.val() + '"');
		    });
		    var clear_button = $('<button type="button" class="btn btn-default btn-sm">Clear</button>').click(function() {
		        filter_input.val('');
		        self.search(filter_input.val()).draw();
		    });

		    $(document).keypress(function (event) {
		        if (event.which == 13) {
		            search_button.click();
		        }
		    });

		    $('.dataTables_filter').append(search_button, clear_button);
		}
	});
});

function xss_basics_success(response) {
	default_challenge_success(response);
}

function xss_basics_error(response) {
	default_challenge_error(response);
}
