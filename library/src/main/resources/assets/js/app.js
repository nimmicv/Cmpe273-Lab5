
var rootURL = "http://localhost:8001/library";
$(":button").click(function() {
	var isbn = this.id;
    alert('About to report lost on ISBN ' + isbn);
    alert(rootURL);
    var tr = $(this).closest('tr');
    var id = tr.find('#status');
    alert(id.text());
	$.ajax({
	    type: "PUT",
	    url: rootURL + '/v1/books/'+ isbn + '?status=lost',
	    contentType: "application/json",
	    success: function(data){
	    	alert(JSON.stringify(data));
			alert(data.book.status);
			$(id).text(data.book.status);
		}
	});
});

