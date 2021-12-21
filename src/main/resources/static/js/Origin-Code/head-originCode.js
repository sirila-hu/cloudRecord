$(".sirilahu-Top").load("/top_bar.html .navbar", function (){
	$("a.search-item").click(function(){
		var jumpTo = $(this).attr("jump-to");
		$(this).parents("form.search-form").attr("action", jumpTo);
		$(this).parents("form.search-form").find(".btn-search").text($(this).text());
	});
});
$(".sirilahu-footer").load("/top_bar.html #footer");


			